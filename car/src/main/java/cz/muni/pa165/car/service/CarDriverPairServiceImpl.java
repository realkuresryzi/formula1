package cz.muni.pa165.car.service;

import cz.muni.pa165.car.data.repository.CarRepository;
import cz.muni.pa165.car.mapper.CarMapper;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.dtos.DriverDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
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
 * Service for Driver Manager.
 */
@Service
public class CarDriverPairServiceImpl implements CarDriverPairService {

  private final CarRepository carRepository;
  private final CommonDbGetter dbGetter;

  /**
   * Constructor for Car - Driver Service.
   *
   * @param carRepository Car repository
   */
  @Autowired
  public CarDriverPairServiceImpl(CarRepository carRepository, CommonDbGetter dbGetter) {
    this.carRepository = carRepository;
    this.dbGetter = dbGetter;
  }

  @Override
  @Transactional
  public CarResponseDto assignDriverToCar(Long driverId, Long carId) {
    var car = carRepository.findById(carId).orElseThrow(
        () -> new DatabaseException("Car with id " + carId + " was not found."));

    var drivers = car.getDrivers();
    var savedDriver = dbGetter.getDriverFromDb(driverId).orElseThrow(
        () -> new DatabaseException("Driver with id " + driverId + " was not found"));

    drivers.add(savedDriver.getId());
    car.setDrivers(drivers);

    carRepository.save(car);
    return CarMapper.carConverterToDto(car);
  }

  @Override
  @Transactional
  public CarResponseDto unassignDriverFromCar(Long driverId, Long carId) {
    var car = carRepository.findById(carId).orElseThrow(
        () -> new DatabaseException("Car with id " + carId + " was not found."));
    var drivers = new HashSet<Long>();
    for (Long id : car.getDrivers()) {
      if (!Objects.equals(id, driverId)) {
        drivers.add(id);
      }
    }
    car.setDrivers(drivers);
    carRepository.save(car);
    return CarMapper.carConverterToDto(car);
  }

  @Override
  public List<DriverDto> getAllDriversOfCar(Long carId) {
    var car = carRepository.findById(carId).orElseThrow(
        () -> new DatabaseException("Car with id " + carId + " was not found."));
    var driverDtos = new ArrayList<DriverDto>();
    for (Long id : car.getDrivers()) {
      DriverResponseDto driver = dbGetter.getDriverFromDb(id).orElseThrow(
          () -> new DatabaseException("Driver with id " + id + " was not found."));
      driverDtos.add(
          new DriverDto(driver.getId(),
              driver.getName(),
              driver.getSurname())
      );
    }
    return driverDtos;
  }

  @Override
  @Transactional
  public CarResponseDto setMainDriver(Long carId, Long driverId) {
    var car = carRepository.findById(carId).orElseThrow(
        () -> new DatabaseException("Car with id " + carId + " was not found."));

    var savedDriver = dbGetter.getDriverFromDb(driverId).orElseThrow(
        () -> new DatabaseException("Driver with id " + driverId + "was not found."));

    car.setMainDriverId(savedDriver.getId());
    var drivers = car.getDrivers();
    drivers.add(savedDriver.getId());
    car.setDrivers(drivers);
    carRepository.save(car);
    return CarMapper.carConverterToDto(car);
  }

  @Override
  @Transactional
  public CarResponseDto removeMainDriver(Long carId) {
    var car = carRepository.findById(carId).orElseThrow(
        () -> new DatabaseException("Car with id " + carId + " was not found."));
    car.setMainDriverId(null);
    carRepository.save(car);
    return CarMapper.carConverterToDto(car);
  }

}
