package cz.muni.pa165.race.rest;

import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.dtos.DriverDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.dtos.Location;
import cz.muni.pa165.common_library.dtos.RaceDriverCarDto;
import cz.muni.pa165.common_library.dtos.RaceDto;
import cz.muni.pa165.race.data.model.Race;

/**
 * Util functions for race tests.
 */
public class RaceTestUtil {
  /**
   * Returns a created race.
   *
   * @return a race.
   */
  public static Race getDaoRace() {
    return Race.builder()
        .id(1L)
        .raceInfo(Race.RaceInfo.builder()
            .id(1L)
            .location(Location.MONACO)
            .name("Monaco Grand Prix 2023")
            .prizePool(30000000L).build())
        .driver1(Race.RaceDriverInfo.builder()
            .id(1L)
            .driverId(1L)
            .carId(1L)
            .finalPosition(1)
            .build())
        .driver2(Race.RaceDriverInfo.builder()
            .id(2L)
            .driverId(2L)
            .carId(2L)
            .finalPosition(2)
            .build())
        .build();
  }

  /**
   * Get dao race.
   *
   * @return dao race.
   */
  public static RaceDto getDaoRaceDto() {
    return RaceDto.builder()
        .id(1L)
        .raceInfo(RaceDto.RaceInfo.builder()
            .location(Location.MONACO)
            .name("Monaco Grand Prix 2023")
            .prizePool(30000000L).build())
        .driverOne(RaceDriverCarDto.builder()
            .driverId(1L)
            .carId(1L)
            .position(1)
            .build())
        .driverTwo(RaceDriverCarDto.builder()
            .driverId(2L)
            .carId(2L)
            .position(2)
            .build())
        .build();
  }

  public static CarResponseDto getCarDao() {
    return new CarResponseDto(1L, null, null, 1L);
  }

  /**
   * Get dao driver.
   *
   * @return dao driver.
   */
  public static DriverResponseDto getDriverDao() {
    var returnedDriver = new DriverResponseDto();
    returnedDriver.setId(1L);
    returnedDriver.setName("Name");
    returnedDriver.setSurname("Surname");
    return returnedDriver;
  }
}