package com.santander.digital.verifiedid.model.claims.sharing;

import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;
import com.santander.digital.verifiedid.model.claims.IAL;

@EqualsAndHashCode(exclude = "claims")
public class Claim {
    private Claims claims;
    @Getter private final String claimName;
    private Boolean essential;
    private IAL ial;
    private String purpose;

    public Claim (final String claimName, final Claims claims) {
        this.claimName = claimName;
        this.claims = claims;
    }

    public Claim (final String claimName) {
        this.claimName = claimName;
    }

    public Claim withPurpose(final String purpose) {
        final int purposeMaxLength = 300;
        if (purpose.length() > purposeMaxLength) {
            this.claims.removeClaim(this.claimName);
            throw new DigitalIdGenericException("purpose shouldn't exceed 300 chars");
        }
        this.purpose = purpose;
        return this;
    }

    public Claim withEssential(final Boolean essential) {
        this.essential = essential;
        return this;
    }

    public Claim withIAL(final IAL ial) {
        this.ial = ial;
        return this;
    }

    public JSONObject toJSON () {
        final JSONObject json = new JSONObject();
        try {
            json.put("essential", essential);
            json.put("purpose", purpose);
            json.put("ial", ial!=null ? ial.getValue() : null);
        } catch (JSONException e) {
            throw new DigitalIdGenericException(e);
        }
        return json;
    }
}
