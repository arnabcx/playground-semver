package org.eclipse.tractusx.selfdescriptionfactory.service.vrel3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.selfdescriptionfactory.config.TechnicalUsersDetails;
import org.eclipse.tractusx.selfdescriptionfactory.model.vrel3.*;
import org.eclipse.tractusx.selfdescriptionfactory.model.vrel3.LegalParticipantSchema;
import org.eclipse.tractusx.selfdescriptionfactory.service.Claims;
import org.eclipse.tractusx.selfdescriptionfactory.service.keycloak.KeycloakClient;
import org.eclipse.tractusx.selfdescriptionfactory.service.wallet.CustodianWallet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;



@ActiveProfiles("gaia-x-ctx")
public class SDocumentConverterGaiaXTest {

    private static String sdRequestLegalParticipant;
    private static String sdRequestServiceOffering;

    private TechnicalUsersDetails technicalUsersDetails = Mockito.mock(TechnicalUsersDetails.class);
    private KeycloakClient keycloakClient = Mockito.mock(KeycloakClient.class);

    private CustodianWallet custodianWallet = Mockito.mock(CustodianWallet.class);

    private static final String KEY_CLOAK_USER = "custodianWallet";

    private ObjectMapper mapper;

    @BeforeAll
    public static void setup(){
        sdRequestLegalParticipant = "{\n" +
                "  \"externalId\": \"ID01234-123-4321\",\n" +
                "  \"type\": \"LegalParticipant\",\n" +
                "  \"holder\": \"BPNL000000000000\",\n" +
                "  \"issuer\": \"CAXSDUMMYCATENAZZ\",\n" +
                "  \"registrationNumber\": [\n" +
                "    {\n" +
                "      \"type\": \"taxID\",\n" +
                "      \"value\": \"o12345678\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"headquarterAddress.country\": \"DE\",\n" +
                "  \"legalAddress.country\": \"DE\",\n" +
                "  \"bpn\": \"BPNL000000000000\"\n" +
                "}";

        sdRequestServiceOffering = "{\n" +
                "  \"externalId\": \"ID01234-123-4321\",\n" +
                "  \"type\": \"ServiceOffering\",\n" +
                "  \"holder\": \"BPNL000000000000\",\n" +
                "  \"issuer\": \"CAXSDUMMYCATENAZZ\",\n" +
                "  \"providedBy\": \"http://test\",\n" +
                "  \"registrationNumber\": [\n" +
                "    {\n" +
                "      \"type\": \"local\",\n" +
                "      \"value\": \"o12345678\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"headquarterAddress.country\": \"DE\",\n" +
                "  \"legalAddress.country\": \"DE\",\n" +
                "  \"bpn\": \"BPNL000000000000\"\n" +
                "}";
    }

    @Test
    public void convertTestforLegalParticipant() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        SDocumentConverterGaiaX sDocumentConverterGaiaX = new SDocumentConverterGaiaX(custodianWallet);
        Map<String, TechnicalUsersDetails.UserDetail> usersDetails = new HashMap<>() {{
            put(KEY_CLOAK_USER, new TechnicalUsersDetails.UserDetail(
                    "serverUrl",
                    "realm",
                    null,
                    "password",
                    "clientId",
                    "clientSecret"));
        }};
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        setFieldsByReflection(sDocumentConverterGaiaX, "gaiaxParticipantSchema", "dummy");
        setFieldsByReflection(sDocumentConverterGaiaX, "gaiaxServiceOfferingSchema", "dummy");
        setFieldsByReflection(sDocumentConverterGaiaX, "cofinitySchema", "dummy");



        Map<String,Object> tokens = new HashMap<>();
        tokens.put("access_token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZXhwIjoxNzE2MjM5MDIyfQ.FuJB22Dq3zXcFXtIAc59tWnmbbFC6jRXzb_2ejbhhoQ");
        Mockito.when(technicalUsersDetails.getUsersDetails()).thenReturn(usersDetails);
        Map<String, Object> mockedWalletRes = Map.of("bpn", "BPNL000000000001",
                "did", "did:sov:AAAyVfpadka4iDqwCLrYL3",
                "name", "smartSense Consulting Solutions pvt ltd");
        Mockito.when(custodianWallet.getWalletData(anyString())).thenReturn(mockedWalletRes);
        Mockito.when(keycloakClient.getTokens(Mockito.any(), anyString(), anyString(), anyString())).thenReturn(tokens);
        Claims claims = sDocumentConverterGaiaX.convert(mapper.readValue(sdRequestLegalParticipant, LegalParticipantSchema.class));
        Assertions.assertNotNull(claims);
    }

    @Test
    public void convertTestforServiceOffering() throws JsonProcessingException, NoSuchFieldException, IllegalAccessException {
        SDocumentConverterGaiaX sDocumentConverterGaiaX = new SDocumentConverterGaiaX(custodianWallet);
        Map<String, TechnicalUsersDetails.UserDetail> usersDetails = new HashMap<>() {{
            put(KEY_CLOAK_USER, new TechnicalUsersDetails.UserDetail(
                    "serverUrl",
                    "realm",
                    null,
                    "password",
                    "clientId",
                    "clientSecret"));
        }};
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        setFieldsByReflection(sDocumentConverterGaiaX, "gaiaxParticipantSchema", "dummy");
        setFieldsByReflection(sDocumentConverterGaiaX, "gaiaxServiceOfferingSchema", "dummy");
        setFieldsByReflection(sDocumentConverterGaiaX, "cofinitySchema", "dummy");

        Map<String,Object> tokens = new HashMap<>();
        tokens.put("access_token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZXhwIjoxNzE2MjM5MDIyfQ.FuJB22Dq3zXcFXtIAc59tWnmbbFC6jRXzb_2ejbhhoQ");
        Mockito.when(technicalUsersDetails.getUsersDetails()).thenReturn(usersDetails);
        Map<String, Object> mockedWalletRes = Map.of("bpn", "BPNL000000000001",
                "did", "did:sov:AAAyVfpadka4iDqwCLrYL3",
                "name", "smartSense Consulting Solutions pvt ltd");
        Mockito.when(custodianWallet.getWalletData(anyString())).thenReturn(mockedWalletRes);
        Mockito.when(keycloakClient.getTokens(Mockito.any(), anyString(), anyString(), anyString())).thenReturn(tokens);
        Claims claims = sDocumentConverterGaiaX.convert(mapper.readValue(sdRequestServiceOffering, ServiceOfferingSchema.class));
        Assertions.assertNotNull(claims);
    }



    private static void setFieldsByReflection(SDocumentConverterGaiaX sDocumentConverterGaiaX, String field, String value) throws NoSuchFieldException, IllegalAccessException {
        Field gaiaxParticipantSchema = sDocumentConverterGaiaX.getClass().getDeclaredField(field);
        gaiaxParticipantSchema.setAccessible(true);
        gaiaxParticipantSchema.set(sDocumentConverterGaiaX, value);
    }


}
