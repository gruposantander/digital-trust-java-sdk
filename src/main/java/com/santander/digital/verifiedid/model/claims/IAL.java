package com.santander.digital.verifiedid.model.claims;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IAL {
    ONE(1),
    TWO(2),
    THREE(3);
    private Integer value;
}
