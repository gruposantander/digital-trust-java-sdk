package com.santander.digital.verifiedid.model.claims.verifying;

import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import com.santander.digital.verifiedid.model.claims.IAL;
import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;

@Data
public class AssertionClaim<T> {
    private final String claimName;
    private Boolean essential;
    private IAL ial;
    private String purpose;

    protected AssertionClaim(final String claimName) {
        this.claimName = claimName;
    }


    public AssertionClaim<T> withPurpose(final String purpose) {
        this.purpose = purpose;
        return this;
    }

    public AssertionClaim<T> withIAL(final IAL ial) {
        this.ial = ial;
        return this;
    }

    public AssertionClaim<T> withEssential(final Boolean essential) {
        this.essential = essential;
        return this;
    }

    public JSONObject toJSON() {
        final JSONObject jsonAssertionClaim = new JSONObject();
        try {
            jsonAssertionClaim.put("essential", this.essential);
            jsonAssertionClaim.put("purpose", this.purpose);
            jsonAssertionClaim.put("ial", this.ial);
        } catch (JSONException e) {
            throw new DigitalIdGenericException(e);
        }
        return jsonAssertionClaim;
    }
}
