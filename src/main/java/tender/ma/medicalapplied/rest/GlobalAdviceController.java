package tender.ma.medicalapplied.rest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tender.ma.medicalapplied.exceptions.BaseServiceException;
import tender.ma.medicalapplied.exceptions.error.ApiErrorResponse;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalAdviceController {
    private static final String VIOLATION_ERROR_MSG_PREFIX = "Отсутствуют обязательные параметры: ";
    /**
     * Обработка ConstraintViolationException.
     *
     * @param exc исключение
     * @return модель исключения
     */

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(ConstraintViolationException exc) {
        var message = VIOLATION_ERROR_MSG_PREFIX + exc.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity
                .badRequest()
                .body(toErrorResponse(HttpStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(BaseServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleBaseServiceExceptions(BaseServiceException exc) {
        return ResponseEntity
                .status(exc.getStatus())
                .body(toErrorResponse(exc.getStatus(), exc.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleExceptions(Exception exc) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(toErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage()));
    }

    private ApiErrorResponse toErrorResponse(HttpStatus status, String message) {
        return new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                LocalDateTime.now()
        );
    }


}