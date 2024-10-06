package tz.go.zanemr.auth.core;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
@SuppressWarnings("unused")
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({BadCredentialsException.class})
  public ResponseEntity<Object> handleBadCredential(Exception ex, WebRequest request) {
    logger.error(ex.getMessage());

    CustomErrorResponse customErrorResponse =
        new CustomErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Authentication failed",
            "Invalid username or password");

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({HttpServerErrorException.InternalServerError.class})
  public ResponseEntity<Object> handleInternalServerErrorException(Exception ex, WebRequest request) {
    logger.error(ex.getMessage());
    CustomErrorResponse customErrorResponse =
            new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), ex.getMessage());

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({InvalidBearerTokenException.class, AuthenticationServiceException.class, AuthenticationException.class, OAuth2AuthenticationException.class})
  public ResponseEntity<Object> handleUnauthorizedErrorException(Exception ex, WebRequest request) {
    logger.error(ex.getMessage());
    CustomErrorResponse customErrorResponse =
            new CustomErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), ex.getMessage());

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
  }


  @ExceptionHandler({ValidationException.class})
  public ResponseEntity<Object> handleValidationException(Exception ex, WebRequest request) {
    logger.error(ex.getMessage());
    CustomErrorResponse customErrorResponse =
        new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation Error", ex.getMessage());

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({NoSuchMethodException.class})
  public ResponseEntity<Object> handleNoSuchMethodException(Exception ex, WebRequest request) {
    logger.error(ex.getMessage());
    CustomErrorResponse customErrorResponse =
        new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), ex.getMessage());

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({HttpServerErrorException.class})
  public ResponseEntity<Object> handleInternalServerError(
      HttpServerErrorException ex, WebRequest request) {
    logger.error(ex.getMessage());
    CustomErrorResponse customErrorResponse =
        new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), ex.getMessage());

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({InsufficientAuthenticationException.class})
  public ResponseEntity<Object> handleAuthenticationRequired(
      InsufficientAuthenticationException ex, WebRequest request) {

    logger.error(ex.getMessage());
    List<String> errors = new ArrayList<>();
    CustomErrorResponse customErrorResponse =
        new CustomErrorResponse(
            HttpStatus.UNAUTHORIZED.value(), "Authentication required", "Authentication required");

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({InvalidDataAccessApiUsageException.class})
  public ResponseEntity<Object> handleInvalidDataAccessException(
      InvalidDataAccessApiUsageException ex, WebRequest request) {
    logger.error(ex.getMessage());

    CustomErrorResponse customErrorResponse =
        new CustomErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMostSpecificCause().getMessage(),
            ex.getMostSpecificCause().getLocalizedMessage());

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Object> handleEntityNotFound(
      EntityNotFoundException ex, WebRequest request) {
    logger.error(ex.getMessage());

    CustomErrorResponse customErrorResponse =
        new CustomErrorResponse(HttpStatus.NOT_FOUND.value(), "Entity Not found!", ex.getMessage());

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({EmptyResultDataAccessException.class})
  public ResponseEntity<Object> handleEmptyResultDataAccessException(
      EmptyResultDataAccessException ex, WebRequest request) {
    logger.error(ex.getMessage());

    CustomErrorResponse customErrorResponse =
        new CustomErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMostSpecificCause().getMessage(),
            ex.getMostSpecificCause().getLocalizedMessage());

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleDataIntegrityException(
      DataIntegrityViolationException ex, WebRequest request, HttpServletRequest httpRequest) {
    logger.error(ex.getMostSpecificCause());
    String str = ex.getMostSpecificCause().getLocalizedMessage();
    if (str.contains("violates foreign key constraint")
        && httpRequest.getMethod().equals("DELETE")) {
      str = "You cannot delete this item";
    } else if (str.contains("duplicate key ")) {
      str = str.substring(str.indexOf("Key") + 3).replace(")", "").replace("(", "");
    } else {
      str = str.substring(str.indexOf("Key") + 3);
    }

    CustomErrorResponse customErrorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), str, str);

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleDataIntegrityException(
      ConstraintViolationException ex, WebRequest request) {
    String str = ex.getLocalizedMessage();
    str = str.substring(str.indexOf("Key") + 1);
    CustomErrorResponse customErrorResponse = new CustomErrorResponse(HttpStatus.BAD_REQUEST.value(), str, str);

    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  @Override
  public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
    super.handleExceptionInternal(ex, body, headers, statusCode, request);
    ex.printStackTrace();
    CustomErrorResponse customErrorResponse = new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Whoops something went wrong", ex.getMessage());
    return new ResponseEntity<>(customErrorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
