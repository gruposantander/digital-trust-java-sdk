package com.santander.digital.verifiedid.model.claims.verifying;

import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

@EqualsAndHashCode
@Getter
public class Property<T> {
    private final String propertyName;
    protected T operand;
    protected Operator operator;

    public Property(String propertyName) {
        this.propertyName = propertyName;
    }

    public Property<T> eq(final T operand) {
        this.operand = operand;
        this.operator = Operator.EQUAL;
        return this;
    }

    public JSONObject toJSON () {
        final JSONObject json = new JSONObject();
        try {
            json.put(operator.getRepresentation(), operand);
        } catch (JSONException e) {
            throw new DigitalIdGenericException(e);
        }
        return json;
    }
}
