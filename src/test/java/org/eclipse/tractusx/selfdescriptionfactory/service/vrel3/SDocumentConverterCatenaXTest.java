package org.eclipse.tractusx.selfdescriptionfactory.service.vrel3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.selfdescriptionfactory.model.vrel3.*;
import org.eclipse.tractusx.selfdescriptionfactory.model.vrel3.LegalParticipantSchema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("catena-x-ctx")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SDocumentConverterCatenaXTest {

    private String sdRequestLegalParticipant;
    private String sdRequestServiceOffering;

    private ObjectMapper mapper;

    @Autowired
    private SDocumentConverterCatenaX sDocumentConverterCatenaX;

    @BeforeAll
    public void setUp(){
        sdRequestLegalParticipant = "{\n" +
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

        sdRequestServiceOffering = "{\n" +
                "  \"externalId\": \"ID01234-123-4321\",\n" +
                "  \"type\": \"ServiceOffering\",\n" +
                "  \"holder\": \"BPNL000000000000\",\n" +
                "  \"issuer\": \"CAXSDUMMYCATENAZZ\",\n" +
                "  \"providedBy\": \"http://test\",\n" +
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

        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    public void convertTestForServiceOffering() throws JsonProcessingException {
        sDocumentConverterCatenaX.convert(mapper.readValue(sdRequestServiceOffering, ServiceOfferingSchema.class));
    }

    @Test
    public void convertTestForLegalParticipant() throws JsonProcessingException {
        sDocumentConverterCatenaX.convert(mapper.readValue(sdRequestLegalParticipant, LegalParticipantSchema.class));
    }

}
