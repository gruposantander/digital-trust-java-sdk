package com.santander.digital.verifiedid.impl.helpers;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomHelper {

    public String randomString() {
        final int size = 32;
        final SecureRandom random = new SecureRandom();
        final byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
