package org.eclipse.tractusx.selfdescriptionfactory.utils;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.eclipse.tractusx.selfdescriptionfactory.config.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class XrayUtils {
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS).build();

    public String getToken() throws IOException {
        var requestBody = new FormBody.Builder().add("client_id", Configuration.getXrayClientId())
                .add("client_secret", Configuration.getXrayClientSecret())
                .build();
        var req = new Request.Builder()
                .url(Configuration.getXrayHost() + "/api/v2/authenticate")
                .post(requestBody)
                .build();
        try (var response = okHttpClient.newCall(req).execute()) {
            return response.body().string().replaceAll("\"", "");
        }
    }

    public void importReport(String json) throws IOException {
        var requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        var request = new Request.Builder().addHeader("Authorization", "Bearer " + getToken()).
                url(Configuration.getXrayHost() + "/api/v2/import/execution/cucumber").
                post(requestBody).build();
        try (var response = okHttpClient.newCall(request).execute()) {
            log.info("Response from xray is  " + response.body().string());
        } catch (Exception ex) {
            log.error(" Error from xray api", ex);
        } finally {
            okHttpClient.dispatcher().executorService().shutdown();
            okHttpClient.connectionPool().evictAll();
        }
    }
}