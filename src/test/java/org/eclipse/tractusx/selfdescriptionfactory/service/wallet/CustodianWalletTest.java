package org.eclipse.tractusx.selfdescriptionfactory.service.wallet;

import com.danubetech.verifiablecredentials.*;


import org.eclipse.tractusx.selfdescriptionfactory.service.keycloak.*;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.keycloak.admin.client.*;
import org.keycloak.admin.client.token.*;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;


public class CustodianWalletTest {

    private TokenManager tokenManager = Mockito.mock(TokenManager.class);

    private KeycloakManager keycloakManager = Mockito.mock(KeycloakManager.class);

    private Keycloak keycloak = Mockito.mock(Keycloak.class);

    @BeforeEach
    public void Setup(){
    }

    @Test
    public void getSignedVcTest(){
        CustodianClient custodianClient =  Mockito.mock(CustodianClient.class);
        var vc  = new VerifiableCredential();
        vc.setJsonObjectKeyValue("test","test");
        Mockito.when(custodianClient.getSignedVC(any())).thenReturn(VerifiableCredential.fromJson(vc.toString()));
        CustodianWallet cw = new CustodianWallet(custodianClient);
        VerifiableCredential vc2 = cw.getSignedVC(vc);
        Assertions.assertNotNull(vc2);
        Mockito.verify(custodianClient,Mockito.times(1)).getSignedVC(any());
    }

    @Test
    public void getWalletDetailsTest(){
        CustodianClient custodianClient =  Mockito.mock(CustodianClient.class);
        Map<String, Object> walletResposne = Map.of("bpn", "BPNL000000000001",
                "did", "did:sov:AAAyVfpadka4iDqwCLrYL3",
                "name", "smartSense Consulting Solutions pvt ltd");
        //String walletResposne = "{\"name\":\"John\", \"age\":30, \"car\":null, \"key\":weqweqe}";
        Mockito.when(custodianClient.getWalletData(any())).thenReturn(walletResposne);
        CustodianWallet cw = new CustodianWallet(custodianClient);
        Map<String, Object> walletData = cw.getWalletData("wewe");
        Assertions.assertNotNull(walletData);
    }
}