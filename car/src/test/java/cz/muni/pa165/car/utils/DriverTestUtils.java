package cz.muni.pa165.car.utils;

import cz.muni.pa165.common_library.dtos.DriverDto;

/**
 * A utility class for creating instances of DriverDto for testing purposes.
 */
public class DriverTestUtils {

  /**
   * Returns a DriverDto object with pre-defined values for testing purposes.
   *
   * @return a DriverDto object
   */
  public static DriverDto getDriverDto1() {
    return DriverDto.builder()
        .id(1L)
        .name("Fernando")
        .surname("Alonso")
        .build();
  }

  /**
   * Returns a DriverDto object with pre-defined values for testing purposes.
   *
   * @return a DriverDto object
   */
  public static DriverDto getDriverDto2() {
    return DriverDto.builder()
        .id(2L)
        .name("Max")
        .surname("Verstappen")
        .build();
  }
}
