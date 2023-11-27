package cz.muni.pa165.common_library.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto for race with id and name.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaceNameDto {


  @NotNull
  @Schema(description = "race id", example = "1")
  long id;

  @NotNull
  @Schema(description = "race name", example = "Monaco Grand Prix 2023")
  private String name;
}
