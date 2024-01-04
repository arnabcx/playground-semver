package org.eclipse.tractusx.selfdescriptionfactory.exceptionhandler;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException feignException) {
        log.error(Constants.FEIGN_EXCEPTION, feignException.getMessage());
        return new ResponseEntity<>(feignException.getMessage(), HttpStatus.valueOf(feignException.status()));
    }

}
