package com.santander.digital.verifiedid.http;

import com.santander.digital.verifiedid.exceptions.HTTPClientException;

public interface OpClient {
    String callSimpleGET(String url) throws HTTPClientException;
    String callInitAuthorize (String url, String request, String auth) throws HTTPClientException;
    String callToken (String url, String code, String redirectUri, String auth, String codeVerifier) throws HTTPClientException;
}
