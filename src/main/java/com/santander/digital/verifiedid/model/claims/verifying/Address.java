package com.santander.digital.verifiedid.model.claims.verifying;

public class Address {

    public static Property<String> formatted() {
        return new Property<>("formatted");
    }

    public static Property<String> streetAddress() {
        return new Property<>("street_address");
    }

    public static Property<String> postalCode() {
        return new Property<>("postal_code");
    }

    public static Property<String> locality() {
        return new Property<>("locality");
    }

    public static Property<String> region() {
        return new Property<>("region");
    }

    public static Property<String> country() {
        return new Property<>("country");
    }
}
