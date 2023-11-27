package cz.muni.pa165.car.utils;

import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import java.math.BigDecimal;

/**
 * This class provides utility methods for creating test data for car components.
 */
public class ComponentTestUtils {

  /**
   * Returns a test car component with id 1.
   *
   * @return a test car component with id 1
   */
  public static CarComponentResponseDto getTestComponent1() {
    return CarComponentResponseDto.builder()
        .id(1L)
        .name("")
        .weight(BigDecimal.TEN)
        .manufacturer("")
        .price(BigDecimal.TEN)
        .build();
  }

  /**
   * Returns a test car component with id 2.
   *
   * @return a test car component with id 2
   */
  public static CarComponentResponseDto getTestComponent2() {
    return CarComponentResponseDto.builder()
        .id(2L)
        .name("")
        .manufacturer("")
        .price(BigDecimal.TEN)
        .weight(BigDecimal.TEN)
        .build();
  }
}
