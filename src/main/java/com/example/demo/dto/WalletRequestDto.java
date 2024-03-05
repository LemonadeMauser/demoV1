package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletRequestDto {
    @NotNull(message = "The field walletId cannot be empty.")
    @Positive(message = "The field walletId cannot be negative.")
    Long walletId;
    @NotNull(message = "The field operationType cannot be empty.")
    @Pattern(regexp = "DEPOSIT|WITHDRAW",
            message = "The field operationType can only contain the value DEPOSIT or WITHDRAW.")
    String operationType;
    @NotNull(message = "The field amount cannot be empty.")
    @Positive(message = "The field positive cannot be negative.")
    BigDecimal amount;
}
