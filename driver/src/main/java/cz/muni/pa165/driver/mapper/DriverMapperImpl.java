package cz.muni.pa165.driver.mapper;

import cz.muni.pa165.common_library.dtos.DriverAddDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.driver.data.model.Driver;
import org.springframework.stereotype.Component;

/**
 * Implementation of driver mapper.
 */
@Component
public class DriverMapperImpl implements DriverMapper {

  /**
   * Maps driver class to driver insight dto.
   *
   * @param driver driver class
   * @return driver insight dto
   */
  @Override
  public DriverResponseDto convertToResponseDto(Driver driver) {
    return DriverResponseDto.builder()
        .id(driver.getId())
        .name(driver.getName())
        .surname(driver.getSurname())
        .nationality(driver.getNationality())
        .characteristics(driver.getCharacteristics())
        .build();
  }

  /**
   * Maps driver dto to driver class.
   *
   * @param driverAddDto driver dto
   * @return driver class
   */
  @Override
  public Driver convertToDriver(DriverAddDto driverAddDto) {
    var driver = new Driver(
        driverAddDto.getName(),
        driverAddDto.getSurname(),
        driverAddDto.getNationality()
    );

    // Overwrites default characteristics levels with new ones
    if (driverAddDto.getCharacteristics() != null) {
      driver.getCharacteristics().putAll(driverAddDto.getCharacteristics());
    }
    return driver;
  }
}
