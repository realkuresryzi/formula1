package cz.muni.pa165.common_library.exception;

/**
 * Exception when resource does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String message) {
    super(message);
  }

}
