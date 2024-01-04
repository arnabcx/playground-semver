package org.eclipse.tractusx.selfdescriptionfactory.service.clearinghouse;

import static org.mockito.ArgumentMatchers.anyString;

import com.danubetech.verifiablecredentials.VerifiableCredential;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.selfdescriptionfactory.service.keycloak.KeycloakManager;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.mockito.Mockito;


public class ClearingHouseMockTest {

    private KeycloakManager keycloakManagerMock = Mockito.mock(KeycloakManager.class);
    private Keycloak keycloakMock = Mockito.mock(Keycloak.class);
    private TokenManager tokenManagerMock = Mockito.mock(TokenManager.class);
    private VerifiableCredential verifiableCredential = Mockito.mock(VerifiableCredential.class);

    private static final String EXTERNAL_ID = "externalID";

    @Test
    public void doWorkTest(){
        ClearingHouse clearingHouseMock = new ClearingHouseMock(new ObjectMapper());
        clearingHouseMock.keycloakManager = keycloakManagerMock;
        Mockito.when(keycloakManagerMock.getToken(anyString())).thenReturn("dasdadasdad");
        clearingHouseMock.sendToClearingHouse(verifiableCredential, EXTERNAL_ID);

    }
}