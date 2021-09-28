package gc.cafe.order.aop;

import gc.cafe.order.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "gc.cafe.order.api")
public class ApiExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionAdvice.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(BadRequestException e) {
        logger.error("is invalid request : " + e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
