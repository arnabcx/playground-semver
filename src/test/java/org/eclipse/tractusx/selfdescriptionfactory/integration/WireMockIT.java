package org.eclipse.tractusx.selfdescriptionfactory.integration;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.eclipse.tractusx.selfdescriptionfactory.service.clearinghouse.ClearingHouseClient;
import org.eclipse.tractusx.selfdescriptionfactory.service.keycloak.KeycloakManager;
import org.eclipse.tractusx.selfdescriptionfactory.service.wallet.CustodianClient;
import org.json.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("gaia-x-ctx")
class WireMockIT {
    private WireMockServer wm;
    @Autowired
    CustodianClient custodianClient;

    @Autowired
    ClearingHouseClient clearingHouseClient;

    @Value("8080")
    int port;

    @MockBean
    KeycloakManager keycloakManager;

    @BeforeAll
    void setup() {
        wm = new WireMockServer(new WireMockConfiguration().port(port));
        wm.start();
    }

    @AfterEach
    void resetAll(){
        wm.resetAll();
    }

    @AfterAll
    void tearDown() {
        wm.stop();
    }

    @Test
    void validReqWallet() throws JSONException{
        Map<String, Object> res = IntegrationUtil.mockedWalletRes;
        wm.stubFor(get(urlEqualTo("/api/wallets/BPNL000000000001")).willReturn(aResponse().withBody(String.valueOf(res)).withStatus(HttpStatus.OK.value())));
        JSONObject obj = new JSONObject(res);
        String val = (String) obj.get("did");
        Assertions.assertNotNull(val);
    }

    @Test
    void invalidReqWallet() throws JSONException {
        wm.stubFor(get(urlEqualTo("/api/wallets/1")).willReturn(aResponse().withBody("{\n" +
                "  \"status\":" +
                "  \"400\",\n" +
                "  \"error\": \"BAD_REQUEST\"\n" +
                "}").withStatus(HttpStatus.OK.value())));
        Map<String, Object> res = IntegrationUtil.mockedWalletRes;
        JSONObject obj = new JSONObject(res);
        String val = (String) obj.get("error");
        Assertions.assertEquals("BAD_REQUEST", val);
    }

    @Test
    void validReqClearingHouse() throws IOException {
        String vc = Files.readString(Path.of("src/test/resources/testdata.json"));
        VerifiableCredential verifiableCredential=  VerifiableCredential.fromJson(vc);
        wm.stubFor(post(urlPathEqualTo("/api")).withRequestBody(equalToJson(vc)).withQueryParam("externalId", equalTo("ID01234-123-4321")).willReturn(aResponse().withStatus(200)));
        clearingHouseClient.send(verifiableCredential,"ID01234-123-4321");
        wm.verify(1, postRequestedFor(urlPathEqualTo("/api")));
    }

    @Test
    void invalidReqCH() {
        VerifiableCredential vc = new VerifiableCredential();
        wm.stubFor(post(urlPathEqualTo("/api")).withRequestBody(equalToJson("{\n" +
                "\n" +
                "}")).withQueryParam("externalId", equalTo("")).willReturn(aResponse().withStatus(400)));
        Assertions.assertThrows(ResponseStatusException.class, ()->clearingHouseClient.send(vc,""));
    }

    @Test
    void serverErrorCH() throws IOException {
        String vc = Files.readString(Path.of("src/test/resources/testdata.json"));
        VerifiableCredential verifiableCredential=  VerifiableCredential.fromJson(vc);
        wm.stubFor(post(urlPathEqualTo("/api")).withRequestBody(equalToJson(vc)).withQueryParam("externalId", equalTo("ID01234-123-4321")).willReturn(aResponse().withStatus(500)));
        Assertions.assertThrows(ResponseStatusException.class, ()->clearingHouseClient.send(verifiableCredential,"ID01234-123-4321"));
    }

}
