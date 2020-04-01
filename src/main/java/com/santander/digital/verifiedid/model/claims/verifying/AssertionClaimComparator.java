package com.santander.digital.verifiedid.model.claims.verifying;


public class AssertionClaimComparator <T> extends AssertionClaimSimple<T>{

    public AssertionClaimComparator(String claimName) {
        super(claimName);
    }

    public AssertionClaimComparator<T> gt (T operand) {
        this.operand = operand;
        this.operator = Operator.GREATER_THAN;
        return this;
    }

    public AssertionClaimComparator<T> gte (T operand) {
        this.operand = operand;
        this.operator = Operator.GREATER_THAN_OR_EQUAL;
        return this;
    }

    public AssertionClaimComparator<T> lt (T operand) {
        this.operand = operand;
        this.operator = Operator.LESS_THAN;
        return this;
    }

    public AssertionClaimComparator<T> lte (T operand) {
        this.operand = operand;
        this.operator = Operator.LESS_THAN_OR_EQUAL;
        return this;
    }
}
