### Java SDK for Santander Digital Trust

This is a plain Java 8 library, it doesn't rely on any specific framework. <br>
As such, it shouldn't have any compatibility issues with other frameworks

#### How to use it

Import the dependency
```
<groupId>com.santander.digital</groupId>
    <artifactId>verifiedid-client</artifactId>
    <version>0.3.0-SNAPSHOT</version>
```

The first step is to create the client and set it up:

```java
final verifiedIdClient = new VerifiedIdClientImpBuilder()
        .withWellKnownURI("https://op-iamid-verifiedid-pro.e4ff.pro-eu-west-1.openshiftapps.com/.well-known/openid-configuration")
        .withPrivateJWKFromFile("./secrets/privateKey.json")
        .withClientId("12345678-aaaa-bbbb-cccc-1234567890ab")
        .build(); // creating the client

verifiedIdClient.setUpClient(); // running all the setup steps (querying the well-known endpoint and storing the public jwks)
```

After the step above, it is possible to initiate the flow straight away, by preparing the request and then send it:

```java
final Claims idClaims = new Claims();
idClaims.givenName().withEssential(true);

final AssertionClaims assertionClaims = new AssertionClaims();
assertionClaims.givenName().eq("John").withPurpose("This is why RP is verifying your name");
assertionClaims.birthdate().eq(Date.from(Instant.EPOCH)).withPurpose("This is why RP is verifying your DOB");
assertionClaims.familyName().eq("Doe").withPurpose("This is why RP is verifying your surname");
assertionClaims.address()
        .withAssertion(Address.country().eq("UK"))
        .withAssertion(Address.postalCode().eq("MK11AA"))
        .withPurpose("This is why RP is verifying your address");
assertionClaims.totalBalance()
        .withAssertion(TotalBalance.amount().gt(BigDecimal.TEN));

InitiateAuthorizeRequest request = InitiateAuthorizeRequest.builder()
                .redirectUri("com.myApp://callback")
                .claims(idClaims)
                .assertionClaims(assertionClaims)
                .build();
```

Both ```Claims``` and ```AssertionClaims```, as well as ```InitiateAuthorizeRequest``` follow a builder pattern, to make it easier to prepare the request

After the claims have been set up, the _/initiate-authorize_ can be invoked:

```java
InitiateAuthorizeResponse initiateAuthorizeResponse = verifiedIdClient.initiateAuthorize(request);
```

The above ```InitiateAuthorizeResponse``` object has the following structure:

```java
@Data
public class InitiateAuthorizeResponse {
    private final String redirectionUri;
    private final String nonce;
    private final String codeVerifier;
    private final String requestObjectUri;
    private final Long expiration;
}
```

The _redirectionUri_ can be used to redirect users to the consent journey:
```
https://op-iamid-verifiedid-pro.e4ff.pro-eu-west-1.openshiftapps.com/web/login?request_uri=urn:op.iamid.io:JDAQ9YwMSLcCbiUz0Wq0HGjpu-wr4HngFDCv8tTkQa-
```
The consent process (happening in a browser, out of scope for this SDK) finishes with an authorization code:
```
302 https://rp.com/callback?code=Ian06qtqg5PNUhfRUy9UFLwx4T7DIzodBLiFjoFYWmr
```
This code can then be used to invoke _/token_:

```java
TokenRequest tokenRequest = TokenRequest.builder()
                .redirectUri("https://rp.example.com/callback")
                .authorizationCode("Ian06qtqg5PNUhfRUy9UFLwx4T7DIzodBLiFjoFYWmr")
                .build();

        IdToken token = verifiedIdClient.token(tokenRequest);
```

## Supported Claims - Sharing
The following claims can be requested for sharing:
```java
final Claims sharingClaims = new Claims();

sharingClaims.givenName();
sharingClaims.familyName();
sharingClaims.birthdate();
sharingClaims.gender();
sharingClaims.countryOfBirth();
sharingClaims.title();
sharingClaims.nationality();
sharingClaims.civilStatus();
sharingClaims.age();
sharingClaims.companyRegisteredName();
sharingClaims.companyTradeName();
sharingClaims.companyStartDate();
sharingClaims.companyEndDate();
sharingClaims.companyType();
sharingClaims.companyCountryIncorporation();
sharingClaims.companyAge();
sharingClaims.companyOperating();
sharingClaims.phoneNumber();
sharingClaims.email();
sharingClaims.address();
sharingClaims.totalBalance();
sharingClaims.lastYearMoneyIn();
sharingClaims.lastQuarterMoneyIn();
sharingClaims.averageMonthlyMoneyIn();
sharingClaims.passportId();
sharingClaims.drivingLicenseId();
sharingClaims.nationalCardId();
```

