package org.eclipse.tractusx.selfdescriptionfactory.service;

import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.apicatalog.jsonld.loader.DocumentLoaderOptions;
import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.selfdescriptionfactory.model.vrel3.SelfdescriptionPostRequest;
import org.eclipse.tractusx.selfdescriptionfactory.service.clearinghouse.ClearingHouse;
import org.eclipse.tractusx.selfdescriptionfactory.service.wallet.CustodianWallet;
import org.junit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.core.convert.ConversionService;

import java.net.URI;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;


public class SDFactoryTest {

    private CustodianWallet custodianWallet = Mockito.mock(CustodianWallet.class);

    private String sdRequest;

    private ClearingHouse clearingHouse = Mockito.mock(ClearingHouse.class);

    private ConversionService conversionService = Mockito.mock(ConversionService.class);

    private VerifiableCredential vfc;

    @BeforeEach
    public void setup(){
        sdRequest = "{\n" +
                "  \"externalId\": \"ID01234-123-4321\",\n" +
                "  \"type\": \"LegalParticipant\",\n" +
                "  \"holder\": \"BPNL000000000000\",\n" +
                "  \"issuer\": \"CAXSDUMMYCATENAZZ\",\n" +
                "  \"registrationNumber\": [\n" +
                "    {\n" +
                "      \"type\": \"EORI\",\n" +
                "      \"value\": \"o12345678\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"headquarterAddress.country\": \"DE\",\n" +
                "  \"legalAddress.country\": \"DE\",\n" +
                "  \"bpn\": \"BPNL000000000000\"\n" +
                "}";

        vfc = new VerifiableCredential();
        vfc.setDocumentLoader(new DocumentLoader() {
            @Override
            public Document loadDocument(URI uri, DocumentLoaderOptions documentLoaderOptions) throws JsonLdError {
                return null;
            }
        });

    }

    @Test
    public void createVcTestCatenaX() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Claims pClaim  = Mockito.mock(Claims.class);
        var claims = mapper.readValue(sdRequest, new TypeReference<Map<String, Object>>() {});
        Mockito.when(pClaim.claims()).thenReturn(claims);
        Mockito.when(conversionService.convert(any(),any(Class.class))).thenReturn(pClaim);
        var vc  = new VerifiableCredential();
        Mockito.when(custodianWallet.getSignedVC(any())).thenReturn(vc);
        Mockito.doNothing().when(clearingHouse).sendToClearingHouse(any(), any());
        SDFactory sdFactory = new SDFactoryCatenaX(custodianWallet, conversionService, clearingHouse);
        sdFactory.createVC(mapper.readValue(sdRequest, SelfdescriptionPostRequest.class));
        Mockito.verify(clearingHouse,Mockito.times(1)).sendToClearingHouse(any(), any());
        Mockito.verify(custodianWallet,Mockito.times(1)).getSignedVC(any());
        Mockito.verify(conversionService,Mockito.times(1)).convert(any(), any());
    }

    @Test
    public void createVcTestGaiaX() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Claims pClaim  = Mockito.mock(Claims.class);
        var claims = mapper.readValue(sdRequest, new TypeReference<Map<String, Object>>() {});
        Mockito.when(pClaim.claims()).thenReturn(claims);
        Mockito.when(conversionService.convert(any(),any(Class.class))).thenReturn(pClaim);
        var vc  = new VerifiableCredential();
        Mockito.when(custodianWallet.getSignedVC(any())).thenReturn(vc);
        Mockito.doNothing().when(clearingHouse).sendToClearingHouse(any(), any());
        SDFactory sdFactory = new SDFactoryGaiaX(conversionService, clearingHouse);
        sdFactory.createVC(mapper.readValue(sdRequest, SelfdescriptionPostRequest.class));
        Mockito.verify(clearingHouse,Mockito.times(1)).sendToClearingHouse(any(), any());
        Mockito.verify(conversionService,Mockito.times(1)).convert(any(), any());
    }


}
