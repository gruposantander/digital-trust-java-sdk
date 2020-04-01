package com.santander.digital.verifiedid.model.token;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@NoArgsConstructor
public class BalanceClaimReponse {
    private Currency currency;
    private BigDecimal amount;
}
