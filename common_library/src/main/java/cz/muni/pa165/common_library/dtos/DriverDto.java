package cz.muni.pa165.common_library.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer object for Driver class.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto {

  @NotNull
  @Schema(description = "driver id",
          example     = "1")
  Long id;

  @NotNull
  @Schema(description = "driver name",
          example     = "Max")
  String name;

  @NotNull
  @Schema(description = "driver surname",
          example     = "Verstappen")
  String surname;

}
