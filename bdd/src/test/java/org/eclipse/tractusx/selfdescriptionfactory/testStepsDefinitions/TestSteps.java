package org.eclipse.tractusx.selfdescriptionfactory.testStepsDefinitions;

import org.eclipse.tractusx.selfdescriptionfactory.config.Configuration;
import org.eclipse.tractusx.selfdescriptionfactory.contants.TestConstants;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.IOException;
import java.util.Map;

import static org.eclipse.tractusx.selfdescriptionfactory.contants.TestConstants.*;

public class TestSteps {

    private String token;

    Map<String, String> env;

    @Given("User is authorized")
    public void i_am_authorized_user() throws IOException, JSONException {
        var client = new OkHttpClient();
        String cl = Configuration.getEnvCreateVcClientSecret();
        var requestBody = new FormBody.Builder().add("client_id", Configuration.getEnvCreateVcClient())
                .add("client_secret", Configuration.getEnvCreateVcClientSecret())
                .add("grant_type", "client_credentials").add("scope", "openid").build();
        var req = new Request.Builder()
                .url(Configuration.getIdpHost() + TOKEN_URI)
                .post(requestBody)
                .build();
        try (var response = client.newCall(req).execute()) {
            JSONObject json = new JSONObject(response.body().string());
            token = json.get("access_token").toString();
            Assert.assertNotNull(token);
        }
    }
    @When("User creates a Verifiable credential")
    public void create_vc() throws IOException, JSONException {
        var client = new OkHttpClient();

        var  requestBody = RequestBody.create(TestConstants.VALID_VC_REQUEST, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Configuration.getSdfHost() + CREATE_VC_URI)
                .addHeader(AUTHORIZATION , Bearer + token)
                .post(requestBody)
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(),202);
        }
    }

    @Then("User gets 202 success code")
    public void success_response_for_create_vc_request() throws IOException, JSONException {
        var client = new OkHttpClient();

        var  requestBody = RequestBody.create(TestConstants.VALID_VC_REQUEST, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Configuration.getSdfHost() + CREATE_VC_URI)
                .addHeader(AUTHORIZATION , "Bearer " + token)
                .post(requestBody)
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(),202);
        }
    }

    @When("User creates a Verifiable credential with invalid request body")
    public void create_vc_with_invalid_req() throws IOException, JSONException {
        var client = new OkHttpClient();

        var  requestBody = RequestBody.create(TestConstants.INVALID_VC_REQUEST, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Configuration.getSdfHost() + CREATE_VC_URI)
                .addHeader(AUTHORIZATION , Bearer + token)
                .post(requestBody)
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(),400);
        }
    }

    @Then("User gets 400 error code")
    public void bad_request_response_for_create_vc_request() throws IOException, JSONException {
        var client = new OkHttpClient();

        var  requestBody = RequestBody.create(TestConstants.INVALID_VC_REQUEST, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Configuration.getSdfHost() + CREATE_VC_URI)
                .addHeader(AUTHORIZATION , Bearer + token)
                .post(requestBody)
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(),400);
        }
    }

    @When("User creates a Verifiable credential with invalid wallet id")
    public void create_vc_with_invalid_wallet_id() throws IOException, JSONException {
        var client = new OkHttpClient();

        var  requestBody = RequestBody.create(TestConstants.VC_REQUEST_WITH_INVALID_WALLET, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Configuration.getSdfHost() + CREATE_VC_URI)
                .addHeader(AUTHORIZATION , Bearer + token)
                .post(requestBody)
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(),404);
        }
    }

    @Then("User gets 404 error code")
    public void not_found_response_for_create_vc_request() throws IOException, JSONException {
        var client = new OkHttpClient();

        var  requestBody = RequestBody.create(TestConstants.VC_REQUEST_WITH_INVALID_WALLET, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Configuration.getSdfHost() + CREATE_VC_URI)
                .addHeader(AUTHORIZATION , Bearer + token)
                .post(requestBody)
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(),404);
        }
    }

    @When("User creates a Verifiable credential with invalid JWT token")
    public void create_vc_with_invalid_jwt_token() throws IOException, JSONException {
        var client = new OkHttpClient();

        var  requestBody = RequestBody.create(TestConstants.VC_REQUEST_WITH_INVALID_WALLET, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Configuration.getSdfHost() + CREATE_VC_URI)
                .addHeader(AUTHORIZATION ,token)
                .post(requestBody)
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(),401);
        }
    }

    @Then("User gets 401 error code")
    public void unauthorized_response_for_create_vc_request() throws IOException, JSONException {
        var client = new OkHttpClient();

        var  requestBody = RequestBody.create(TestConstants.VC_REQUEST_WITH_INVALID_WALLET, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Configuration.getSdfHost() + CREATE_VC_URI)
                .addHeader(AUTHORIZATION , token)
                .post(requestBody)
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(),401);
        }
    }

    @Given("User is authorized but not having access to create VC")
    public void i_am_authorized_user_but_not_having_required_access() throws IOException, JSONException {
        var client = new OkHttpClient();
        var requestBody = new FormBody.Builder().add("client_id", Configuration.getEnvDtrClientId())
                .add("client_secret", Configuration.getEnvDtrClientSecret())
                .add("grant_type", "client_credentials").add("scope", "openid").build();
        var req = new Request.Builder()
                .url(Configuration.getIdpHost() + TOKEN_URI)
                .post(requestBody)
                .build();
        try (var response = client.newCall(req).execute()) {
            JSONObject json = new JSONObject(response.body().string());
            token = json.get("access_token").toString();
            Assert.assertNotNull(token);
        }
    }

    @When("User not having access creates a Verifiable credential")
    public void create_vc_while_not_having_access() throws IOException, JSONException {
        var client = new OkHttpClient();

        var  requestBody = RequestBody.create(TestConstants.VALID_VC_REQUEST, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Configuration.getSdfHost() + CREATE_VC_URI)
                .addHeader(AUTHORIZATION , Bearer + token)
                .post(requestBody)
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(),403);
        }
    }

    @Then("User gets 403 error code")
    public void forbidden_response_for_create_vc_request() throws IOException, JSONException {
        var client = new OkHttpClient();

        var  requestBody = RequestBody.create(TestConstants.VC_REQUEST_WITH_INVALID_WALLET, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(Configuration.getSdfHost() + CREATE_VC_URI)
                .addHeader(AUTHORIZATION , Bearer + token)
                .post(requestBody)
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(),403);
        }
    }
}
