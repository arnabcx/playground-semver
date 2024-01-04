package org.eclipse.tractusx.selfdescriptionfactory.service.v2210;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.selfdescriptionfactory.model.v2210.SelfdescriptionPostRequest;
import org.eclipse.tractusx.selfdescriptionfactory.service.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("catena-x-ctx")
public class SDocumentConverterTest {

    private String sdRequest;

    @Autowired
    private SDocumentConverter sdConverter;

    @BeforeEach
    public void setup() {
        sdRequest = """
                {
                  "externalId": "ID01234-123-4321",
                  "type": "LegalParticipant",
                  "holder": "BPNL000000000000",
                  "issuer": "CAXSDUMMYCATENAZZ",
                  "registrationNumber": [
                    {
                      "type": "taxID",
                      "value": "o12345678"
                    }
                  ],
                  "headquarterAddress.country": "DE",
                  "legalAddress.country": "DE",
                  "bpn": "BPNL000000000000"
                }""";

    }

    @Test
    public void convertTest() throws JsonProcessingException {

        System.setProperty("app.verifiableCredentials.schema2210Url","sdsfsfs");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SelfdescriptionPostRequest sdf = mapper.readValue(sdRequest, SelfdescriptionPostRequest.class);
        Claims claims = sdConverter.convert(sdf);
        Assertions.assertNotNull(claims);



    }
}