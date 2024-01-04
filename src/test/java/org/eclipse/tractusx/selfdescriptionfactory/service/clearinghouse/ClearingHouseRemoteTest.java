package org.eclipse.tractusx.selfdescriptionfactory.service.clearinghouse;

import com.danubetech.verifiablecredentials.CredentialSubject;
import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import foundation.identity.jsonld.JsonLDUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.anyString;



class ClearingHouseRemoteTest {

    private WebClient webClient = Mockito.mock(WebClient.class);
    private VerifiableCredential verifiableCredential ;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec = Mockito.mock(WebClient.RequestBodyUriSpec.class);
    private WebClient.RequestBodySpec requestBodySpec = Mockito.mock(WebClient.RequestBodySpec.class);
    private WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);

    private WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);

    private Object externalId;

    @BeforeEach
    public void setUp() throws JsonProcessingException {

        String sdRequest = "{\n" +
                "  \"externalId\": \"ID01234-123-4321\",\n" +
                "  \"type\": \"LegalPerson\",\n" +
                "  \"holder\": \"BPNL000000000000\",\n" +
                "  \"issuer\": \"CAXSDUMMYCATENAZZ\",\n" +
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

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        var claims = mapper.readValue(sdRequest, new TypeReference<Map<String, Object>>() {});

        var holder = claims.remove("holder");
        var issuer = claims.remove("issuer");
        var type = claims.get("type");
        externalId = claims.remove("externalId");

        var credentialSubject = CredentialSubject.fromJsonObject(claims);
        verifiableCredential = VerifiableCredential.builder()
                .context(URI.create("https://localhost/vocabulary"))
                .issuanceDate(new Date())
                .expirationDate(Date.from(Instant.now().plus(Duration.ofDays(90))))
                .credentialSubject(credentialSubject)
                .build();
        JsonLDUtils.jsonLdAdd(verifiableCredential, "issuerIdentifier", issuer);
        JsonLDUtils.jsonLdAdd(verifiableCredential, "holderIdentifier", holder);
        JsonLDUtils.jsonLdAdd(verifiableCredential, "type", type);


    }
    @Test
    public void doWorkTest() {
        ClearingHouseRemote chr = new ClearingHouseRemote(Mockito.mock(ClearingHouseClient.class), Mockito.mock(ObjectMapper.class));
        try (MockedStatic<WebClient> webClientStatic = Mockito.mockStatic(WebClient.class)) {
            webClientStatic.when(() -> WebClient.create(anyString())).thenReturn(webClient);
            Mockito.when(webClient.post()).thenReturn(requestBodyUriSpec);
            Mockito.when(requestBodyUriSpec.uri(Mockito.any(Function.class))).thenReturn(requestBodySpec);
            Mockito.when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
            Mockito.when(requestBodySpec.headers(Mockito.any(Consumer.class))).thenReturn(requestBodySpec);
            Mockito.when(requestBodySpec.bodyValue(Mockito.any())).thenReturn(requestHeadersSpec);
            Mockito.when(requestHeadersSpec.accept(MediaType.ALL)).thenReturn(requestBodySpec);
            Mockito.when(requestBodySpec.retrieve()).thenReturn(responseSpec);
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
            Mockito.when(responseSpec.toBodilessEntity())
                    .thenReturn((Mono.just(responseEntity)));
            chr.doWork("test", verifiableCredential, externalId.toString(), "Bearer.shdgsajdjdh");
            webClientStatic.when(() -> WebClient.create(anyString())).thenThrow(new RuntimeException());
            try{
                chr.doWork("test", verifiableCredential, externalId.toString(), "Bearer.shdgsajdjdh");
            }catch (Exception e){
                System.out.println("Error Expected");
            }
        }
    }
}