package com.santander.digital.verifiedid;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.santander.digital.verifiedid.util.KeyUtils;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKey;
import org.apache.cxf.rs.security.jose.jwk.JsonWebKeys;
import org.apache.cxf.rs.security.jose.jwk.JwkUtils;
import org.apache.cxf.rs.security.jose.jws.JwsCompactConsumer;
import org.json.JSONObject;
import com.santander.digital.verifiedid.model.OPConfiguration;
import com.santander.digital.verifiedid.model.claims.sharing.Claims;
import com.santander.digital.verifiedid.model.claims.verifying.Address;
import com.santander.digital.verifiedid.model.claims.verifying.AssertionClaims;
import com.santander.digital.verifiedid.model.claims.verifying.Balance;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeResponse;
import com.santander.digital.verifiedid.model.token.AssertionClaimResponse;
import com.santander.digital.verifiedid.model.token.IdToken;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import static com.santander.digital.verifiedid.helpers.JSONHelper.readFile;
import static com.santander.digital.verifiedid.helpers.JSONHelper.readJson;

public class TestConstants {

    public static final String JWKS_STRING = readFile("./src/test/resources/http/jwks.json");
    public static final JsonWebKeys JWKS = JwkUtils.readJwkSet(JWKS_STRING);
    public static final JsonWebKey PRIVATE_JWK = KeyUtils.readJWKFromFile("./src/test/resources/private.json");
    public static final long NOW = 1580724687L;
    public static final String TEST_UUID = "11111111-1111-1111-1111-111111111111";
    public static final String TEST_CODE_VERIFIER = "verifier-11111111-1111-1111-1111-111111111111";
    public final static String CLIENT_ID = "1234";
    public static final String KID = "MY_KID";
    public static final String WELL_KNOWN_URL = "/.well-known/openid-configuration";
    public static final String WELL_KNOWN_COMPLETE_URI = "http://localhost:9099/.well-known/openid-configuration";
    public static final String JWKS_URL = "/jwks";
    public static final String JWKS_COMPLETE_URI = "http://localhost:9099/jwks";
    public static final String INIT_AUTHORIZE_URL = "/initiate-authorize";
    public static final String INIT_AUTHORIZE_COMPLETE_URI = "http://localhost:9099/initiate-authorize";
    public static final String AUTHORIZE_URL = "/web/login";
    public static final String AUTHORIZE_COMPLETE_URI = "http://localhost:9099/web/login";
    public static final String TOKEN_URL = "/token";
    public static final String TOKEN_COMPLETE_URI = "http://localhost:9099/token";
    public static final String ISSUER = "http://localhost:9099";
    public static final OPConfiguration OP_CONFIGURATION = OPConfiguration.builder()
            .initAuthorizeEndpoint(INIT_AUTHORIZE_COMPLETE_URI)
            .authorizationEndpoint(AUTHORIZE_COMPLETE_URI)
            .issuer(ISSUER)
            .jwksUri(JWKS_COMPLETE_URI)
            .jwks(null)
            .tokenEndpoint(TOKEN_COMPLETE_URI)
            .build();
    public static final OPConfiguration OP_CONFIGURATION_WITH_JWKS = OPConfiguration.builder()
            .initAuthorizeEndpoint(INIT_AUTHORIZE_COMPLETE_URI)
            .authorizationEndpoint(AUTHORIZE_COMPLETE_URI)
            .issuer(ISSUER)
            .jwksUri(JWKS_COMPLETE_URI)
            .jwks(JWKS)
            .tokenEndpoint(TOKEN_COMPLETE_URI)
            .build();
    public static final String REDIRECTION_URI = "http://localhost:9099/web/login?request_uri=urn:op.iamid.io:6nLpmuKd-VWx83Hp1i9lKPxTowPLhh58Wd_PNKf4zVt";
    public static final String URN = "urn:op.iamid.io:6nLpmuKd-VWx83Hp1i9lKPxTowPLhh58Wd_PNKf4zVt";
    public static final String NONCE = "nonce-" + TEST_UUID;
    public static final InitiateAuthorizeResponse INITIATE_AUTHORIZE_RESPONSE = InitiateAuthorizeResponse.builder()
            .codeVerifier(null)
            .expiration(NOW + 30)
            .nonce(NONCE)
            .requestObjectUri(URN)
            .redirectionUri(REDIRECTION_URI)
            .build();
    public static final InitiateAuthorizeResponse INITIATE_AUTHORIZE_RESPONSE_WITH_CODE_VERIFIER = InitiateAuthorizeResponse.builder()
            .codeVerifier(TEST_CODE_VERIFIER)
            .expiration(NOW + 30)
            .nonce(NONCE)
            .requestObjectUri(URN)
            .redirectionUri(REDIRECTION_URI)
            .build();
    public static final JSONObject WELL_KNOWN_HTTP_RESPONSE
            = readJson("./src/test/resources/http/wellKnownResponse.json");
    public static final String WELL_KNOWN_STRING
            = readFile("./src/test/resources/http/wellKnownResponse.json");
    public static final JSONObject JWKS_HTTP_RESPONSE
            = readJson("./src/test/resources/http/jwks.json");
    public static final String TOKEN_HTTP_RESPONSE
            = readFile("./src/test/resources/http/token-response.json");
    public static final String INITIATE_AUTHORIZE_HTTP_RESPONSE
            = readFile("./src/test/resources/http/initiate-authorize.json");
    public static final JSONObject INITIATE_AUTHORIZE_REQUEST_WITH_REDIRECT_URI = readJson("./src/test/resources/init-authorize-request-with-redirect-uri.json");
    public static final JSONObject INITIATE_AUTHORIZE_REQUEST_WITH_REDIRECT_URI_AND_CODE_CHALLENGE = readJson("./src/test/resources/init-authorize-request-with-redirect-uri-and-code-challenge.json");
    public static final JSONObject INITIATE_AUTHORIZE_REQUEST_WITHOUT_REDIRECT_URI = readJson("./src/test/resources/init-authorize-request-without-redirect-uri.json");
    public static final JSONObject INITIATE_AUTHORIZE_REQUEST_BASIC_WITH_GENERIC_PURPOSE = readJson("./src/test/resources/init-authorize-basic-request-with-redirect-uri-with-generic-purpose.json");
    public static final JSONObject INITIATE_AUTHORIZE_REQUEST_BASIC_WITH_REDIRECT_URI = readJson("./src/test/resources/init-authorize-basic-request-with-redirect-uri.json");
    public static final JSONObject INITIATE_AUTHORIZE_REQUEST_BASIC_WITHOUT_REDIRECT_URI = readJson("./src/test/resources/init-authorize-basic-request-without-redirect-uri.json");
    public static final JSONObject INITIATE_AUTHORIZE_REQUEST_BASIC_WITHOUT_CLAIMS = readJson("./src/test/resources/init-authorize-basic-request-without-claims.json");
    public static final JSONObject INITIATE_AUTHORIZE_REQUEST_BASIC_WITHOUT_ASSERTION_CLAIMS = readJson("./src/test/resources/init-authorize-basic-request-without-assertion-claims.json");
    public static final JSONObject INITIATE_AUTHORIZE_AUTH = readJson("./src/test/resources/init-authorize-auth.json");
    public static final String SIGNED_JWT_REQUEST = "eyJraWQiOiJ0ZXN0LWtleSIsImFsZyI6IlJTMjU2IiwidHlwIjoiSldUIn0.eyJzY29wZSI6Im9wZW5pZCIsImNsYWltcyI6eyJpZF90b2tlbiI6eyJhc3NlcnRpb25fY2xhaW1zIjp7InRvdGFsX2JhbGFuY2UiOnsiYXNzZXJ0aW9uIjp7InByb3BzIjp7ImFtb3VudCI6eyJndCI6MTB9fX19LCJiaXJ0aGRhdGUiOnsicHVycG9zZSI6IlRoaXMgaXMgd2h5IFJQIGlzIHZlcmlmeWluZyB5b3VyIERPQiIsImFzc2VydGlvbiI6eyJlcSI6IjE5NzAtMDEtMDEifX0sImFkZHJlc3MiOnsicHVycG9zZSI6IlRoaXMgaXMgd2h5IFJQIGlzIHZlcmlmeWluZyB5b3VyIGFkZHJlc3MiLCJhc3NlcnRpb24iOnsicHJvcHMiOnsiY291bnRyeSI6eyJlcSI6IlVLIn0sInBvc3RhbF9jb2RlIjp7ImVxIjoiTUsxMUFBIn19fX0sImdpdmVuX25hbWUiOnsicHVycG9zZSI6IlRoaXMgaXMgd2h5IFJQIGlzIHZlcmlmeWluZyB5b3VyIG5hbWUiLCJhc3NlcnRpb24iOnsiZXEiOiJKb2huIn19LCJmYW1pbHlfbmFtZSI6eyJwdXJwb3NlIjoiVGhpcyBpcyB3aHkgUlAgaXMgdmVyaWZ5aW5nIHlvdXIgc3VybmFtZSIsImFzc2VydGlvbiI6eyJlcSI6IkRvZSJ9fX0sImdpdmVuX25hbWUiOnsiZXNzZW50aWFsIjp0cnVlfX19LCJyZXNwb25zZV90eXBlIjoiY29kZSIsInN0YXRlIjoic3RhdGUtMTIzIiwicmVkaXJlY3RfdXJpIjoiaHR0cHM6Ly9ycC1leGFtcGxlLmNvbS9jYWxsYmFjayIsIm5vbmNlIjoibm9uY2UtMTExMTExMTEtMTExMS0xMTExLTExMTEtMTExMTExMTExMTExIiwiY2xpZW50X2lkIjoiMTIzNCIsInN1YiI6Im15U3ViIiwiaXNzIjoibXlzZWxmIiwiYXVkIjoiaHR0cHM6Ly9vcC5pYW1pZC5pbyIsImlhdCI6MTU4MDcyNDY4NywibmJmIjoxNTgwNzI0Njg3LCJleHAiOjE1ODA3MjQ3MTcsImp0aSI6Im15LWp0aSJ9.HWVexKL7NxRyGFy4Lms7pQoGtTEHi4kKZic2w5UijLXw2bmx8yLoHewkBYe3ue2UJw1W3ODZrM4hqLPaDW_YWP5YPdJcZfIXHL6aaL4y5mt_tdG2b_KyfWqSHuXLTZf3KAEKRCBu31T2YAXQIzPhNApIZaNz5ixj3paxLNsS4wp99adMT1BPtsFgruR4l1I_jy9CQdqzVaxEnv6yj5YnFK9KgdFigEZBNbQLYugT5xxffODSHEK1for6-ztxTW0kDRbxhlnBa17N8FxooZ-7fzqZNeJzGt-bkv-ZdE-COlLXDRtrgq6i-my3otVVi59jSi9G5blbpkMHUVdjrg5F2Q";
    public static final String SIGNED_JWT_AUTH = "eyJraWQiOiJ0ZXN0LWtleSIsImFsZyI6IlJTMjU2IiwidHlwIjoiSldUIn0.eyJzdWIiOiJteVN1YiIsImlzcyI6Im15c2VsZiIsImF1ZCI6Imh0dHBzOi8vb3AuaWFtaWQuaW8iLCJpYXQiOjE1ODA3MjQ2ODcsIm5iZiI6MTU4MDcyNDY4NywiZXhwIjoxNTgwNzI0NzE3LCJqdGkiOiJteS1qdGkifQ.YJRlIJec-0_RmufUYsw4Q1w-8wG0bVsyB0RE53RP0FgbdvYFX1dxqgn6z6Q6B_R2UqeN3igG6YGnXZBTG34sOCFUwY07RDW9pf-9lYzutx7f3r5WEqkPKaeK2s5VPX6-cOisBvfI1XQzeTPlyemqHPBlopToiE6uI7eCLQ4eZAfyEiiFrVwI5wQLwN9BJUr012XmplL6egb5IQs3GTHOmdg7-BGXS7hrHadas_BAJgizu_j4aCa69vmj23CHLOC5HF8rBVCO0XnKu5HmdzQ5cpQJUNizrO91i5AEC5CIp0e6cHfA_dsO-IqzaqY3qdRMVcW0EYR_kKzId6lya6rAGg";
    public static final AssertionClaims ASSERTION_CLAIMS = new AssertionClaims();
    public static final Claims ID_CLAIMS = new Claims();

