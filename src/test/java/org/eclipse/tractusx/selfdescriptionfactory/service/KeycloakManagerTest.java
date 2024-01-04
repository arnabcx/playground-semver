package org.eclipse.tractusx.selfdescriptionfactory.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.tractusx.selfdescriptionfactory.config.TechnicalUsersDetails;
import org.eclipse.tractusx.selfdescriptionfactory.service.keycloak.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class KeycloakManagerTest {

    private TechnicalUsersDetails technicalUsersDetails = Mockito.mock(TechnicalUsersDetails.class);

    private KeycloakClient keycloakClient = Mockito.mock(KeycloakClient.class);

    private static final String KEY_CLOAK_USER = "custodianWallet";

    @Test
    public void getKeycloack_whenTokenNotExpired() {
        Map<String,Object> tokens = new HashMap<>();
        tokens.put("access_token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZXhwIjoxNzE2MjM5MDIyfQ.FuJB22Dq3zXcFXtIAc59tWnmbbFC6jRXzb_2ejbhhoQ");
        Map<String, TechnicalUsersDetails.UserDetail> usersDetails = new HashMap<>() {{
            put(KEY_CLOAK_USER, new TechnicalUsersDetails.UserDetail(
                    "serverUrl",
                    "realm",
                    null,
                    "password",
                    "clientId",
                    "clientSecret"));
        }};


        Mockito.when(technicalUsersDetails.getUsersDetails()).thenReturn(usersDetails);
        Mockito.when(keycloakClient.getTokens(Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(tokens);
        KeycloakManager keycloakManager = new KeycloakManager(technicalUsersDetails,keycloakClient,new ObjectMapper());
        String keycloak = keycloakManager.getToken("custodianWallet");
        Assertions.assertNotNull(keycloak);
        //test when token is present in token map
        String keycloakFromTokenMap = keycloakManager.getToken("custodianWallet");
        Assertions.assertNotNull(keycloakFromTokenMap);
    }

    @Test
    public void getKeycloack() throws Exception {

        Map<String,Object> tokens = new HashMap<>();
        tokens.put("access_token","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiZXhwIjoxNTE2MjM5MDIyfQ.E9bQ6QAil4HpH825QC5PtjNGEDQTtMpcj0SO2W8vmag");
        Map<String, TechnicalUsersDetails.UserDetail> usersDetails = new HashMap<>() {{
            put(KEY_CLOAK_USER, new TechnicalUsersDetails.UserDetail(
                    "serverUrl",
                    "realm",
                    null,
                    "password",
                    "clientId",
                    "clientSecret"));
        }};


        Mockito.when(technicalUsersDetails.getUsersDetails()).thenReturn(usersDetails);
        Mockito.when(keycloakClient.getTokens(Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(tokens);
        KeycloakManager keycloakManager = new KeycloakManager(technicalUsersDetails,keycloakClient,new ObjectMapper());
        String keycloak = keycloakManager.getToken("custodianWallet");
        Assertions.assertNotNull(keycloak);
        //test when token is present in token map
        String keycloakFromTokenMap = keycloakManager.getToken("custodianWallet");
        Assertions.assertNotNull(keycloakFromTokenMap);

    }

    @Test
    public void getKeycloackWhenUserDetailsIsNull() throws Exception {

        Map<String,Object> tokens = new HashMap<>();
        tokens.put("access_token","token-dummy");
        Mockito.when(keycloakClient.getTokens(Mockito.any(),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(tokens);
        KeycloakManager keycloakManager = new KeycloakManager(technicalUsersDetails,keycloakClient,new ObjectMapper());
        String keycloak = keycloakManager.getToken("custodianWallet");
        Assertions.assertNull(keycloak);
    }


}