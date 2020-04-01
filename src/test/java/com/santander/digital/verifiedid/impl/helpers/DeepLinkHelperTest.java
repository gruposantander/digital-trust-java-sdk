package com.santander.digital.verifiedid.impl.helpers;


import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeepLinkHelperTest {

    private DeepLinkHelper deepLinkHelper;

    @Before
    public void before () {
        deepLinkHelper = new DeepLinkHelper();
    }

    @Test
    public void isDeepLinkShouldReturnTrueIfLinkIsDeepLink () {
        Boolean isDeepLink = deepLinkHelper.isDeepLink("eBay://item/view?id=360703170135");
        assertThat("wrong deep link identification", isDeepLink, is(true));
    }

    @Test
    public void isDeepLinkShouldReturnTrueIfLinkIsHttp () {
        Boolean isDeepLink = deepLinkHelper.isDeepLink("http://item/view?id=360703170135");
        assertThat("wrong deep link identification", isDeepLink, is(false));
    }

   @Test
    public void isDeepLinkShouldReturnTrueIfLinkIsHttpWithPort () {
        Boolean isDeepLink = deepLinkHelper.isDeepLink("http://localhost:8080/callback");
        assertThat("wrong deep link identification", isDeepLink, is(false));
    }

    @Test
    public void isDeepLinkShouldReturnTrueIfLinkIsHttps () {
        Boolean isDeepLink = deepLinkHelper.isDeepLink("https://item/view?id=360703170135");
        assertThat("wrong deep link identification", isDeepLink, is(false));
    }

    @Test
    public void isDeepLinkShouldReturnTrueIfLinkIsHttpsWithPort () {
        Boolean isDeepLink = deepLinkHelper.isDeepLink("https://item/view?id=360703170135");
        assertThat("wrong deep link identification", isDeepLink, is(false));
    }

    @Test
    public void isDeepLinkShouldReturnTrueIfLinkIsMalformed () {
        Boolean isDeepLink = deepLinkHelper.isDeepLink("/item/view?id=360703170135");
        assertThat("wrong deep link identification", isDeepLink, is(false));
    }

    @Test
    public void isDeepLinkShouldReturnTrueIfLinkIsNull () {
        Boolean isDeepLink = deepLinkHelper.isDeepLink(null);
        assertThat("wrong deep link identification", isDeepLink, is(false));
    }

    @Test
    public void isDeepLinkShouldReturnTrueIfLinkIsBlank () {
        Boolean isDeepLink = deepLinkHelper.isDeepLink(" ");
        assertThat("wrong deep link identification", isDeepLink, is(false));
    }

    @Test
    public void isDeepLinkShouldReturnTrueIfLinkIsEmpty () {
        Boolean isDeepLink = deepLinkHelper.isDeepLink("");
        assertThat("wrong deep link identification", isDeepLink, is(false));
    }
}