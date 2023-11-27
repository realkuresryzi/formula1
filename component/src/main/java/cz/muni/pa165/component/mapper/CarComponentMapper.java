package cz.muni.pa165.component.mapper;

import cz.muni.pa165.common_library.dtos.CarComponentRequestDto;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.component.data.model.CarComponent;
import org.springframework.stereotype.Component;

/**
 * Car component mapper.
 */
@Component
public class CarComponentMapper {
  /**
   * Converts carComponentDto -> CarComponent.
   *
   * @param carComponentDto of a car component.
   * @return car component.
   */
  public CarComponent carComponentDtoConverterWithoutId(CarComponentRequestDto carComponentDto) {
    return CarComponent.builder()
        .id(null)
        .weight(carComponentDto.getWeight())
        .price(carComponentDto.getPrice())
        .manufacturer(carComponentDto.getManufacturer())
        .name(carComponentDto.getName())
        .build();
  }

  /**
   * Converts CarComponentDto -> CarComponent.
   *
   * @param carComponentDto with id.
   * @return car component.
   */
  public CarComponent carComponentDtoConverter(CarComponentResponseDto carComponentDto) {
    return CarComponent.builder()
        .id(carComponentDto.getId())
        .weight(carComponentDto.getWeight())
        .price(carComponentDto.getPrice())
        .manufacturer(carComponentDto.getManufacturer())
        .name(carComponentDto.getName())
        .build();
  }

  /**
   * Converts CarComponent -> CarComponentResponseDto.
   *
   * @param carComponent ...
   * @return response dto.
   */
  public CarComponentResponseDto carComponentConverter(CarComponent carComponent) {
    return CarComponentResponseDto.builder()
        .id(carComponent.getId())
        .weight(carComponent.getWeight())
        .price(carComponent.getPrice())
        .manufacturer(carComponent.getManufacturer())
        .name(carComponent.getName())
        .build();
  }
}