For each of the claim, a specific value _purpose_, _ial_ and/or _essential_ can be specified:
```java
sharingClaims.givenName().withPurpose("given name purpose").withIAL(THREE).withEssential(true);
sharingClaims.familyName().withIAL(TWO).withPurpose("family name purpose");
sharingClaims.birthdate().withEssential(false);
sharingClaims.gender();
```
None of these attributes is mandatory and they can be applied in any order

## Supported Claims - Verifying

The SDK support the same verifying claims as it does for sharing, though the data model is quite different

There are three types of assertions in this SDK: _simple_, _comparative_ and _complex_.
 * *Simple assertions*:  they support only the ``.eq`` (equal assertion), e.g.
    ```java
    assertionClaims.email().eq("jane.doe@santander.co.uk");
    ```
   which translates into ``Verify that the email is "jane.doe@santander.co.uk"``
 * *Comparative assertions*: they extend _simple_ assertions with the ``.gt``, ``.gte``, ``.lt`` and ``.lte`` operations, e.g.:
   ```java
    assertionClaims.age().gte(18);
    ``` 
   which translates into ``Verify that the age is greater or equal than 18``
 * *Complex claims*: they include different properties, that can be individually asserted, for example:
    ```java
    assertionClaims.totalBalance()
        .withAssertion(Balance.currency().equal(Currency.getInstance("GBP")))
        .withAssertion(Balance.amount().gt(BigDecimal.valueOf(99.99)));
    ```
   which translates into ``Verify that the balance currency is equal to GBP and the amount is greater than 99.99``. 
   Depending on the individual claim, the property can either support only ``.eq`` or the entire set of comparisons 

Similarly to sharing claims, _purpose_, _ial_ and/or _essential_ can be attached to verifying claims:
```java
assertionClaims.address()
    .withAssertion(Address.country().eq("UK"))
    .withAssertion(Address.postalCode().eq("MK11AA"))
    .withPurpose("This is why RP is verifying your address");
```

To keep the API simple and concise a ``Balance`` and ``Address`` helpers have been created with the following methods:
```java
Balance.currency()
Balance.amount()
Address.formatted() 
Address.streetAddress()
Address.postalCode()
Address.locality()
Address.region()
Address.country()
```

## PKCE and nonce support
This SDK supports nonce verification and PKCE. Depending on the scenario one or both of them will be automatically enforced.
* Nonce: nonces can be passed as part of the ``InitiateAuthorizeRequest`` object:
   ```java
    InitiateAuthorizeRequest request = InitiateAuthorizeRequest.builder()
                .redirectUri("https://example.com/callback")
                .claims(idClaims)
                .assertionClaims(assertionClaims)
                .nonce("nonce-1111111")
                .build();
    ```
    If such value is not passed, the SDK will create one and it will return it as part of the response object of the ``initiateAuthorize`` method.
    Whether automatically generated or not, such value will need to be passed when invoking the ``token`` method.
 * PKCE: similarly to nonce, a code challenge can be passed:
   ```java
   InitiateAuthorizeRequest request = InitiateAuthorizeRequest.builder()
       .redirectUri("https://example.com/callback")
       .claims(idClaims)
       .assertionClaims(assertionClaims)
       .codeChallenge("ii2ebegd173dg")
       .build();
   ```
   Differently than the nonce case, a code verifier will be automatically generated (and the challenge calculated) if not passed *and* the redirectURI is a _deep link_. 
   If a challenge is passed or a verifier is generated, the verifier needs to be passed when invoking the ``token`` method:
   ```java
    TokenRequest tokenRequest = TokenRequest.builder()
        .redirectUri("com.myApp://callback")
        .authorizationCode("27IyhbWvL5uGY61f69A-RlEl7N2qRLm5vQ7_mO0tRGH")
        .codeVerifier("H-jwul2I2vbDb90ll-lfl14LXtES9lqZvgtiX3WYF44")
        .build();
    ```



