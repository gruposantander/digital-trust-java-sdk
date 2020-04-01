package com.santander.digital.verifiedid.model.claims.verifying;

import java.math.BigDecimal;
import java.util.Currency;

public class Balance {
    public static Property<Currency> currency() {
        return new Property<>("currency");
    }

    public static PropertyComparator<BigDecimal> amount() {
        return new PropertyComparator<>("amount");
    }
}
