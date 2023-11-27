package cz.muni.pa165.car.service;

import cz.muni.pa165.common_library.dtos.CarRequestDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.dtos.DriverDto;
import java.util.List;

/**
 * Interface fot assign operations of drivers and cars.
 */
public interface CarDriverPairService {
  /**
   * Calls repository to assign a driver to a car, if unsuccessful, throws exception.
   *
   * @param driverId Id of the diver.
   * @param carId    Id of the car.
   * @return Car object that the driver was assigned to.
   */
  CarResponseDto assignDriverToCar(Long driverId, Long carId);

  /**
   * Calls repository to unassign a driver from a car, if unsuccessful, throws exception.
   *
   * @param driverId Id of the diver.
   * @param carId    Id of the car.
   * @return Car object that the driver was unassigned from.
   */
  CarResponseDto unassignDriverFromCar(Long driverId, Long carId);

  /**
   * Calls repository to return list of all drivers of specific car.
   *
   * @param carId Id of the car.
   * @return List of all drivers assigned to the car.
   */
  List<DriverDto> getAllDriversOfCar(Long carId);

  /**
   * Sets the main driver of the car identified by the given ID.
   *
   * @param carId    The ID of the car to set the main driver for.
   * @param driverId The ID of the driver to set as the main driver.
   * @return         A CarDto object representing the updated car.
   */
  CarResponseDto setMainDriver(Long carId, Long driverId);

  /**
   * Removes the main driver from the car identified by the given ID.
   *
   * @param carId The ID of the car to remove the main driver from.
   * @return      A CarDto object representing the updated car.
   */
  CarResponseDto removeMainDriver(Long carId);
}
