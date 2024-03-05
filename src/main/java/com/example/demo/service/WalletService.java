package com.example.demo.service;

import com.example.demo.dto.WalletRequestDto;
import com.example.demo.model.Wallet;
import com.example.demo.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repository;

    @Transactional(readOnly = true)
    public ResponseEntity<String> getAmount(Long walletUuid) {
        Optional<Wallet> wallet = walletExistChecker(walletUuid);
        return wallet.map(value -> ResponseEntity.ok().body("You have " + value.getAmount() + " in your wallet"))
                .orElseGet(() -> ResponseEntity.badRequest().body("Wallet with id= " + walletUuid + " not found."));
    }

    @Transactional
    public ResponseEntity<String> accountOperation(WalletRequestDto dto) {
        String operationName = dto.getOperationType();
        if (operationName.equalsIgnoreCase("DEPOSIT")) {
            return ResponseEntity.ok().body("You have " + addingAmount(dto) + " in your wallet");
        } else {
            return withdrawalFromAccount(dto);
        }
    }

    private BigDecimal addingAmount(WalletRequestDto dto) {
        Wallet acc = repository.findAccountByWalletId(dto.getWalletId())
                .orElseGet(() -> repository.save(dtoToEntity(dto)));
        acc.setAmount(acc.getAmount().add(dto.getAmount()));
        repository.save(acc);
        return acc.getAmount();
    }

    private ResponseEntity<String> withdrawalFromAccount(WalletRequestDto dto) {
        Optional<Wallet> walletFromDb = walletExistChecker(dto.getWalletId());

        if (walletFromDb.isEmpty())
            return ResponseEntity.badRequest().body("Wallet with id= " + dto.getWalletId() + " not found.");

        Wallet wallet = walletFromDb.get();
        BigDecimal newAmount = wallet.getAmount().subtract(dto.getAmount());
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Insufficient funds in the wallet.");
        }
        wallet.setAmount(newAmount);
        repository.save(wallet);
        return ResponseEntity.ok().body("You have " + wallet.getAmount() + " in your wallet");
    }

    private Wallet dtoToEntity(WalletRequestDto dto) {
        Wallet wallet = new Wallet();
        wallet.setAmount(new BigDecimal(0));
        wallet.setWalletId(dto.getWalletId());
        return wallet;
    }

    private Optional<Wallet> walletExistChecker(Long walletId) {
        return repository.findAccountByWalletId(walletId);
    }
}
