package org.eclipse.tractusx.selfdescriptionfactory.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.selfdescriptionfactory.model.vrel3.*;
import org.eclipse.tractusx.selfdescriptionfactory.model.vrel3.LegalParticipantSchema;

import java.net.URI;
import java.util.*;


public class IntegrationUtil {
    protected static Map<String, Object> mockedWalletRes = Map.of("bpn", "BPNL000000000001",
            "did", "did:sov:AAAyVfpadka4iDqwCLrYL3",
            "name", "smartSense Consulting Solutions pvt ltd");

    protected static String getLegalPerson(String bpn) throws JsonProcessingException {
        LegalParticipantSchema schems = new LegalParticipantSchema();
        RegistrationNumberSchema registrationNumberSchema = new RegistrationNumberSchema();
        Set<RegistrationNumberSchema> registrationNumber = new LinkedHashSet<>();
        registrationNumber.add(registrationNumberSchema);
        registrationNumberSchema.setType(RegistrationNumberSchema.TypeEnum.TAXID);
        registrationNumberSchema.setValue("DE118645675");
        schems.setType("LegalPerson");
        schems.setExternalId("ID01234-123-4321");
        schems.setHolder("BPNL000000000001");
        schems.setIssuer("BPNL000000000000");
        schems.setRegistrationNumber(registrationNumber);
        schems.setHeadquarterAddressCountry("DE");
        schems.setLegalAddressCountry("DE-HH");
        schems.setBpn(bpn);
        String req = new ObjectMapper().writeValueAsString(schems);
        return req;
    }

    protected static String getServiceOffering(String holder) throws JsonProcessingException {
        ServiceOfferingSchema serviceOfferingSchema = new ServiceOfferingSchema();
        serviceOfferingSchema.setType("ServiceOffering");
        serviceOfferingSchema.setHolder(holder);
        serviceOfferingSchema.setIssuer("BPNL000000000000");
        serviceOfferingSchema.setExternalId("ID01234-123-4321");
        serviceOfferingSchema.setProvidedBy(URI.create("http://dummy/1"));
        serviceOfferingSchema.setAggregationOf("testing");
        String req = new ObjectMapper().writeValueAsString(serviceOfferingSchema);
        return req;
    }

}
