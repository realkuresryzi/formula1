package cz.muni.pa165.driver.service;

import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.dtos.DriverAddDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.dtos.DriverUpdateDto;
import cz.muni.pa165.common_library.exception.DatabaseException;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.driver.data.model.Driver;
import cz.muni.pa165.driver.data.repository.DriverRepository;
import cz.muni.pa165.driver.mapper.DriverMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of driver service.
 */
@Service
public class DriverServiceImpl implements DriverService {

  private final DriverRepository driverRepository;
  private final DriverMapper driverMapper;
  private final CommonDbGetter dbGetter;

  @Autowired
  DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper,
                    CommonDbGetter dbGetter) {
    this.driverRepository = driverRepository;
    this.driverMapper = driverMapper;
    this.dbGetter = dbGetter;
  }

  /**
   * Adds given driver to database.
   *
   * @param driverAddDto driver dto object
   * @return dto of added driver
   */
  @Transactional
  public DriverResponseDto addDriver(DriverAddDto driverAddDto) {
    var driver = driverMapper.convertToDriver(driverAddDto);
    Driver saved = driverRepository.save(driver);
    return driverMapper.convertToResponseDto(saved);
  }

  /**
   * Updates driver with given id by given data.
   *
   * @param id              driver id
   * @param driverUpdateDto data to be updated
   * @return dto of updated driver
   */
  @Transactional
  public DriverResponseDto updateDriverById(Long id, DriverUpdateDto driverUpdateDto) {
    var driver = driverRepository.findById(id)
        .orElseThrow(() -> new DatabaseException(
            String.format("Driver with id %s was not found.", id)));
    updateDriverAttributes(driver, driverUpdateDto);
    return driverMapper.convertToResponseDto(driverRepository.save(driver));
  }

  /**
   * Updates given driver attributes that were specified to be updated.
   *
   * @param driver          existing driver
   * @param driverUpdateDto driver update dto
   */
  private static void updateDriverAttributes(Driver driver, DriverUpdateDto driverUpdateDto) {
    if (driverUpdateDto.getName() != null) {
      driver.setName(driverUpdateDto.getName());
    }
    if (driverUpdateDto.getSurname() != null) {
      driver.setSurname(driverUpdateDto.getSurname());
    }
    if (driverUpdateDto.getNationality() != null) {
      driver.setNationality(driverUpdateDto.getNationality());
    }
    if (driverUpdateDto.getCharacteristics() != null) {
      driver.getCharacteristics().putAll(driverUpdateDto.getCharacteristics());
    }
  }

  /**
   * Removes driver with given id.
   *
   * @param id driver id
   * @return dto of removed driver
   */
  @Transactional
  public DriverResponseDto removeDriverById(Long id) {
    // search all cars if the component is not being used.
    var response = dbGetter.getAllCars();
    if (response.isPresent()) {
      List<CarResponseDto> cars = response.get();
      for (CarResponseDto car : cars) {
        if (car.getDriverIdsNames().contains(id)) {
          throw new DatabaseException("Driver with id " + id
              + " cannot be removed as it is assigned to the car with id " + car.getId());
        }
      }
    }
    var driver = driverRepository.findById(id)
        .orElseThrow(() -> new DatabaseException(
            String.format("Driver with id %s was not found.", id)));
    driverRepository.delete(driver);
    return driverMapper.convertToResponseDto(driver);
  }

  /**
   * Gets all stored drivers.
   *
   * @return all stored drivers dto list
   */
  @Transactional(readOnly = true)
  public List<DriverResponseDto> getAllDrivers() {
    System.out.println();
    return driverRepository.findAll()
        .stream()
        .map(driverMapper::convertToResponseDto)
        .collect(Collectors.toList());
  }

  /**
   * Gets driver with given id.
   *
   * @param id driver id
   * @return found driver dto if successful
   */
  @Transactional(readOnly = true)
  public DriverResponseDto getDriverById(Long id) {
    return driverMapper.convertToResponseDto(driverRepository.findById(id)
        .orElseThrow(() -> new DatabaseException(
            String.format("Driver with id %s was not found.", id))));
  }

  @Override
  public void fillTheDatabase() {
    // name, surname, nationality
    var charles = new Driver("Charles", "Leclerc", "Monegasque");
    var firstDriverChars = Map.of(
        "technical_knowledge", 2,
        "aggressiveness", 4,
        "reactions", 2,
        "fitness", 5,
        "teamwork", 3,
        "adaptability", 3);

    charles.setCharacteristics(firstDriverChars);

    var carlos = new Driver("Carlos", "Sainz Jr.", "Spanish");
    var secondDriverChars = Map.of(
        "technical_knowledge", 4,
        "aggressiveness", 2,
        "reactions", 3,
        "fitness", 4,
        "teamwork", 2,
        "adaptability", 1);
    carlos.setCharacteristics(secondDriverChars);

    var antonio = new Driver("Antonio", "Giovinazzi", "Italian");
    var thirdDriverChars = Map.of(
        "technical_knowledge", 5,
        "aggressiveness", 3,
        "reactions", 1,
        "fitness", 3,
        "teamwork", 4,
        "adaptability", 2);
    antonio.setCharacteristics(thirdDriverChars);

    var robert = new Driver("Robert", "Shwartzman", "Israeli");
    var fourthDriverChars = Map.of(
        "technical_knowledge", 3,
        "aggressiveness", 5,
        "reactions", 3,
        "fitness", 3,
        "teamwork", 5,
        "adaptability", 1);
    robert.setCharacteristics(fourthDriverChars);

    driverRepository.save(charles);
    driverRepository.save(carlos);
    driverRepository.save(antonio);
    driverRepository.save(robert);
  }

  @Override
  public void clearDatabase() {
    driverRepository.deleteAll();
  }
}

