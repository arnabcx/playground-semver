package org.eclipse.tractusx.selfdescriptionfactory.service.clearinghouse;

import com.danubetech.verifiablecredentials.*;
import com.fasterxml.jackson.databind.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Service
@Profile("test")
@Slf4j
@RequiredArgsConstructor
public class ClearingHouseMock extends ClearingHouse{
    private final ObjectMapper objectMapper;
    @Override
    @SneakyThrows
    public void doWork(String url, VerifiableCredential payload, String externalId, String token) {
        log.debug("URL: {}", url);
        log.debug("Authorization: {}", token);
        log.debug("ExternalId: {}", externalId);
        log.debug("Payload: {}", objectMapper.writeValueAsString(payload));
    }
}

