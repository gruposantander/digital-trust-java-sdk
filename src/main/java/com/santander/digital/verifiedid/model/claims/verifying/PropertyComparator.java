package com.santander.digital.verifiedid.model.claims.verifying;

public class PropertyComparator<T> extends Property<T> {
    public PropertyComparator(String propertyName) {
        super(propertyName);
    }

    public PropertyComparator<T> gt (T operand) {
        this.operand = operand;
        this.operator = Operator.GREATER_THAN;
        return this;
    }

    public PropertyComparator<T> gte (T operand) {
        this.operand = operand;
        this.operator = Operator.GREATER_THAN_OR_EQUAL;
        return this;
    }

    public PropertyComparator<T> lt (T operand) {
        this.operand = operand;
        this.operator = Operator.LESS_THAN;
        return this;
    }

    public PropertyComparator<T> lte (T operand) {
        this.operand = operand;
        this.operator = Operator.LESS_THAN_OR_EQUAL;
        return this;
    }
}
