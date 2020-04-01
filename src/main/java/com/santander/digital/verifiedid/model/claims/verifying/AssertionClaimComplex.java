package com.santander.digital.verifiedid.model.claims.verifying;

import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AssertionClaimComplex extends AssertionClaim {

    public AssertionClaimComplex(String claimName) {
        super(claimName);
    }

    protected List<Property> properties = new ArrayList<>();


    @Override
    public JSONObject toJSON () {
        final JSONObject json = super.toJSON();
        final JSONObject assertion = new JSONObject();
        try {
            for (Property property: this.properties) {
                assertion.put(property.getPropertyName(), property.toJSON());
            }
            json.put("assertion", assertion);
        } catch (JSONException e) {
            throw new DigitalIdGenericException(e);
        }
        return json;
    }

    public AssertionClaimComplex withAssertion(final Property property) {
        final Optional<Property> existingProperty = this.properties.stream()
                .filter(p -> p.getPropertyName().equals(property.getPropertyName()))
                .findFirst();
        existingProperty.ifPresent(p -> this.properties.remove(p));
        this.properties.add(property);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AssertionClaimComplex that = (AssertionClaimComplex) o;
        return properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), properties);
    }
}
