package cz.muni.pa165.race.rest;

import cz.muni.pa165.common_library.dtos.SeasonDto;
import cz.muni.pa165.race.data.model.Season;
import java.util.ArrayList;

/**
 * Utils for season tests.
 */
public class SeasonTestUtil {
  /**
   * Returns a created Season.
   *
   * @return a season.
   */
  public static Season getDaoSeason() {
    return Season.builder()
        .id(1L)
        .seasonYear(2023)
        .races(new ArrayList<>())
        .build();
  }

  /**
   * get dao season.
   *
   * @return dao season.
   */
  public static SeasonDto getDaoSeasonDto() {
    return SeasonDto.builder()
        .id(1L)
        .races(new ArrayList<>())
        .year(2023)
        .build();
  }
}