    public static final String SIGNED_ID_TOKEN_STRING = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Im9wX2tleV8xIn0.eyJzdWIiOiJhZjA2Y2VjMWRhZWNiMzgzYWQyZmNlMTI0YzdjMDBmZGJhNTM4ZjIzMTc5MjBmYTE1MjlhNGZhMDM0ZDc4ZDRlIiwiYXNzZXJ0aW9uX2NsYWltcyI6eyJiaXJ0aGRhdGUiOnsicmVzdWx0IjpmYWxzZX0sImVtYWlsIjp7InJlc3VsdCI6ZmFsc2V9fSwibm9uY2UiOiJub25jZS01YjA3ZTdhNi1mYjVlLTQxNjgtOTE3MS01ZDA2YjgzNTk4ODciLCJhdF9oYXNoIjoiaDZmY3VYWUtrRkRldGtndlNacklqQSIsImF1ZCI6IlRFU1QtMjc1NGVmYTc1ZThjNGQxMWE2ZDdmOTViOTBjZDhlNDAtVEVTVCIsImV4cCI6MTU4MDg5NDg2NiwiaWF0IjoxNTgwODk0MjY2LCJpc3MiOiJodHRwczovL29wLmlhbWlkLmlvIn0.fH99RbjaOLtV1iijU0kd-uS3sLvLHaVBoc7-4EoPQWiKHqwMf1-sFUsCrL3on9Pl0Mx93GMbv_Cb_UCkgoAxXGe03JMMl59zzEInTM-eFP0ZQhg6SYSoUAlTUVp5OBb7g0Ctfh4D6pm9xnl_Hn4ME5SP6vuQUXq_pnJd7HiGJaQ-MmKFXPPwe578bx_JF5atLV2wlDEqMr57XmZ0fa3r7gX2zCBAVjJ1eeEqsJTstU90ULg4gx3g_NRwpJtyUn14uJCvY4DRRbBZEt7cMkmraQFhhS1c3XXTyvdIBEpHB6I8KoZ6tt1IvqYYeMJvVAZoHHcDzIh_eiJNU3Vzsa7Oww";
    public static final JwsCompactConsumer SIGNED_ID_TOKEN = new JwsCompactConsumer(SIGNED_ID_TOKEN_STRING);
    public static final String SIGNED_ID_TOKEN_STRING2 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZjA2Y2VjMWRhZWNiMzgzYWQyZmNlMTI0YzdjMDBmZGJhNTM4ZjIzMTc5MjBmYTE1MjlhNGZhMDM0ZDc4ZDRlIiwibm9uY2UiOiJub25jZS01YjA3ZTdhNi1mYjVlLTQxNjgtOTE3MS01ZDA2YjgzNTk4ODciLCJhdF9oYXNoIjoiaDZmY3VYWUtrRkRldGtndlNacklqQSIsImF1ZCI6IlRFU1QtMjc1NGVmYTc1ZThjNGQxMWE2ZDdmOTViOTBjZDhlNDAtVEVTVCIsImV4cCI6MTU4NTc2MDkzOSwiaWF0IjoxNTgwODk0MjY2LCJpc3MiOiJodHRwczovL29wLmlhbWlkLmlvIiwiYXNzZXJ0aW9uX2NsYWltcyI6eyJiaXJ0aGRhdGUiOnsicmVzdWx0IjpmYWxzZX0sImVtYWlsIjp7InJlc3VsdCI6ZmFsc2V9LCJhZGRyZXNzIjp7InJlc3VsdCI6dHJ1ZX19LCJlbWFpbCI6ImphbmUuZG9lQG9wLmlhbWlkLmlvIiwiZ2l2ZW5fbmFtZSI6IkphbmUiLCJmYW1pbHlfbmFtZSI6IkRvZSIsInBob25lX251bWJlciI6Iis0NDExMTExMTExMTEiLCJiaXJ0aGRhdGUiOiIxOTkwLTAxLTMwIiwiYWdlIjoyNSwidG90YWxfYmFsYW5jZSI6eyJhbW91bnQiOjEwMDAsImN1cnJlbmN5IjoiR0JQIn0sImFkZHJlc3MiOnsicG9zdGFsX2NvZGUiOiJNSzEgMUFBIiwibG9jYWxpdHkiOiJNaWx0b24gS2V5bmVzIiwicmVnaW9uIjoiQnVja3MiLCJjb3VudHJ5IjoiVW5pdGVkIEtpbmdkb20iLCJzdHJlZXRfYWRkcmVzcyI6IjEgQmFnIEVuZCJ9LCJqdGkiOiJjNjM4YjFhMC01OGE0LTQ2MjAtYjJmOS05ZWMwYzkxM2VmZjkifQ.FCasmbESmX7Oyj6NuhJDt3Ht9P6fWDdMh4eV-CoFeh4";
    public static final JwsCompactConsumer SIGNED_ID_TOKEN2 = new JwsCompactConsumer(SIGNED_ID_TOKEN_STRING2);
    public static final String SIGNED_ID_TOKEN_STRING_ALL_SHARING = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZjA2Y2VjMWRhZWNiMzgzYWQyZmNlMTI0YzdjMDBmZGJhNTM4ZjIzMTc5MjBmYTE1MjlhNGZhMDM0ZDc4ZDRlIiwibm9uY2UiOiJub25jZS01YjA3ZTdhNi1mYjVlLTQxNjgtOTE3MS01ZDA2YjgzNTk4ODciLCJhdF9oYXNoIjoiaDZmY3VYWUtrRkRldGtndlNacklqQSIsImF1ZCI6IlRFU1QtMjc1NGVmYTc1ZThjNGQxMWE2ZDdmOTViOTBjZDhlNDAtVEVTVCIsImV4cCI6MTU4NTc2MTEwNiwiaWF0IjoxNTgwODk0MjY2LCJpc3MiOiJodHRwczovL29wLmlhbWlkLmlvIiwiZW1haWwiOiJqYW5lLmRvZUBvcC5pYW1pZC5pbyIsImdpdmVuX25hbWUiOiJKYW5lIiwiZmFtaWx5X25hbWUiOiJEb2UiLCJwaG9uZV9udW1iZXIiOiIrNDQxMTExMTExMTExIiwiYmlydGhkYXRlIjoiMTk5MC0wMS0zMCIsImFnZSI6MjUsInRvdGFsX2JhbGFuY2UiOnsiYW1vdW50IjoxMDAwLCJjdXJyZW5jeSI6IkdCUCJ9LCJnZW5kZXIiOiJmZW1hbGUiLCJjb3VudHJ5X29mX2JpcnRoIjoiVUsiLCJ0aXRsZSI6IkRyIiwibmF0aW9uYWxpdHkiOiJHQiIsImNpdmlsX3N0YXR1cyI6Im1hcnJpZWQiLCJjb21wYW55X3JlZ2lzdGVyZWRfbmFtZSI6IkFjbWUgQ28iLCJjb21wYW55X3RyYWRlX25hbWUiOiJBY21lIiwiY29tcGFueV9zdGFydF9kYXRlIjoiMTk4MS0xMC0wMiIsImNvbXBhbnlfZW5kX2RhdGUiOiIyMDE5LTAxLTA1IiwiY29tcGFueV90eXBlIjoiU29sZSBUcmFkZXIiLCJjb21wYW55X2NvdW50cnlfaW5jb3Jwb3JhdGlvbiI6IlVLIiwiY29tcGFueV9hZ2UiOjMsImNvbXBhbnlfb3BlcmF0aW5nIjp0cnVlLCJsYXN0X3llYXJfbW9uZXlfaW4iOnsiYW1vdW50IjoxMDAwMCwiY3VycmVuY3kiOiJFVVIifSwibGFzdF9xdWFydGVyX21vbmV5X2luIjp7ImFtb3VudCI6MzAwMCwiY3VycmVuY3kiOiJVU0QifSwiYXZlcmFnZV9tb250aGx5X21vbmV5X2luIjp7ImFtb3VudCI6OTk5LCJjdXJyZW5jeSI6IkNBRCJ9LCJwYXNzcG9ydF9pZCI6IioqKioqMTIxMiIsImRyaXZpbmdfbGljZW5zZV9pZCI6IkpPTkVTKioqKioqKioqKioqMDYiLCJuYXRpb25hbF9jYXJkX2lkIjoiKioqKioxMTFBIiwiYWRkcmVzcyI6eyJwb3N0YWxfY29kZSI6Ik1LMSAxQUEiLCJsb2NhbGl0eSI6Ik1pbHRvbiBLZXluZXMiLCJyZWdpb24iOiJCdWNrcyIsImNvdW50cnkiOiJVbml0ZWQgS2luZ2RvbSIsInN0cmVldF9hZGRyZXNzIjoiMSwgQmFnIEVuZCJ9LCJqdGkiOiJkOGM5OGI4ZC0wZjljLTQ4MmItYjQwYi03ODA4ODE1MWYyODEifQ.FEnfxerSYhDpWVxw5IrTcKm-NIRHrgjrpN2LUR0rP14";
    public static final JwsCompactConsumer SIGNED_ID_ALL_SHARING = new JwsCompactConsumer(SIGNED_ID_TOKEN_STRING_ALL_SHARING);
    public static final AssertionClaimResponse TRUE_ASSERTION = new AssertionClaimResponse(true);
    public static final AssertionClaimResponse FALSE_ASSERTION = new AssertionClaimResponse(false);
    public static Date THIRTIETH_OF_JANUARY_1990;
    public static Date SECOND_OF_OCTOBER_1981;
    public static Date FIFTH_OF_JANUARY_2019;
    public static IdToken ID_TOKEN_WITH_NONCE = IdToken.builder()
            .nonce(NONCE)
            .build();

    static {
        try {
            THIRTIETH_OF_JANUARY_1990 = new StdDateFormat().parse("1990-01-30");
            SECOND_OF_OCTOBER_1981 = new StdDateFormat().parse("1981-10-02");
            FIFTH_OF_JANUARY_2019 = new StdDateFormat().parse("2019-01-05");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ID_CLAIMS.givenName().withEssential(true);
        ASSERTION_CLAIMS.givenName().eq("John").withPurpose("This is why RP is verifying your name");
        ASSERTION_CLAIMS.birthdate().eq(Date.from(Instant.EPOCH)).withPurpose("This is why RP is verifying your DOB");
        ASSERTION_CLAIMS.familyName().eq("Doe").withPurpose("This is why RP is verifying your surname");
        ASSERTION_CLAIMS.address()
                .withAssertion(Address.country().eq("UK"))
                .withAssertion(Address.postalCode().eq("MK11AA"))
                .withPurpose("This is why RP is verifying your address");
        ASSERTION_CLAIMS.totalBalance()
                .withAssertion(Balance.amount().gt(BigDecimal.TEN));

    }
}
