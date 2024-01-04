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

package org.eclipse.tractusx.selfdescriptionfactory.config;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import feign.*;
import feign.codec.*;
import io.vavr.control.*;
import lombok.extern.slf4j.*;
import org.eclipse.tractusx.selfdescriptionfactory.service.keycloak.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.web.server.*;

import java.util.*;

import static org.eclipse.tractusx.selfdescriptionfactory.exceptionhandler.Constants.*;

@Configuration
@Slf4j
public class DefaultFeignConfig {
    @Bean
    public RequestInterceptor getRequestInterceptor(KeycloakManager keycloakManager) {
        return requestTemplate -> Optional.of(requestTemplate.feignTarget().name())
                .map(keycloakManager::getToken)
                .ifPresent(token -> requestTemplate.header("Authorization", "Bearer ".concat(token)));

    }

    @Bean
    public ErrorDecoder getErrorDecoder(ObjectMapper mapper) {
        return (methodKey, response) -> {
            var msg = Try.of(() -> response.body().asReader(response.charset()))
                    .mapTry(mapper::readTree)
                    .map(node -> response.status() == BAD_REQUEST_ERROR_CODE ? ((ArrayNode) node.get("response")).get(0) : node.get("message"))
                    .filter(Objects::nonNull)
                    .map(JsonNode::asText)
                    .map(" : "::concat)
                    .recover(err -> "").get();
            var statusCode = HttpStatusCode.valueOf(response.status());
            if (response.status() == NOT_FOUND_ERROR_CODE) {
                log.error(NOT_FOUND.concat(response.request().url().concat(msg)));
                return new ResponseStatusException(statusCode, response.request().url().concat(msg));
            } else if (response.status() == BAD_REQUEST_ERROR_CODE) {
                log.error(msg);
                return new ResponseStatusException(statusCode, msg);
            }
            //log.error("Original payload: {}", new String(response.request().body(), response.request().charset()));
            return new ResponseStatusException(statusCode, methodKey.concat(msg));

        };
    }
}
