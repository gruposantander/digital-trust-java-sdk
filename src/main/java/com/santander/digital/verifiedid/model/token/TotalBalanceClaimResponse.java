package com.santander.digital.verifiedid.model.token;

import lombok.Value;

import java.math.BigDecimal;
import java.util.Currency;

@Value
public class TotalBalanceClaimResponse {
    private Currency currency;
    private BigDecimal amount;
}
