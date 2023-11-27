package cz.muni.pa165.car.mapper;

import cz.muni.pa165.car.data.model.Car;
import cz.muni.pa165.common_library.dtos.CarRequestDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class for converting Car object and its Data Transfer Objects.
 */
public class CarMapper {

  /**
   * Converts Dto object to Car object.
   *
   * @param carRequestDto Dto object of the car.
   * @return Car object.
   */
  public static Car carDtoConverter(CarRequestDto carRequestDto) {

    return Car.builder()
        .id(null)
        .components(new HashSet<>(carRequestDto.getComponentIds()))
        .drivers(new HashSet<>(carRequestDto.getDriverIds()))
        .mainDriverId(carRequestDto.getMainDriverId())
        .build();
  }

  /**
   * Converts Car object to its Dto.
   *
   * @param car Car object.
   * @return Dto object.
   */
  public static CarResponseDto carConverterToDto(Car car) {

    return CarResponseDto.builder()
        .id(car.getId())
        .componentIdsNames(new ArrayList<>(car.getComponents()))
        .driverIdsNames(new ArrayList<>(car.getDrivers()))
        .mainDriverId(car.getMainDriverId())
        .build();
  }


}
