package cz.muni.pa165.common_library.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

/**
 * Data Transfer object for Car class.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestDto {

  @NotNull
  @Schema(description = "car's components ids",
          example     = "[]")
  List<Long> componentIds;

  @NotNull
  @Schema(description = "car's drivers ids",
          example     = "[]")
  List<Long> driverIds;

  @Schema(description = "id of car's main driver",
          example     = "null")
  Long mainDriverId;

}
