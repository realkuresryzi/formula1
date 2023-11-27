package cz.muni.pa165.car.utils;

import cz.muni.pa165.car.data.model.Car;
import cz.muni.pa165.common_library.dtos.CarRequestDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import java.util.List;
import java.util.Set;

/**
 * This class provides utility methods for testing the Car entity and its related DTOs.
 */
public class CarTestUtils {

  /**
   * Creates a test car with null id, components set to [1L, 2L],
   * drivers set to [1L, 2L], and mainDriverId set to 1L.
   *
   * @return the test car object
   */
  public static Car getTestCar1() {
    return Car.builder()
        .id(null)
        .components(Set.of(1L, 2L))
        .drivers(Set.of(1L, 2L))
        .mainDriverId(1L)
        .build();
  }

  /**
   * Creates a test car with id set to 1L, components set to [1L, 2L],
   * drivers set to [1L, 2L], and mainDriverId set to 1L.
   *
   * @return the test car object
   */
  public static Car getTestCar2() {
    return Car.builder()
        .id(1L)
        .components(Set.of(1L, 2L))
        .drivers(Set.of(1L, 2L))
        .mainDriverId(1L)
        .build();
  }

  /**
   * Creates a test CarResponseDto object with id set to 1L,
   * componentIdsNames set to [1L, 2L], driverIdsNames set to [1L, 2L],
   * and mainDriverId set to 1L.
   *
   * @return the test CarResponseDto object
   */
  public static CarResponseDto getTestCarResponseDto() {
    return CarResponseDto.builder()
        .id(1L)
        .componentIdsNames(List.of(1L, 2L))
        .driverIdsNames(List.of(1L, 2L))
        .mainDriverId(1L)
        .build();
  }

  /**
   * Creates a test CarResponseDto object with id set to 1L,
   * componentIdsNames set to an empty list, driverIdsNames set to [1L],
   * and mainDriverId set to null.
   *
   * @return the test CarResponseDto object
   */
  public static CarResponseDto getTestCarResponseDto2() {
    return CarResponseDto.builder()
        .id(1L)
        .componentIdsNames(List.of())
        .driverIdsNames(List.of(1L))
        .mainDriverId(null)
        .build();
  }

  /**
   * Creates a test CarResponseDto object with id set to 2L,
   * componentIdsNames set to an empty list, driverIdsNames set to an empty list,
   * and mainDriverId set to null.
   *
   * @return the test CarResponseDto object
   */
  public static CarResponseDto getTestCarResponseDto3() {
    return CarResponseDto.builder()
        .id(2L)
        .componentIdsNames(List.of())
        .driverIdsNames(List.of())
        .mainDriverId(null)
        .build();
  }

  /**
   * Creates a test CarResponseDto object with id set to 3L,
   * componentIdsNames set to an empty list, driverIdsNames set to [1L, 2L],
   * and mainDriverId set to 1L.
   *
   * @return the test CarResponseDto object
   */
  public static CarResponseDto getTestCarResponseDto4() {
    return CarResponseDto.builder()
        .id(3L)
        .componentIdsNames(List.of())
        .driverIdsNames(List.of(1L, 2L))
        .mainDriverId(1L)
        .build();
  }

  /**
   * Creates a test CarResponseDto object with id set to 1L,
   * componentIdsNames set to an empty list, driverIdsNames set to an empty list,
   * and mainDriverId set to null.
   *
   * @return the test CarResponseDto object
   */
  public static CarResponseDto getTestCarResponseDto5() {
    return CarResponseDto.builder()
        .id(1L)
        .componentIdsNames(List.of())
        .driverIdsNames(List.of())
        .mainDriverId(null)
        .build();
  }

  /**
   * Creates a test CarRequestDto object with componentIds set to [1L, 2L],
   * driverIds set to [1L, 2L], and mainDriverId set to 1L.
   *
   * @return the test CarRequestDto object
   */
  public static CarRequestDto getTestCarRequestDto() {
    return CarRequestDto.builder()
        .componentIds(List.of(1L, 2L))
        .driverIds(List.of(1L, 2L))
        .mainDriverId(1L)
        .build();
  }

  /**
   * Creates a test CarRequestDto object with empty componentIds
   * and driverIds lists, and null mainDriverId.
   *
   * @return the test CarRequestDto object
   */
  public static CarRequestDto getTestCarRequestDto1() {
    return CarRequestDto.builder()
        .componentIds(List.of())
        .driverIds(List.of())
        .mainDriverId(null)
        .build();
  }
}
