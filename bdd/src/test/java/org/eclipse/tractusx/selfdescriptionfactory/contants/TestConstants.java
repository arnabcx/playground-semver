package org.eclipse.tractusx.selfdescriptionfactory.contants;

public class TestConstants {

    public static final String  AUTHORIZATION = "Authorization";
    public static final String Bearer = "Bearer ";

    public static final String TOKEN_URI = "/auth/realms/CX-Central/protocol/openid-connect/token";
    public static final String CREATE_VC_URI = "/api/rel3/selfdescription";
    public static final String VALID_VC_REQUEST = "{\"externalId\":\"ID01234-123-4321\",\"type\":\"LegalParticipant\"," +
            "\"holder\":\"BPNL000000000000\",\"issuer\":\"BPNL000000000000\"," +
            "\"registrationNumber\":[{\"type\":\"vatID\",\"value\":\"DE118645675\"}]," +
            "\"headquarterAddress.country\":\"DE\",\"legalAddress.country\":\"DE-HH\",\"bpn\":\"BPNL000000000001\"}";
     public static final String INVALID_VC_REQUEST = "{\"externalId\":\"ID01234-123-4321\",\"type\":\"LegalPerson\"," +
             "\"holder\":\"BPNL000000000000\",\"issuer\":\"BPNL000000000000\"," +
             "\"registrationNumber\":[{\"type\":\"vatID\",\"value\":\"DE118645675\"}]," +
             "\"headquarterAddress.country\":\"DE\",\"legalAddress.country\":\"DE-HH\",\"bpn\":\"BPNL000000000001\"}";

    public static final String VC_REQUEST_WITH_INVALID_WALLET = "{\"externalId\":\"ID01234-123-4321\",\"type\":\"LegalParticipant\"," +
            "\"holder\":\"BPNL000000003423\",\"issuer\":\"BPNL000000003423\"," +
            "\"registrationNumber\":[{\"type\":\"vatID\",\"value\":\"DE118645675\"}]," +
            "\"headquarterAddress.country\":\"DE\",\"legalAddress.country\":\"DE-HH\",\"bpn\":\"BPNL000000009999\"}";
}
