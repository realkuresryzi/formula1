package cz.muni.pa165.car.service;

import static cz.muni.pa165.car.mapper.CarMapper.carConverterToDto;
import static cz.muni.pa165.car.mapper.CarMapper.carDtoConverter;

import cz.muni.pa165.car.data.model.Car;
import cz.muni.pa165.car.data.repository.CarRepository;
import cz.muni.pa165.car.mapper.CarMapper;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.common_library.dtos.CarRequestDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.dtos.RaceDto;
import cz.muni.pa165.common_library.exception.DatabaseException;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for car management.
 */
@Service
public class CarServiceImpl implements CarService {

  private final CarRepository carRepository;
  private final CommonDbGetter dbGetter;

  /**
   * Constructor for Car Service.
   *
   * @param carRepository Car repository
   */
  @Autowired
  public CarServiceImpl(CarRepository carRepository, CommonDbGetter dbGetter) {
    this.carRepository = carRepository;
    this.dbGetter = dbGetter;
  }

  @Override
  @Transactional
  public CarResponseDto postCar(CarRequestDto carRequestDto) {

    var componentIds = carRequestDto.getComponentIds();
    for (Long componentId : componentIds) {
      if (isComponentInUse(componentId)) {
        throw new DatabaseException("Component with id " + componentId + " is already in use.");
      }
      dbGetter.getComponentFromDb(componentId);
    }
    return carConverterToDto(carRepository.save(carDtoConverter(carRequestDto)));
  }

  @Override
  @Transactional(readOnly = true)
  public CarResponseDto getCarById(Long carId) {
    return carConverterToDto(carRepository.findById(carId).orElseThrow(
        () -> new DatabaseException(
            "Car with id" + carId + " was not found."
        )
    ));
  }

  @Override
  @Transactional(readOnly = true)
  public List<CarResponseDto> getAllCars() {
    return carRepository
        .findAll()
        .stream()
        .map(CarMapper::carConverterToDto)
        .toList();
  }

  @Override
  @Transactional
  public String deleteById(Long carId) {
    // search all races if the car is not being used.
    var response = dbGetter.getAllRaces();
    if (response.isPresent()) {
      List<RaceDto> races = response.get();
      for (RaceDto race : races) {
        if (race.getDriverOne().getCarId().equals(carId)) {
          throw new DatabaseException("Car with id " + carId
              + " cannot be removed as it is used by driver with id "
              + race.getDriverOne().getDriverId()
              + " in race with id " + race.getId());
        }
        if (race.getDriverTwo().getCarId().equals(carId)) {
          throw new DatabaseException("Car with id " + carId
              + " cannot be removed as it is used by driver with id "
              + race.getDriverTwo().getDriverId()
              + " in race with id " + race.getId());
        }
      }
    }
    carRepository.deleteById(carId);
    return "Car with id " + carId + " was successfully deleted.";
  }

  @Override
  public void fillTheDataBase() {
    // ID, set(components), set(drivers), mainDriverID
    Set<Long> firstCarComponents = new HashSet<>();
    Set<Long> firstCarDrivers = new HashSet<>();
    var firstCar = new Car(null,
        firstCarComponents,
        firstCarDrivers,
        null);
    Optional<Long> componentId = getRandomComponentId();
    componentId.ifPresent(id -> firstCar.setComponents(
        new HashSet<>(Collections.singleton(id))));
    carRepository.save(firstCar);

    Set<Long> secondCarComponents = new HashSet<>();
    Set<Long> secondCarDrivers = new HashSet<>();
    var secondCar = new Car(null,
        secondCarComponents,
        secondCarDrivers,
        null);
    componentId = getRandomComponentId();
    componentId.ifPresent(id -> secondCar.setComponents(
        new HashSet<>(Collections.singleton(id))));
    Optional<Long> driverId = getRandomDriverId();
    driverId.ifPresent(id -> secondCar.setDrivers(
        new HashSet<>(Collections.singleton(id))));
    carRepository.save(secondCar);
  }

  @Override
  public void clearDatabase() {
    carRepository.deleteAll();
  }

  private boolean isComponentInUse(Long componentId) {
    var cars = carRepository.findAll();

    for (Car car : cars) {
      if (car.getComponents().stream()
          .anyMatch(x -> x.longValue() == componentId)) {
        return true;
      }
    }
    return false;
  }

  private Optional<Long> getRandomComponentId() {
    var response = dbGetter.getAllComponentsFromDb();
    if (response.isEmpty() || response.get().isEmpty()) {
      dbGetter.fillComponentsFromDb();
    }
    response = dbGetter.getAllComponentsFromDb();
    List<CarComponentResponseDto> allComponents = response.get();
    if (!allComponents.isEmpty()) {
      Random random = new Random();
      int index =  random.ints(0,
          allComponents.size()).findFirst().getAsInt();
      return Optional.of(allComponents.get(index).getId());
    }
    return Optional.empty();
  }

  private Optional<Long> getRandomDriverId() {
    var response = dbGetter.getAllDriversFromDb();
    if (response.isEmpty() || response.get().isEmpty()) {
      dbGetter.fillDriversInDb();
    }
    response = dbGetter.getAllDriversFromDb();
    List<DriverResponseDto> allDrivers = response.get();
    if (!allDrivers.isEmpty()) {
      Random random = new Random();
      int index =  random.ints(0,
          allDrivers.size()).findFirst().getAsInt();
      return Optional.of(allDrivers.get(index).getId());
    }
    return  Optional.empty();
  }
}
