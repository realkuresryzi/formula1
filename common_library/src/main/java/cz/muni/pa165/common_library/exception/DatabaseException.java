package cz.muni.pa165.common_library.exception;

/**
 * Exception when a query does is not executed correctly.
 */
public class DatabaseException extends RuntimeException {

  public DatabaseException(String message) {
    super(message);
  }
}
