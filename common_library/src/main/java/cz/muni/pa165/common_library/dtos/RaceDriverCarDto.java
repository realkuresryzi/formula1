package cz.muni.pa165.common_library.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto fro DriverCar entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaceDriverCarDto {

  @Schema(description = "driver id", example = "1")
  Long driverId;

  @Schema(description = "car id", example = "1")
  Long carId;

  @Min(value = 1, message = "Minimal position is 1")
  @Max(value = 20, message = "Maximal position is 20")
  @Schema(description = "drivers position in the race", example = "1")
  Integer position;
}
