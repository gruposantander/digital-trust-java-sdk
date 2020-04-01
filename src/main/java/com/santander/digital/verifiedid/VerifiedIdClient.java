package com.santander.digital.verifiedid;

import com.santander.digital.verifiedid.model.TokenRequest;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeRequest;
import com.santander.digital.verifiedid.model.init.authorize.InitiateAuthorizeResponse;
import com.santander.digital.verifiedid.model.token.IdToken;

public interface VerifiedIdClient {

    void setUpClient ();
    InitiateAuthorizeResponse initiateAuthorize (InitiateAuthorizeRequest request);
    IdToken token(TokenRequest request);
}
