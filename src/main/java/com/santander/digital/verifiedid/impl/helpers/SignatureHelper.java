package com.santander.digital.verifiedid.impl.helpers;

import lombok.Builder;
import org.apache.cxf.rs.security.jose.common.JoseType;
import org.apache.cxf.rs.security.jose.jwa.SignatureAlgorithm;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKey;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKeys;
import org.apache.cxf.rs.security.jose.jws.JwsCompactConsumer;
import org.apache.cxf.rs.security.jose.jws.JwsCompactProducer;
import org.apache.cxf.rs.security.jose.jws.JwsHeaders;
import org.apache.cxf.rs.security.jose.jws.JwsJwtCompactProducer;
import org.apache.cxf.rs.security.jose.jwt.JwtClaims;
import org.apache.cxf.rs.security.jose.jwt.JwtUtils;
import org.json.JSONObject;

import java.time.Clock;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public class SignatureHelper {

    private final Clock clock;

    SignatureHelper(final Clock clock) {
        this.clock = clock;
    }

    public String createJWT(final JsonWebKey jwk,
                            final String algorithm,
                            final String issuer,
                            final String audience,
                            final Long ttl,
                            final String sub,
                            final String jti,
                            final JSONObject payload) {
        final JsonWebKey localJwk = cloneJWK(jwk);
        final JwsHeaders headers = new JwsHeaders();
        headers.setKeyId(localJwk.getKeyId());
        headers.setAlgorithm(algorithm);
        headers.setType(JoseType.JWT);
        final JwtClaims jwtClaims = JwtUtils.jsonToClaims(payload.toString());
        final Long now = clock.instant().getEpochSecond();
        localJwk.setAlgorithm(algorithm);
        jwtClaims.setSubject(sub);
        jwtClaims.setIssuer(issuer);
        jwtClaims.setAudience(audience);
        jwtClaims.setIssuedAt(now);
        jwtClaims.setNotBefore(now);
        jwtClaims.setExpiryTime(now + ttl);
        jwtClaims.setProperty("jti", jti);
        final JwsCompactProducer jwsCompactProducer = new JwsJwtCompactProducer(headers, jwtClaims);
        return jwsCompactProducer.signWith(localJwk);
    }

    private JsonWebKey cloneJWK(final JsonWebKey jwk) {
        return new JsonWebKey(jwk.asMap().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    public boolean verifySignature (final JsonWebKeys jwks, final JwsCompactConsumer jwt) {
        final String keyId = jwt.getJwsHeaders().getKeyId();
        final String algorithmAsString = jwt.getJwsHeaders().getAlgorithm();
        final SignatureAlgorithm algorithm = SignatureAlgorithm.getAlgorithm(algorithmAsString);
        return jwt.verifySignatureWith(jwks.getKey(keyId), algorithm);
    }
}
