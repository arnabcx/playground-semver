package org.eclipse.tractusx.selfdescriptionfactory.integration;
import org.eclipse.tractusx.selfdescriptionfactory.service.clearinghouse.ClearingHouseClient;
import org.eclipse.tractusx.selfdescriptionfactory.service.keycloak.KeycloakManager;
import org.eclipse.tractusx.selfdescriptionfactory.service.wallet.CustodianClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@AutoConfigureMockMvc
class SelfDescriptionIT {
    @Autowired
    protected MockMvc mockMvc;
    @MockBean
    CustodianClient custodianClient;
    @MockBean
    ClearingHouseClient clearingHouseClient;

    @MockBean
    KeycloakManager keycloakManager;


    @Test
    @WithMockUser(authorities = {"add_self_descriptions"})
    void givenValidLegalPersonSchema() throws Exception {
        when(custodianClient.getWalletData(anyString())).thenReturn(IntegrationUtil.mockedWalletRes);
        clearingHouseClient.send(any(), anyString());
        Mockito.verify(clearingHouseClient, times(1)).send(any(), anyString());
        when(keycloakManager.getToken(anyString())).thenReturn("token");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rel3/selfdescription").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(IntegrationUtil.getLegalPerson("BPNL000000000001"))).andDo(MockMvcResultHandlers.print()).andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(authorities = {"add_self_descriptions"})
    void givenInvalidLegalPersonSchema() throws Exception {
        when(custodianClient.getWalletData(anyString())).thenReturn(IntegrationUtil.mockedWalletRes);
        clearingHouseClient.send(any(), anyString());
        Mockito.verify(clearingHouseClient, times(1)).send(any(), anyString());
        when(keycloakManager.getToken(anyString())).thenReturn("token");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rel3/selfdescription").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(IntegrationUtil.getLegalPerson(null))).andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"add_self_descriptions"})
    void givenValidServiceOfferingSchema() throws Exception {
        when(custodianClient.getWalletData(anyString())).thenReturn(IntegrationUtil.mockedWalletRes);
        clearingHouseClient.send(any(), anyString());
        Mockito.verify(clearingHouseClient, times(1)).send(any(), anyString());
        when(keycloakManager.getToken(anyString())).thenReturn("token");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rel3/selfdescription").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(IntegrationUtil.getServiceOffering("BPNL000000000001"))).andDo(MockMvcResultHandlers.print()).andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(authorities = {"add_self_descriptions"})
    void givenInvalidServiceOfferingSchema() throws Exception {
        when(custodianClient.getWalletData(anyString())).thenReturn(IntegrationUtil.mockedWalletRes);
        clearingHouseClient.send(any(), anyString());
        Mockito.verify(clearingHouseClient, times(1)).send(any(), anyString());
        when(keycloakManager.getToken(anyString())).thenReturn("token");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/rel3/selfdescription").accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).content(IntegrationUtil.getServiceOffering(null))).andDo(MockMvcResultHandlers.print()).andExpect(status().isBadRequest());
    }

}
