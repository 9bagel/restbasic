package com.epam.esm.bahlei.restbasic.handler;

import com.epam.esm.bahlei.restbasic.controller.dto.response.ErrorResponse;
import com.epam.esm.bahlei.restbasic.security.exception.TokenExpiredException;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import com.fasterxml.jackson.core.JsonParseException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class CertificateRestExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(Exception exc) {
    ErrorResponse error = new ErrorResponse(exc.getMessage());

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(HttpMediaTypeNotSupportedException exc) {
    ErrorResponse error = new ErrorResponse(exc.getMessage());

    return new ResponseEntity<>(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(HttpRequestMethodNotSupportedException exc) {
    ErrorResponse error = new ErrorResponse(exc.getMessage());

    return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(JsonParseException exc) {
    ErrorResponse error = new ErrorResponse("Invalid JSON format");

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException exc) {
    ErrorResponse error = new ErrorResponse("Invalid JSON format");

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(ValidationException exc) {
    ErrorResponse error = new ErrorResponse(exc.getErrors());

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException exc) {
    ErrorResponse error = new ErrorResponse("Wrong parameter value");

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(AccessDeniedException exc) {
    ErrorResponse error = new ErrorResponse("Access is denied");

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(AuthenticationException exc) {
    ErrorResponse error = new ErrorResponse("Authentication exception");

    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(BadCredentialsException exc) {
    ErrorResponse error = new ErrorResponse(exc.getMessage());

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(TokenExpiredException.class)
  public ResponseEntity<ErrorResponse> handleException(TokenExpiredException exc) {
    ErrorResponse error = new ErrorResponse(exc.getMessage());

    return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(JwtException.class)
  public ResponseEntity<ErrorResponse> handleException(JwtException exc) {
    ErrorResponse error = new ErrorResponse(exc.getMessage());

    return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
  }
}
