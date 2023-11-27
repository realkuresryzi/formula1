package cz.muni.pa165.common_library.exception;

import jakarta.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import org.apache.hc.client5.http.HttpHostConnectException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Exception handler for all controllers.
 */
@RestControllerAdvice
public class RestExceptionHandler {

  /**
   * Handlers for 404 status code.
   *
   * @param ex thrown exception.
   * @return Message with datetime.
   */
  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ExError> handleEntityNotFoundException(EntityNotFoundException ex) {
    return new ResponseEntity<>(
        new ExError(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  /**
   * Handlers for 404 when resource is not found in the database.
   *
   * @param ex thrown exception.
   * @return Message with datetime.
   */
  @ExceptionHandler(DatabaseException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ExError> handleDatabaseException(DatabaseException ex) {
    return new ResponseEntity<>(
        new ExError(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  /**
   * Handlers for 404 when there is no endpoint mapped to the requested path.
   *
   * @param ex thrown exception.
   * @return Message with datetime.
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ExError> handleNoHandlerFoundException(NoHandlerFoundException ex) {
    return new ResponseEntity<>(
        new ExError(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  /**
   * Handler for 400 status code when json is not valid.
   *
   * @param ex thrown exception.
   * @return Message with datetime.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ExError> handleNotReadableException(HttpMessageNotReadableException ex) {
    return new ResponseEntity<>(
        new ExError(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handler for 400 status code when json is not valid.
   *
   * @param ex thrown exception.
   * @return Message with datetime.
   */
  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ExError> handleValidationException(ValidationException ex) {
    return new ResponseEntity<>(
        new ExError(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handler for 400 status code when there are validation exceptions.
   *
   * @param ex thrown exception.
   * @return Message with datetime.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ExError> handleValidationException(MethodArgumentNotValidException ex) {
    String message = ex.getMessage();
    try {

      StringBuilder validtionMessage = new StringBuilder("Validation Errors: [");
      var validationErrors = ex.getBindingResult().getAllErrors();
      for (ObjectError error : validationErrors) {
        var errorField = (FieldError) error;
        var errorMessage = errorField.getDefaultMessage();
        var fieldName = errorField.getField();

        validtionMessage.append(" field ").append(fieldName).append(": ")
            .append(errorMessage).append(",");
      }
      validtionMessage.deleteCharAt(validtionMessage.length() - 1);
      validtionMessage.append(" ]");
      message = validtionMessage.toString();
    } catch (Exception e) {
      System.out.println("When creating validation error message: " + e.getMessage());
    }
    return new ResponseEntity<>(
        new ExError(message), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handler for server error exception.
   *
   * @param ex thrown exception.
   * @return Message with datetime.
   */
  @ExceptionHandler(HttpServerErrorException.class)
  public ResponseEntity<ExError> handleServerException(HttpServerErrorException ex) {
    return new ResponseEntity<>(
        new ExError(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handler for server error exception.
   *
   * @param ex thrown exception.
   * @return Message with datetime.
   */
  @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
  public ResponseEntity<ExError> handleServerException(
      HttpServerErrorException.InternalServerError ex) {
    return new ResponseEntity<>(
        new ExError(ex.getMessage()), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Handler for server error exception.
   *
   * @param ex thrown exception.
   * @return Message with datetime.
   */
  @ExceptionHandler(HttpHostConnectException.class)
  public ResponseEntity<ExError> handleServerException(HttpHostConnectException ex) {
    var message = "";
    if (ex.getMessage().contains("8083")) {
      message = "Failed to connect to the driver service";
    } else if (ex.getMessage().contains("8082")) {
      message = "Failed to connect to the car service";
    }
    return new ResponseEntity<>(
        new ExError(message), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * UnAuthorized access.
   *
   * @param ex caught ex
   * @return message
   */
  @ExceptionHandler(BadCredentialsException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<ExError> handleBadCredentialsException(BadCredentialsException ex) {
    return new ResponseEntity<>(
        new ExError(ex.getMessage()), new HttpHeaders(), HttpStatus.FORBIDDEN);
  }
}
