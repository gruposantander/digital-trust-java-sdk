package com.santander.digital.verifiedid.impl.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeepLinkHelper {

    private final Pattern pattern = Pattern.compile("(.*):");

    public Boolean isDeepLink(final String link) {
        if(link == null) {
            return Boolean.FALSE;
        }
        final Matcher matcher = pattern.matcher(link);
        if(matcher.find()) {
            final String prefix = matcher.group();
            return !prefix.toLowerCase().matches("https?:.*");
        }
        return Boolean.FALSE;
    }
}
