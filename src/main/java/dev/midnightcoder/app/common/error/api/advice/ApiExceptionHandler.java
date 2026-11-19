package dev.midnightcoder.app.common.error.api.advice;

import dev.midnightcoder.app.common.error.ApiException;
import dev.midnightcoder.app.common.error.ServiceException;
import dev.midnightcoder.app.common.error.api.BadRequestApiException;
import dev.midnightcoder.app.common.error.api.ForbiddenApiException;
import dev.midnightcoder.app.common.error.api.NotFoundApiException;
import dev.midnightcoder.app.common.error.services.InvalidStateException;
import dev.midnightcoder.app.common.error.services.ResourceNotFoundException;
import dev.midnightcoder.app.common.error.services.UnauthorizedOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Glabay | Glabay-Studios
 * @project Midnight-Coder
 * @social Discord: Glabay
 * @since 2025-12-27
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    ///  ===== SERVICE EXCEPTIONS =====

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ApiException> handleInvalidState(InvalidStateException ex) {
        log.error("Invalid state exception: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_ACCEPTABLE)
            .body(new NotFoundApiException(ex.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiException> handleNotFound(ResourceNotFoundException ex) {
        log.error("Resource not found exception: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new NotFoundApiException(ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ApiException> handleForbidden(UnauthorizedOperationException ex) {
        log.error("Unauthorized operation exception: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(new ForbiddenApiException(ex.getMessage()));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiException> handleServiceException(ServiceException ex) {
        log.error("Service exception: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new BadRequestApiException(ex.getMessage()));
    }
}
