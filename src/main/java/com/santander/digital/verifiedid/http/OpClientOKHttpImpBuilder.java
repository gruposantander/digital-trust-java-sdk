package com.santander.digital.verifiedid.http;

import okhttp3.OkHttpClient;

public class OpClientOKHttpImpBuilder {

    private OkHttpClient okHttpClient = new OkHttpClient();

    public OpClientOKHttpImpBuilder withOkHttpClient (final OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
        return this;
    }

    public OpClientOKHttpImp build () {
        return new OpClientOKHttpImp(this.okHttpClient);
    }
}
