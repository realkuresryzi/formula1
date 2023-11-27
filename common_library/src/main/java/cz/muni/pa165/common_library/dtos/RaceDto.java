package cz.muni.pa165.common_library.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto for race entity.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RaceDto {

  @Schema(description = "race id", example = "1")
  Long id;

  @NotNull
  @Schema(description = "race information")
  private RaceDto.RaceInfo raceInfo;

  @Schema(description = "driver one")
  private RaceDriverCarDto driverOne;

  @Schema(description = "driver two")
  private RaceDriverCarDto driverTwo;

  /**
   * Race information.
   */
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RaceInfo {

    @NotNull
    @Schema(description = "race location", example = "MONACO")
    private Location location;

    @NotNull
    @Schema(description = "race name", example = "Monaco Grand Prix 2023")
    private String name;

    @NotNull
    @Schema(description = "race prize pool", example = "30000000")
    private long prizePool;
  }

}
