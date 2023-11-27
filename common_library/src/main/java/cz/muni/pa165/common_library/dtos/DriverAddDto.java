package cz.muni.pa165.common_library.dtos;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


/**
 * DriverAdd dto.
 */
@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverAddDto {

  @NotBlank(message = "Driver's name cannot be blank.")
  String name;

  @NotBlank(message = "Driver's surname cannot be blank.")
  String surname;

  @NotBlank(message = "Driver's nationality cannot be blank.")
  String nationality;

  @Nullable
  Map<@Pattern(
      regexp = "technical_knowledge|aggressiveness|consistency|reactions"
          + "|fitness|teamwork|adaptability",
      message = "Driver's characteristics has to be valid.")
      String,
      @Min(value = 1, message = "Characteristic level has to range from 1 to 5.")
      @Max(value = 5, message = "Characteristic level has to range from 1 to 5.")
          Integer> characteristics;
}
