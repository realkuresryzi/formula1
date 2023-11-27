package cz.muni.pa165.car.service;

import cz.muni.pa165.car.data.model.Car;
import cz.muni.pa165.car.data.repository.CarRepository;
import cz.muni.pa165.car.mapper.CarMapper;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.exception.DatabaseException;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for manipulation with car's components.
 */
@Service
public class CarComponentPairServiceImpl implements CarComponentPairService {

  private final CarRepository carRepository;
  private final CommonDbGetter dbGetter;

  /**
   * Constructor for Car - Car Component Service.
   *
   * @param carRepository Car repository
   */
  @Autowired
  public CarComponentPairServiceImpl(CarRepository carRepository, CommonDbGetter dbGetter) {
    this.carRepository = carRepository;
    this.dbGetter = dbGetter;
  }

  @Override
  @Transactional
  public CarResponseDto addComponent(Long componentId, Long carId) {
    var car = carRepository.findById(carId).orElseThrow(
        () -> new DatabaseException("Car with id " + carId + " was not found"));

    if (isComponentInUse(componentId, carId)) {
      throw new DatabaseException("Component with id " + componentId + " is already in use");
    }
    var components = car.getComponents();
    CarComponentResponseDto carComponent = dbGetter.getComponentFromDb(componentId)
        .orElseThrow(() -> new DatabaseException(
            "Component with id " + componentId + " was not found"));

    components.add(carComponent.getId());
    car.setComponents(components);
    carRepository.save(car);
    return CarMapper.carConverterToDto(car);
  }

  @Override
  public CarResponseDto removeComponent(Long componentId, Long carId) {
    var car = carRepository.findById(carId).orElseThrow(
        () -> new DatabaseException("Car with id " + carId + " was not found."));
    var components = new HashSet<Long>();
    for (Long id : car.getComponents()) {
      if (!Objects.equals(id, componentId)) {
        components.add(id);
      }
    }
    car.setComponents(components);
    carRepository.save(car);
    return CarMapper.carConverterToDto(car);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CarComponentResponseDto> getAllComponentsOfCar(Long carId) {
    var car = carRepository.findById(carId).orElseThrow(
        () -> new DatabaseException("Car with id " + carId + " was not found."));
    var componentDtos = new ArrayList<CarComponentResponseDto>();
    for (Long id : car.getComponents()) {
      CarComponentResponseDto carComponent = dbGetter.getComponentFromDb(id)
          .orElseThrow(() -> new DatabaseException("Component with id " + id + " was not found."));
      componentDtos.add(
          new CarComponentResponseDto(
              carComponent.getId(),
              carComponent.getWeight(),
              carComponent.getPrice(),
              carComponent.getManufacturer(),
              carComponent.getName()
          )
      );
    }
    return componentDtos;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CarComponentResponseDto> getAllSpareCarComponents(Long carId, Long componentId) {
    // get all the components of a given car
    var carComponents = getAllComponentsOfCar(carId);
    if (carComponents == null) {
      throw new DatabaseException("Car with id " + carId
          + " does not have any components assigned.");
    }
    // check if the given component is in the car
    boolean found = false;
    for (var carComponent : carComponents) {
      if (carComponent.getId().equals(componentId)) {
        found = true;
        break;
      }
    }
    if (!found) {
      throw new DatabaseException("Car with id " + carId + " does not have component with id "
      + componentId + " assigned to it.");
    }
    // get all the components from DB
    var response = dbGetter.getAllComponentsFromDb();
    if (response.isEmpty()) {
      throw new DatabaseException("Components could not be fetched from database.");
    }
    List<CarComponentResponseDto> allComponents = response.get();
    CarComponentResponseDto requestedComponent = null;
    // check if the component is in the DB
    for (CarComponentResponseDto carComponent : allComponents) {
      if (carComponent.getId().equals(componentId)) {
        requestedComponent = carComponent;
        break;
      }
    }
    if (requestedComponent == null) {
      return new ArrayList<>();
    }
    // get all the viable replacements
    List<CarComponentResponseDto> allViableReplacements = new ArrayList<>();
    for (CarComponentResponseDto carComponent : allComponents) {
      if (carComponent.getName().equals(requestedComponent.getName())
          && !carComponent.getId().equals(componentId)) {
        allViableReplacements.add(carComponent);
      }
    }
    return allViableReplacements;
  }

  private boolean isComponentInUse(Long componentId, Long carId) {
    var cars = carRepository.findAll();

    for (Car car : cars) {
      if (car.getComponents().stream()
          .anyMatch(x -> x.longValue() == componentId && !Objects.equals(car.getId(), carId))) {
        return true;
      }
    }
    return false;
  }
}
