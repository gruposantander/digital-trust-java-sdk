package com.santander.digital.verifiedid.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import com.santander.digital.verifiedid.exceptions.HTTPClientException;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class OpClientOKHttpImp implements OpClient {

    private final OkHttpClient httpClient;

    OpClientOKHttpImp(final OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String callSimpleGET(String url) throws HTTPClientException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        log.debug("Request: {}", request);
        final Response response;
        try {
            response = this.httpClient.newCall(request).execute();
            if (response.body() == null) {
                throw new HTTPClientException("null response body");
            }
            int responseCode = response.code();
            if (responseCode != 200) {
                log.warn(Objects.requireNonNull(response.body()).string());
                throw new HTTPClientException("Unexpected response code: " + responseCode);
            }
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            throw new HTTPClientException(e);
        }
    }

    @Override
    public String callInitAuthorize(final String url,
                                    final String requestClaims,
                                    final String auth) throws HTTPClientException {

        final RequestBody body = new FormBody.Builder()
                .add("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer")
                .add("client_assertion", auth)
                .add("request", requestClaims)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(body)
                .build();
        log.debug("Request: {}", request);
        final Response response;
        try {
            response = this.httpClient.newCall(request).execute();
            if (response.body() == null) {
                throw new HTTPClientException("null response body");
            }
            final int responseCode = response.code();
            if (responseCode != 201) {
                log.warn(Objects.requireNonNull(response.body()).string());
                throw new HTTPClientException("Unexpected response code: " + responseCode);
            }
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            throw new HTTPClientException(e);
        }
    }

    @Override
    public String callToken(final String url,
                            final String code,
                            final String redirectUri,
                            final String auth,
                            final String codeVerifier) throws HTTPClientException {
        final FormBody.Builder bodyBuilder = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", redirectUri)
                .add("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer")
                .add("client_assertion", auth);
        if(codeVerifier != null) {
            bodyBuilder.add("code_verifier", codeVerifier);
        }
        final FormBody body = bodyBuilder.build();
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(body)
                .build();
        log.debug("Request: {}", request);
        final Response response;
        try {
            response = this.httpClient.newCall(request).execute();
            if (response.body() == null) {
                throw new HTTPClientException("null response body");
            }
            final int responseCode = response.code();
            if (responseCode != 200) {
                log.warn(Objects.requireNonNull(response.body()).string());
                throw new HTTPClientException("Unexpected response code: " + responseCode);
            }
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            throw new HTTPClientException(e);
        }
    }
}
