package cz.muni.pa165.common_library.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Dto for season.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonDto {

  @Schema(description = "season id", example = "1")
  Long id;

  @NotNull
  @Min(1946)
  @Max(9999)
  @Schema(description = "season year", example = "2020")
  private int year;

  @NotNull
  @Schema(description = "season races")
  List<RaceNameDto> races;
}
