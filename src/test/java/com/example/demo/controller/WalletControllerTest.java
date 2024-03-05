package com.example.demo.controller;

import com.example.demo.dto.WalletRequestDto;
import com.example.demo.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WalletControllerTest {

    private final WalletService service = mock(WalletService.class);
    private final WalletController controller = new WalletController(service);

    @Test
    void testGetAmount() {
        Long walletUuid = 123L;
        String expectedResponseBody = "You have 1000 in your wallet";
        when(service.getAmount(walletUuid)).thenReturn(ResponseEntity.ok().body(expectedResponseBody));

        ResponseEntity<String> response = controller.getAmount(walletUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void testAccountOperationDeposit() {
        WalletRequestDto dto =
                new WalletRequestDto(123L, "DEPOSIT", new BigDecimal("500"));
        String expectedResponseBody = "You have 1500 in your wallet";
        when(service.accountOperation(dto)).thenReturn(ResponseEntity.ok().body(expectedResponseBody));

        ResponseEntity<?> response = controller.accountOperation(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void testAccountOperationWithdrawal() {
        WalletRequestDto dto =
                new WalletRequestDto(123L, "WITHDRAWAL", new BigDecimal("200"));
        String expectedResponseBody = "You have 800 in your wallet";
        when(service.accountOperation(dto)).thenReturn(ResponseEntity.ok().body(expectedResponseBody));

        ResponseEntity<?> response = controller.accountOperation(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void testGetAmountWithInvalidWalletId() {
        Long walletUuid = 999L;
        when(service.getAmount(walletUuid))
                .thenReturn(ResponseEntity.badRequest()
                        .body("Wallet with id= " + walletUuid + " not found."));

        ResponseEntity<String> response = controller.getAmount(walletUuid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Wallet with id= 999 not found.", response.getBody());
    }

    @Test
    void testAccountOperationWithWithdrawalExceedsBalance() {
        WalletRequestDto dto =
                new WalletRequestDto(123L, "WITHDRAWAL", new BigDecimal("2000"));
        when(service.accountOperation(dto)).
                thenReturn(ResponseEntity.badRequest().body("Insufficient funds in the wallet."));

        ResponseEntity<?> response = controller.accountOperation(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Insufficient funds in the wallet.", response.getBody());
    }
}