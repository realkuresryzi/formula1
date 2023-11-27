package cz.muni.pa165.driver.service;

import cz.muni.pa165.common_library.dtos.DriverAddDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.dtos.DriverUpdateDto;
import java.util.List;

/**
 * Driver service interface.
 */
public interface DriverService {

  /**
   * Adds given driver to database.
   *
   * @param driverAddDto driver dto object
   * @return dto of added driver
   */
  DriverResponseDto addDriver(DriverAddDto driverAddDto);

  /**
   * Updates driver with given id by given data.
   *
   * @param id        driver id
   * @param driverDto data to be updated
   * @return dto of updated driver
   */
  DriverResponseDto updateDriverById(Long id, DriverUpdateDto driverDto);

  /**
   * Removes driver with given id.
   *
   * @param id driver id
   * @return dto of removed driver
   */
  DriverResponseDto removeDriverById(Long id);

  /**
   * Gets all stored drivers.
   *
   * @return all stored drivers dto list
   */
  List<DriverResponseDto> getAllDrivers();

  /**
   * Gets driver with given id.
   *
   * @param id driver id
   * @return found driver dto if successful
   */
  DriverResponseDto getDriverById(Long id);

  void fillTheDatabase();

  void clearDatabase();
}
