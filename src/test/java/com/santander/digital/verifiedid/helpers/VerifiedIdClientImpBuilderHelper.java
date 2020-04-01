package com.santander.digital.verifiedid.helpers;

import com.santander.digital.verifiedid.impl.VerifiedIdClientImpBuilder;
import com.santander.digital.verifiedid.impl.handlers.InitAuthorizeHandler;
import com.santander.digital.verifiedid.impl.handlers.JWKSHandler;
import com.santander.digital.verifiedid.impl.handlers.WellKnownHandler;
import com.santander.digital.verifiedid.impl.helpers.SignatureHelper;
import com.santander.digital.verifiedid.impl.handlers.TokenHandler;
import com.santander.digital.verifiedid.impl.helpers.RandomHelper;

public class VerifiedIdClientImpBuilderHelper extends VerifiedIdClientImpBuilder {

    public VerifiedIdClientImpBuilderHelper withWellKnownHandler(WellKnownHandler wellKnownHandler) {
        this.wellKnownHandler = wellKnownHandler;
        return this;
    }

    public VerifiedIdClientImpBuilderHelper withJWKSHandler(JWKSHandler jwksHandler) {
        this.jwksHandler = jwksHandler;
        return this;
    }

    public VerifiedIdClientImpBuilderHelper withInitAuthorizeHandler(InitAuthorizeHandler initAuthorizeHandler) {
        this.initAuthorizeHandler = initAuthorizeHandler;
        return this;
    }

    public VerifiedIdClientImpBuilderHelper withTokenHandler(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
        return this;
    }

    public VerifiedIdClientImpBuilderHelper withSignatureHelper (SignatureHelper signatureHelper) {
        this.signatureHelper = signatureHelper;
        return this;
    }

    public VerifiedIdClientImpBuilderHelper withUUIDHelper (RandomHelper randomHelper) {
        this.randomHelper = randomHelper;
        return this;
    }

}
