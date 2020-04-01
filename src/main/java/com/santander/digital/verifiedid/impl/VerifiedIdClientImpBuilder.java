package com.santander.digital.verifiedid.impl;

import com.santander.digital.verifiedid.http.OpClient;
import lombok.Getter;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKey;
import com.santander.digital.verifiedid.http.OpClientOKHttpImpBuilder;
import com.santander.digital.verifiedid.impl.handlers.InitAuthorizeHandler;
import com.santander.digital.verifiedid.impl.handlers.JWKSHandler;
import com.santander.digital.verifiedid.impl.handlers.TokenHandler;
import com.santander.digital.verifiedid.impl.handlers.WellKnownHandler;
import com.santander.digital.verifiedid.impl.helpers.DeepLinkHelper;
import com.santander.digital.verifiedid.impl.helpers.RandomHelper;
import com.santander.digital.verifiedid.impl.helpers.SignatureHelper;

import java.time.Clock;

import static com.santander.digital.verifiedid.util.KeyUtils.readJWKFromFile;

@Getter
public class VerifiedIdClientImpBuilder {

    private String wellKnownURI;
    private JsonWebKey privateJWK;
    private String algorithm = "RS256";
    private String clientId;
    private OpClient opClient = new OpClientOKHttpImpBuilder().build();
    protected WellKnownHandler wellKnownHandler = new WellKnownHandler();
    protected JWKSHandler jwksHandler = new JWKSHandler();
    protected InitAuthorizeHandler initAuthorizeHandler = new InitAuthorizeHandler();
    protected TokenHandler tokenHandler = new TokenHandler();
    protected SignatureHelper signatureHelper = SignatureHelper.builder()
            .clock(Clock.systemDefaultZone())
            .build();
    protected RandomHelper randomHelper = new RandomHelper();
    protected DeepLinkHelper deepLinkHelper = new DeepLinkHelper();

    public VerifiedIdClientImpBuilder withWellKnownURI(String wellKnownURI) {
        this.wellKnownURI = wellKnownURI;
        return this;
    }

    public VerifiedIdClientImpBuilder withPrivateJWK(JsonWebKey privateJWK) {
        this.privateJWK = privateJWK;
        return this;
    }
    
    public VerifiedIdClientImpBuilder withPrivateJWKFromFile(String path) {
        this.privateJWK = readJWKFromFile(path);
        return this;
    }

    public VerifiedIdClientImpBuilder withAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public VerifiedIdClientImpBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public VerifiedIdClientImpBuilder withOpClient (OpClient opClient) {
        this.opClient = opClient;
        return this;
    }

    public VerifiedIdClientImp build() {
        return new VerifiedIdClientImp(this);
    }
}