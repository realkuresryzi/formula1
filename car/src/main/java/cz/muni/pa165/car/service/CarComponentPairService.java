package cz.muni.pa165.car.service;

import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import java.util.List;

/**
 * Interface for manipulation with car's components.
 */
public interface CarComponentPairService {
  /**
   * Calls repository to add a component to a car.
   *
   * @param componentId Id of the component.
   * @param carId       Id of the car.
   * @return Car with added component.
   */
  CarResponseDto addComponent(Long componentId, Long carId);

  /**
   * Calls repository to remove a component from a car.
   *
   * @param componentId Id of the component.
   * @param carId       Id of the car.
   * @return Car with removed component.
   */
  CarResponseDto removeComponent(Long componentId, Long carId);

  /**
   * Calls repository ond returns oll components of specific car.
   *
   * @param carId Id of the car.
   * @return Components of the cat.
   */
  List<CarComponentResponseDto> getAllComponentsOfCar(Long carId);

  /**
   * Calls car component repository to retrieve all the components with the same name
   * as the component given by the id.
   *
   * @param carId if a car.
   * @param componentId of a car's component.
   * @return list of all the spare components.
   */
  List<CarComponentResponseDto> getAllSpareCarComponents(Long carId, Long componentId);
}
