package cz.muni.pa165.common_library.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer object for CarComponent class.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarComponentResponseDto {

  @NotNull
  @Schema(description = "component id", example = "1")
  Long id;

  @NotNull
  @Schema(description = "component weight", example = "50")
  BigDecimal weight;

  @NotNull
  @Schema(description = "component price", example = "24000")
  BigDecimal price;

  @NotNull
  @Schema(description = "name of the manufacturer", example = "Michellin")
  String manufacturer;

  @NotNull
  @Schema(description = "component name", example = "Default engine")
  String name;

}

