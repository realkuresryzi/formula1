package cz.muni.pa165.component.util;

import cz.muni.pa165.common_library.dtos.CarComponentRequestDto;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.component.data.model.CarComponent;
import java.math.BigDecimal;

/**
 * Helper class for creating Component objects.
 */
public class ComponentTestUtil {

  /**
   * Returns a CarComponent instance initialized with the given parameters.
   *
   * @param id the ID of the car component
   * @param name the name of the car component
   * @param decimal the weight and price of the car component
   * @param manufacturer the manufacturer of the car component
   * @return a CarComponent instance initialized with the given parameters
   */
  public static CarComponent getComponent(Long id,
                                          String name,
                                          BigDecimal decimal,
                                          String manufacturer) {
    return CarComponent.builder()
        .id(id)
        .name(name)
        .weight(decimal)
        .price(decimal)
        .manufacturer(manufacturer)
        .build();
  }

  /**
   * Returns a CarComponentResponseDto instance initialized with the given parameters.
   *
   * @param id the ID of the car component
   * @param name the name of the car component
   * @param decimal the weight and price of the car component
   * @param manufacturer the manufacturer of the car component
   * @return a CarComponentResponseDto instance initialized with the given parameters
   */
  public static CarComponentResponseDto getComponentResponseDto(Long id,
                                                                String name,
                                                                BigDecimal decimal,
                                                                String manufacturer) {
    return CarComponentResponseDto.builder()
        .id(id)
        .name(name)
        .weight(decimal)
        .price(decimal)
        .manufacturer(manufacturer)
        .build();
  }

  /**
   * Returns a CarComponentResponseDto instance initialized with the given parameters.
   *
   * @param name the name of the car component
   * @param decimal the weight and price of the car component
   * @param manufacturer the manufacturer of the car component
   * @return a CarComponentResponseDto instance initialized with the given parameters
   */
  public static CarComponentRequestDto getComponentRequestDto(String name,
                                                              BigDecimal decimal,
                                                              String manufacturer) {
    return CarComponentRequestDto.builder()
        .name(name)
        .weight(decimal)
        .price(decimal)
        .manufacturer(manufacturer)
        .build();
  }

}
