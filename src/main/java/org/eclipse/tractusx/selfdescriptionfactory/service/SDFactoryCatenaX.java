/********************************************************************************
 * Copyright (c) 2022,2023 T-Systems International GmbH
 * Copyright (c) 2022,2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/

package org.eclipse.tractusx.selfdescriptionfactory.service;

import com.danubetech.verifiablecredentials.*;
import foundation.identity.jsonld.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.eclipse.tractusx.selfdescriptionfactory.service.clearinghouse.*;
import org.eclipse.tractusx.selfdescriptionfactory.service.wallet.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.convert.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.util.*;

/**
 * A service to create and manipulate of Self-Description document
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Profile("catena-x-ctx")
public class SDFactoryCatenaX implements SDFactory{
    @Value("${app.verifiableCredentials.durationDays:90}")
    private int duration;
    private final CustodianWallet custodianWallet;
    private final ConversionService conversionService;
    private final ClearingHouse clearingHouse;

    @Override
    @PreAuthorize("hasAuthority(@securityRoles.createRole)")
    public void createVC(Object document) {
        var claimsHolder = Optional.ofNullable(conversionService.convert(document, Claims.class)).orElseThrow();
        var claims = new LinkedHashMap<>(claimsHolder.claims());
        var holder = claims.remove("holder");
        var issuer = claims.remove("issuer");
        var type = claims.get("type");
        var externalId = claims.remove("externalId");
        var credentialSubject = CredentialSubject.fromJsonObject(claims);
        var verifiableCredential = VerifiableCredential.builder()
                .contexts(claimsHolder.vocabularies())
                .issuanceDate(new Date())
                .expirationDate(Date.from(Instant.now().plus(Duration.ofDays(duration))))
                .credentialSubject(credentialSubject)
                .build();
        JsonLDUtils.jsonLdAdd(verifiableCredential, "issuerIdentifier", issuer);
        JsonLDUtils.jsonLdAdd(verifiableCredential, "holderIdentifier", holder);
        JsonLDUtils.jsonLdAdd(verifiableCredential, "type", type);
        var vc = custodianWallet.getSignedVC(verifiableCredential);
        clearingHouse.sendToClearingHouse(vc, externalId.toString());
    }
}
