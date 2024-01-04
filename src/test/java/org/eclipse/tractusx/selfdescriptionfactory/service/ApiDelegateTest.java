package org.eclipse.tractusx.selfdescriptionfactory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.selfdescriptionfactory.api.vrel3.ApiApi;
import org.eclipse.tractusx.selfdescriptionfactory.api.vrel3.ApiApiController;
import org.eclipse.tractusx.selfdescriptionfactory.api.vrel3.ApiApiDelegate;
import org.eclipse.tractusx.selfdescriptionfactory.model.vrel3.SelfdescriptionPostRequest;
import org.eclipse.tractusx.selfdescriptionfactory.service.vrel3.ApiDelegate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;


import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiDelegateTest {

    private SDFactory sdFactoryMock = Mockito.mock(SDFactory.class);

    private ObjectMapper mapper;

    private ApiApi apiDelegate;

    private String sdRequest;
    @BeforeAll
    public void setUp(){
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

        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        apiDelegate = new ApiApi() {
            @Override
            public ApiApiDelegate getDelegate() {
                return ApiApi.super.getDelegate();
            }
        };
    }

    @Test
    public void selfDescriptionPostTest() {
        ApiDelegate apiDelegate = new ApiDelegate(sdFactoryMock);
        Mockito.doNothing().when(sdFactoryMock).createVC(any());
        ResponseEntity<Void> responseEntity = apiDelegate.selfdescriptionPost(Mockito.mock(SelfdescriptionPostRequest.class));
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 202);
    }

    @Test
    public void getApiDelegateTest() {

        apiDelegate.getDelegate();
        Assertions.assertTrue(apiDelegate != null);
        Assertions.assertTrue(apiDelegate.getDelegate() != null);
    }

    @Test
    public void selfDescriptionPostTestInApiDelegate() throws JsonProcessingException {
        ResponseEntity<Void> responseEntity = apiDelegate.selfdescriptionPost(mapper.readValue(sdRequest,SelfdescriptionPostRequest.class));
        Assertions.assertTrue(responseEntity != null);

        ApiApi apiApi = new ApiApiController(apiDelegate.getDelegate());
        Assertions.assertTrue(apiApi.getDelegate() != null);

    }
}