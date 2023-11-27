package cz.muni.pa165.driver.mapper;

import cz.muni.pa165.common_library.dtos.DriverAddDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.driver.data.model.Driver;

/**
 * Interface for driver mapper.
 */
public interface DriverMapper {

  /**
   * Maps driver class to driver response dto.
   *
   * @param driver driver class
   * @return driver response dto
   */
  DriverResponseDto convertToResponseDto(Driver driver);

  /**
   * Maps driver dto to driver class.
   *
   * @param driverAddDto driver dto
   * @return driver class
   */
  Driver convertToDriver(DriverAddDto driverAddDto);
}
