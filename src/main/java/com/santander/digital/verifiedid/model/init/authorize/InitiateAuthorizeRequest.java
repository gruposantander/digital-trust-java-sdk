package com.santander.digital.verifiedid.model.init.authorize;

import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import com.santander.digital.verifiedid.model.claims.sharing.Claim;
import com.santander.digital.verifiedid.model.claims.sharing.Claims;
import com.santander.digital.verifiedid.model.claims.verifying.AssertionClaims;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

@Data
@Builder
@Setter
public class InitiateAuthorizeRequest {

    private final String redirectUri;
    private String nonce;
    private final AssertionClaims assertionClaims;
    private final Claims claims;
    private final String state;
    private String codeChallenge;
    private final String purpose;

    public JSONObject toJSON() {
        final JSONObject json = new JSONObject();
        try {
            addVerifyingAndSharingClaims(json);
            addCodeChallenge(json);
            json.put("state", this.state);
            json.put("nonce", this.nonce);
            json.put("redirect_uri", this.redirectUri);

        } catch (JSONException e) {
            throw new DigitalIdGenericException(e);
        }
        return json;
    }

    private void addCodeChallenge(final JSONObject json) throws JSONException {
        if (Objects.nonNull(this.codeChallenge)) {
            json.put("code_challenge", this.codeChallenge);
            json.put("code_challenge_method", "S256");
        }
    }

    private void addVerifyingAndSharingClaims(final JSONObject json) throws JSONException {
        final JSONObject tokenClaims = new JSONObject();
        addIdentificationPurpose(tokenClaims);
        final JSONObject idToken = new JSONObject();
        if (Objects.nonNull(this.claims)) {
            for (Claim claim : this.claims.getClaims()) {
                idToken.put(claim.getClaimName(), claim.toJSON());
            }
        }
        if (Objects.nonNull(this.assertionClaims)) {
            idToken.put("assertion_claims", this.assertionClaims.toJSON());
        }
        tokenClaims.put("id_token", idToken);
        json.put("claims", tokenClaims);
    }

    private void addIdentificationPurpose(final JSONObject tokenClaims) throws JSONException {
        if (Objects.nonNull(this.purpose)) {
            tokenClaims.put("purpose", this.purpose);
        }
    }
}
