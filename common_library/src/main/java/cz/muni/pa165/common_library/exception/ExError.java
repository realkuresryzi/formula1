package cz.muni.pa165.common_library.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;

/**
 * Response when exception is thrown in API.
 */
public class ExError {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
  private final LocalDateTime dateTime = LocalDateTime.now();

  @Getter
  private final String message;

  public ExError(String message) {
    this.message = message;
  }
}
