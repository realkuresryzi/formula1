package cz.muni.pa165.car.service;

import cz.muni.pa165.common_library.dtos.CarRequestDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import java.util.List;

/**
 * Interface for car service with basic CRUD operations.
 */
public interface CarService {
  /**
   * Calls repository to insert a car into the database.
   *
   * @param carRequestDto Dto object of the car.
   * @return Dto object of inserted car.
   */
  CarResponseDto postCar(CarRequestDto carRequestDto);

  /**
   * Calls repository to get a specific car from the database.
   *
   * @param carId car id
   * @return Dto object of the car
   */
  CarResponseDto getCarById(Long carId);

  /**
   * Calls repository to get all cars in the database.
   *
   * @return list of Dto objects
   */
  List<CarResponseDto> getAllCars();

  /**
   * Calls repository to delete a car in the database.
   *
   * @param carId car id
   * @return result status
   */
  String deleteById(Long carId);

  void fillTheDataBase();

  void clearDatabase();
}
