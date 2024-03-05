package com.example.demo.controller;

import com.example.demo.dto.WalletRequestDto;
import com.example.demo.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController {

    private  final WalletService service;

    @GetMapping("/wallets/{WALLET_UUID}")
    public ResponseEntity<String> getAmount (@PathVariable Long WALLET_UUID) {
        return service.getAmount(WALLET_UUID);
    }

    @PostMapping("/wallet")
    public ResponseEntity<?> accountOperation (@Valid @RequestBody WalletRequestDto dto) {
        return service.accountOperation(dto);
    }
}
