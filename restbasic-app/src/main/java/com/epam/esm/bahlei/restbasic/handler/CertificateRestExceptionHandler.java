package com.epam.esm.bahlei.restbasic.handler;

import com.epam.esm.bahlei.restbasic.config.exception.response.ErrorResponse;
import com.epam.esm.bahlei.restbasic.service.validator.exception.ValidationException;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class CertificateRestExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(Exception exc) {
    ErrorResponse error = new ErrorResponse(exc.getMessage());

    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
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
  public ResponseEntity<ErrorResponse> handleException(DataAccessException exc) {
    ErrorResponse error = new ErrorResponse("Wrong parameters");

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException exc) {
    ErrorResponse error = new ErrorResponse("Wrong parameter value");

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
}
