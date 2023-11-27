package cz.muni.pa165.common_library.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer object for Car class.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarResponseDto {

  @NotNull
  @Schema(description = "car id",
          example = "1")
  Long id;

  @NotNull
  @Schema(description = "car's components ids and names",
          example     = "[]")
  List<Long> componentIdsNames;

  @NotNull
  @Schema(description = "car's drivers ids and names",
          example     = "[]")
  List<Long> driverIdsNames;

  @Schema(description = "id of car's main driver",
          example     = "1")
  Long mainDriverId;

}
