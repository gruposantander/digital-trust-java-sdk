package com.santander.digital.verifiedid.model.claims.verifying;

import com.santander.digital.verifiedid.exceptions.DigitalIdGenericException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static com.santander.digital.verifiedid.util.CommonUtils.isoDate;

public class AssertionClaimSimple <T> extends AssertionClaim <T> {

    public AssertionClaimSimple(String claimName) {
        super(claimName);
    }

    protected T operand;
    protected Operator operator;

    public AssertionClaimSimple<T> eq(final T operand) {
        this.operand = operand;
        this.operator = Operator.EQUAL;
        return this;
    }

    @Override
    public JSONObject toJSON () {
        final JSONObject json = super.toJSON();
        final JSONObject assertion = new JSONObject();
        try {
            assertion.put(operator.getRepresentation(), prepareOperand(operand));
            json.put("assertion", assertion);
        } catch (JSONException e) {
            throw new DigitalIdGenericException(e);
        }
        return json;
    }

    Object prepareOperand(T operand) {
        if(operand instanceof Date) {
            return isoDate((Date) operand);
        } else if (operand instanceof Number) {
            return operand;
        }
        return operand.toString();
    }
}
