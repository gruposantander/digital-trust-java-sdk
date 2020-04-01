package com.santander.digital.verifiedid.extensions;

import com.santander.digital.verifiedid.model.claims.sharing.Claim;
import com.santander.digital.verifiedid.model.claims.sharing.Claims;

public class CustomClaims extends Claims {

    public Claim creditScore () {
        return this.addClaim("credit_score");
    }
}
