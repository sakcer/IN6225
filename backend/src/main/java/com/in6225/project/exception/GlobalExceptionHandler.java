package com.in6225.project.exception;

import com.in6225.project.dto.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Throwable.class)
//    public ResponseEntity<ErrorDTO<?>> handleException(HttpServletRequest request, Throwable e) {
//        return ResponseEntity.ok(ErrorDTO.of(SystemErrorCode.SYSTEM_ERROR, e.getMessage()));
//    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorDTO<?>> handleException(HttpServletRequest request, Throwable e) {
        return ResponseEntity.ok(ErrorDTO.of(SystemErrorCode.SYSTEM_ERROR, e.getMessage()));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorDTO<?>> badCredentialsException(HttpServletRequest request, Throwable e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDTO.of(SystemErrorCode.ID_PASSWORD_ERROR, e.getMessage()));
    }

    @ExceptionHandler(value = JwtException.class)
    public ResponseEntity<ErrorDTO<?>> jwtException(HttpServletRequest request, Throwable e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDTO.of(SystemErrorCode.INVALID_TOKEN, e.getMessage()));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> accessDeniedException(HttpServletRequest request, Throwable e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorDTO.of(SystemErrorCode.NO_PERMISSION));
    }

}
