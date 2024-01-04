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

package org.eclipse.tractusx.selfdescriptionfactory.service.keycloak;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import lombok.*;
import org.eclipse.tractusx.selfdescriptionfactory.config.*;
import org.springframework.stereotype.*;

import java.net.*;
import java.time.*;
import java.util.*;

@Component
@RequiredArgsConstructor
public class KeycloakManager {

    private static final long REFRESH_GAP = 60L;
    private final TechnicalUsersDetails technicalUsersDetails;
    private final Map<String, String> tokenMap = new HashMap<>();

    private final KeycloakClient keycloakClient;

    private final ObjectMapper mapper;

    @SneakyThrows
    public String getToken(String name) {
        var token = tokenMap.get(name);
        if (!Objects.isNull(token)) {
            var jwt = mapper.readValue(Base64.getDecoder().decode(token.split("\\.")[1]), new TypeReference<Map<String, Object>>(){});
            var expirationInstant = Instant.ofEpochSecond((Integer) jwt.get("exp"));
            if (Duration.between(Instant.now(), expirationInstant).compareTo(Duration.ofSeconds(REFRESH_GAP)) > 0) {
                return token;
            }
        }
        var details = technicalUsersDetails.getUsersDetails().get(name);
        if (Objects.isNull(details))
            return null;
        token = keycloakClient.getTokens(URI.create(details.serverUrl()), details.realm(), details.clientId(), details.clientSecret()).get("access_token").toString();
        tokenMap.put(name, token);
        return token;
    }
}

