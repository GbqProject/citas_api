package co.fcv.citas.infrastructure.adapters.in.rest.auth;

import co.fcv.citas.application.auth.AuthService.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(AuthException.class) org.springframework.http.ResponseEntity<ErrorResponse> auth(AuthException ex) {
        HttpStatus status = ex.code().equals("INVALID_CREDENTIALS") || ex.code().equals("INVALID_REFRESH_TOKEN") ? HttpStatus.UNAUTHORIZED : HttpStatus.CONFLICT;
        return org.springframework.http.ResponseEntity.status(status).body(new ErrorResponse(ex.code()));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) org.springframework.http.ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex) {
        return org.springframework.http.ResponseEntity.badRequest().body(new ErrorResponse("VALIDATION_ERROR"));
    }
    record ErrorResponse(String code) { }
}
