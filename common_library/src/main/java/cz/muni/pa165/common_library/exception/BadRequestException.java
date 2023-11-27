package cz.muni.pa165.common_library.exception;

/**
 * Exception when request is not valid.
 */
public class BadRequestException  extends RuntimeException {

  public BadRequestException(String message) {
    super(message);
  }
}
