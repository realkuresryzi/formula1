package cz.muni.pa165.race.service;

import cz.muni.pa165.common_library.dtos.Location;
import cz.muni.pa165.common_library.dtos.RaceDriverCarDto;
import cz.muni.pa165.common_library.dtos.RaceDto;
import cz.muni.pa165.common_library.exception.BadRequestException;
import cz.muni.pa165.common_library.exception.DatabaseException;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.race.data.model.Race;
import cz.muni.pa165.race.data.model.Season;
import cz.muni.pa165.race.data.repository.RaceRepository;
import cz.muni.pa165.race.data.repository.SeasonRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import javax.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing races.
 */
@Service
public class RaceService implements RaceServiceI {

  private final PointsUtil pointsUtil;
  private final SeasonRepository seasonRepository;
  private final RaceRepository raceRepository;
  private final CommonDbGetter dbGetter;

  @Autowired
  RaceService(RaceRepository raceRepository, SeasonRepository seasonRepository,
              CommonDbGetter dbGetter) {
    this.pointsUtil = new PointsUtil();
    this.raceRepository = raceRepository;
    this.seasonRepository = seasonRepository;
    this.dbGetter = dbGetter;
  }

  /**
   * Calls repository to insert a race into the database.
   *
   * @param raceDto race to insert.
   * @return inserted race.
   */
  @Transactional
  public RaceDto postRace(RaceDto raceDto) {
    raceDto.setId(null);

    if ((raceDto.getDriverOne() != null)
        && (raceDto.getDriverTwo() != null)) {

      if (raceDto.getDriverOne().getDriverId() != null
          && raceDto.getDriverTwo().getDriverId() != null) {

        if (Objects.equals(raceDto.getDriverOne().getCarId(),
            raceDto.getDriverTwo().getCarId())) {
          throw new DatabaseException("Cant assign same car to both drivers");
        }

        if (Objects.equals(raceDto.getDriverOne().getDriverId(),
            raceDto.getDriverTwo().getDriverId())) {
          throw new DatabaseException("Cant assign same driver to both drivers");
        }
      }
    }

    if (raceDto.getDriverOne() != null) {
      if (raceDto.getDriverOne().getPosition() != null
          && (raceDto.getDriverOne().getPosition() > 20
          || raceDto.getDriverOne().getPosition() < 1)) {
        throw new ValidationException("Final position can be set to max 20 and min 1");
      }

      dbGetter.getDriverFromDb(raceDto.getDriverOne().getDriverId()).orElseThrow(
          () -> new DatabaseException(
              "Driver with id " + raceDto.getDriverOne().getDriverId() + " was not found"));

      dbGetter.getCar(raceDto.getDriverOne().getCarId()).orElseThrow(
          () -> new DatabaseException(
              "Car with id " + raceDto.getDriverOne().getCarId() + " was not found"));
    }

    if (raceDto.getDriverTwo() != null) {

      if (raceDto.getDriverTwo().getPosition() != null
          && (raceDto.getDriverOne().getPosition() > 20
          || raceDto.getDriverTwo().getPosition() < 1)) {
        throw new ValidationException("Final position can be set to max 20 and min 1");
      }

      dbGetter.getDriverFromDb(raceDto.getDriverTwo().getDriverId()).orElseThrow(
          () -> new DatabaseException(
              "Driver with id " + raceDto.getDriverTwo().getDriverId() + " was not found"));

      dbGetter.getCar(raceDto.getDriverTwo().getCarId()).orElseThrow(
          () -> new DatabaseException(
              "Car with id " + raceDto.getDriverTwo().getCarId() + " was not found"));
    }

    return convertRace(raceRepository.save(convertRaceDto(raceDto)));
  }

  /**
   * Deletes the race from repository by id.
   *
   * @param raceId race id
   */
  @Transactional
  public String deleteRace(Long raceId) {
    // search all seasons if the race is not assigned.
    var seasons = seasonRepository.findAll();
    if (seasons.size() != 0) {
      for (Season season : seasons) {
        if (season.getRaces().stream().map(Race::getId).toList().contains(raceId)) {
          throw new DatabaseException("Race with id " + raceId
              + " cannot be removed as it is assigned in the season with id " + season.getId());
        }
      }
    }
    raceRepository.deleteById(raceId);
    return "Race with id " + raceId + " was successfully deleted.";
  }

  /**
   * Assigns driver one.
   */
  @Transactional
  public RaceDto assignDriverOne(Long driverId, Long raceId, Long carId) {
    var race = raceRepository.findById(raceId)
        .orElseThrow(() -> new DatabaseException("Race with id " + raceId + " was not found."));

    var driver = dbGetter.getDriverFromDb(driverId).orElseThrow(
        () -> new DatabaseException("Driver with id " + driverId + " was not found"));
    if (race.getDriver2() != null && Objects.equals(race.getDriver2().getDriverId(), driverId)) {
      throw new BadRequestException("Driver already assigned to the race as driver two.");
    }

    var car = dbGetter.getCar(carId).orElseThrow(
        () -> new DatabaseException("Car with id " + carId + " was not found"));
    if (race.getDriver2() != null && Objects.equals(race.getDriver2().getCarId(), carId)) {
      throw new BadRequestException("Car is already assigned to the race for driver two.");
    }
    race.getDriver1().setDriverId(driver.getId());
    race.getDriver1().setCarId(car.getId());
    return convertRace(raceRepository.save(race));
  }

  /**
   * Assigns driver two.
   */
  @Transactional
  public RaceDto assignDriverTwo(Long driverId, Long raceId, Long carId) {
    var race = raceRepository.findById(raceId)
        .orElseThrow(() -> new DatabaseException("Race with id " + raceId + " was not found."));

    var driver = dbGetter.getDriverFromDb(driverId).orElseThrow(
        () -> new DatabaseException("Driver with id " + driverId + " was not found"));
    if (race.getDriver1() != null && Objects.equals(race.getDriver1().getDriverId(), driverId)) {
      throw new BadRequestException("Driver already assigned to the race as driver one.");
    }
    var car = dbGetter.getCar(carId).orElseThrow(
        () -> new DatabaseException("Car with id " + carId + " was not found"));
    if (race.getDriver1() != null && Objects.equals(race.getDriver1().getCarId(), carId)) {
      throw new BadRequestException("Car is already assigned to the race for driver two.");
    }

    race.getDriver2().setDriverId(driver.getId());
    race.getDriver2().setCarId(car.getId());
    return convertRace(raceRepository.save(race));
  }

  /**
   * Finds race by id.
   *
   * @param raceId id of race.
   * @return found race.
   */
  @Transactional(readOnly = true)
  public RaceDto findRaceById(Long raceId) {
    return convertRace(raceRepository.findById(raceId).orElseThrow(
        () -> new DatabaseException("Race with id " + raceId + " was not found.")));
  }

  /**
   * Find all races.
   *
   * @return found races.
   */
  @Transactional(readOnly = true)
  public List<RaceDto> findRaces() {
    return raceRepository.findAll().stream().map(this::convertRace).toList();
  }

  /**
   * Assigns positions for driver number two.
   *
   * @param raceId   race id.
   * @param position position of driver two.
   * @return updated race.
   */
  @Transactional
  public RaceDto assignPositionForDriverTwo(Long raceId, Integer position) {
    var race =
        raceRepository.findById(raceId).orElseThrow(() -> new DatabaseException(
            "Race with id " + raceId + " was not found."));

    if (position > 20 || position < 1) {
      throw new ValidationException("Final position can be set to max 20 and min 1");
    }

    race.getDriver2().setFinalPosition(position);
    return convertRace(raceRepository.save(race));
  }

  /**
   * Assigns positions for driver number one.
   *
   * @param raceId   race id.
   * @param position position of driver one.
   * @return updated race.
   */
  @Transactional
  public RaceDto assignPositionForDriverOne(Long raceId, Integer position) {
    var race =
        raceRepository.findById(raceId).orElseThrow(() -> new DatabaseException(
            "Race with id " + raceId + " was not found."));

    if (position > 20 || position < 1) {
      throw new ValidationException("Final position can be set to max 20 and min 1");
    }
    race.getDriver1().setFinalPosition(position);
    return convertRace(raceRepository.save(race));
  }

  @Override
  public void fillTheDataBase() {
    // raceInfo, raceDriverInfo, raceDriverInfo
    var raceOneInfo = new Race.RaceInfo();
    raceOneInfo.setLocation(Location.MONACO);
    raceOneInfo.setPrizePool(5000000L);
    raceOneInfo.setName("Monaco Grand Prix 2023");
    var raceOneDriverOneInfo = new Race.RaceDriverInfo();
    var raceOneDriverTwoInfo = new Race.RaceDriverInfo();
    boolean couldSetupDrivers = false;
    couldSetupDrivers = setupDrivers(raceOneDriverOneInfo, raceOneDriverTwoInfo);
    if (couldSetupDrivers) {
      var firstRace = new Race();
      firstRace.setRaceInfo(raceOneInfo);
      firstRace.setDriver1(raceOneDriverOneInfo);
      firstRace.setDriver2(raceOneDriverTwoInfo);
      raceRepository.save(firstRace);
    }

    var raceTwoInfo = new Race.RaceInfo();
    raceTwoInfo.setLocation(Location.INTERLAGOS);
    raceTwoInfo.setPrizePool(6000000L);
    raceTwoInfo.setName("Brazilian Grand Prix 2023");
    var raceTwoDriverOneInfo = new Race.RaceDriverInfo();
    var raceTwoDriverTwoInfo = new Race.RaceDriverInfo();
    couldSetupDrivers = setupDrivers(raceTwoDriverOneInfo, raceTwoDriverTwoInfo);
    if (couldSetupDrivers) {
      var secondRace = new Race();
      secondRace.setRaceInfo(raceTwoInfo);
      secondRace.setDriver1(raceTwoDriverOneInfo);
      secondRace.setDriver2(raceTwoDriverTwoInfo);
      raceRepository.save(secondRace);
    }

    var raceThreeInfo = new Race.RaceInfo();
    raceThreeInfo.setLocation(Location.SILVERSTONE);
    raceThreeInfo.setPrizePool(400000L);
    raceThreeInfo.setName("British Grand Prix 2023");
    var raceThreeDriverOneInfo = new Race.RaceDriverInfo();
    var raceThreeDriverTwoInfo = new Race.RaceDriverInfo();
    setupDrivers(raceThreeDriverOneInfo, raceThreeDriverTwoInfo);
    var thirdRace = new Race();
    thirdRace.setRaceInfo(raceThreeInfo);
    thirdRace.setDriver1(raceThreeDriverOneInfo);
    thirdRace.setDriver2(raceThreeDriverTwoInfo);
    raceRepository.save(thirdRace);
  }

  @Override
  public void clearDatabase() {
    var assignedRaces = getAllAssignedRaces();
    if (!assignedRaces.isEmpty()) {
      throw new ValidationException("Cannot delete a race that is assigned to a season."
          + " Remove the race firstly with id: " + assignedRaces.stream().toList().get(0));
    }
    raceRepository.deleteAll();
  }

  /**
   * Finds most suitable drivers for given location.
   *
   * @param location location of the race.
   * @return set if ids of most suitable drivers for given location.
   */
  @Transactional(readOnly = true)
  public Set<Long> findMostSuitableDriver(Location location) {
    var races = raceRepository.findRacesByLocation(location);
    Map<Long, Integer> driverWithPoints = new HashMap<>();

    for (Race race : races) {
      updatePointsForDriver(race.getDriver1(), driverWithPoints);
      updatePointsForDriver(race.getDriver2(), driverWithPoints);
    }

    var max = Collections.max(driverWithPoints.values());
    Set<Long> drivers = new HashSet<>();
    for (Map.Entry<Long, Integer> entry : driverWithPoints.entrySet()) {
      if (Objects.equals(entry.getValue(), max)) {
        drivers.add(entry.getKey());
      }
    }
    return drivers;
  }

  private void updatePointsForDriver(Race.RaceDriverInfo driverInfo,
                                     Map<Long, Integer> driverWithPoints) {
    if (driverInfo == null || driverInfo.getDriverId() == null) {
      return;
    }

    Integer points;
    if (driverInfo.getFinalPosition() != null) {
      points = pointsUtil.points.get(driverInfo.getFinalPosition());
    } else {
      points = 0;
    }

    driverWithPoints.merge(driverInfo.getDriverId(), points, Integer::sum);
  }

  private Set<Long> getAllAssignedRaces() {
    var seasons = seasonRepository.findAll();

    var racesToAdd = new HashSet<Long>();
    for (Season season : seasons) {
      for (Race race : season.getRaces()) {
        racesToAdd.add(race.getId());
      }
    }
    return racesToAdd;
  }

  private Race convertRaceDto(RaceDto raceDto) {
    var race = Race.builder()
        .id(raceDto.getId())
        .build();

    var raceInfo = Race.RaceInfo
        .builder()
        .id(raceDto.getId())
        .name(raceDto.getRaceInfo().getName())
        .location(raceDto.getRaceInfo().getLocation())
        .prizePool(raceDto.getRaceInfo().getPrizePool())
        .build();
    race.setRaceInfo(raceInfo);

    if (raceDto.getDriverOne() != null) {
      race.setDriver1(Race.RaceDriverInfo
          .builder()
          .driverId(raceDto.getDriverOne().getDriverId())
          .carId(raceDto.getDriverOne().getCarId())
          .finalPosition(raceDto.getDriverOne().getPosition())
          .build());
    } else {
      race.setDriver1(Race.RaceDriverInfo.builder().build());
    }

    if (raceDto.getDriverTwo() != null) {
      race.setDriver2(Race.RaceDriverInfo
          .builder()
          .driverId(raceDto.getDriverTwo().getDriverId())
          .carId(raceDto.getDriverTwo().getCarId())
          .finalPosition(raceDto.getDriverTwo().getPosition())
          .build());
    } else {
      race.setDriver2(Race.RaceDriverInfo.builder().build());
    }

    return race;
  }

  private RaceDto convertRace(Race race) {
    var raceDto = RaceDto.builder()
        .id(race.getId())
        .raceInfo(RaceDto.RaceInfo.builder()
            .name(race.getRaceInfo().getName())
            .location(race.getRaceInfo().getLocation())
            .prizePool(race.getRaceInfo().getPrizePool())
            .build())
        .build();
    if (race.getDriver1() != null) {
      raceDto.setDriverOne(RaceDriverCarDto.builder()
          .carId(race.getDriver1().getCarId())
          .driverId(race.getDriver1().getDriverId())
          .position(race.getDriver1().getFinalPosition())
          .build());
    }
    if (race.getDriver2() != null) {
      raceDto.setDriverTwo(RaceDriverCarDto.builder()
          .carId(race.getDriver2().getCarId())
          .driverId(race.getDriver2().getDriverId())
          .position(race.getDriver2().getFinalPosition())
          .build());
    }
    return raceDto;
  }

  /**
   * Will randomly setup drivers for a race. If there are not at least two drivers
   * in the database, will return false.
   * If there are not at least two cars that can be assigned to the drivers,
   * will return false.
   * Will get random driver and random car which will be assigned to the driver.
   *
   * @param driverOne first driver to setup
   * @param driverTwo second driver to setup
   * @return true if drivers were set, false otherwise.
   */
  private boolean setupDrivers(Race.RaceDriverInfo driverOne, Race.RaceDriverInfo driverTwo) {
    Random random = new Random();
    // get drivers
    var driversResponse = dbGetter.getAllDriversFromDb();
    if (driversResponse.isEmpty() || driversResponse.get().isEmpty()) {
      dbGetter.fillDriversInDb();
      driversResponse = dbGetter.getAllDriversFromDb();
    }
    var drivers = driversResponse.get();
    int numDrivers = drivers.size();
    if (numDrivers < 2) {
      return false;
    }
    int objectOneIndex;
    int objectTwoIndex;
    objectOneIndex = random.nextInt(0, numDrivers);
    var driverOneFromDb = drivers.get(objectOneIndex);
    objectTwoIndex = random.nextInt(0, numDrivers);
    while (objectTwoIndex == objectOneIndex) {
      objectTwoIndex = random.nextInt(0, numDrivers);
    }
    var driverTwoFromDb = drivers.get(objectTwoIndex);
    driverOne.setDriverId(driverOneFromDb.getId());
    driverTwo.setDriverId(driverTwoFromDb.getId());

    // get cars
    var carsResponse = dbGetter.getAllCars();
    if (carsResponse.isEmpty() || carsResponse.get().isEmpty()) {
      dbGetter.fillCarsInDb();
      carsResponse = dbGetter.getAllCars();
    }
    var cars = carsResponse.get();
    int numCars = cars.size();
    if (numCars < 2) {
      return false;
    }
    objectOneIndex = random.nextInt(0, numCars);
    var carOneFromDb = cars.get(objectOneIndex);
    objectTwoIndex = random.nextInt(0, numCars);
    while (objectTwoIndex == objectOneIndex) {
      objectTwoIndex = random.nextInt(0, numCars);
    }
    var carTwoFromDb = cars.get(objectTwoIndex);
    driverOne.setCarId(carOneFromDb.getId());
    driverTwo.setCarId(carTwoFromDb.getId());

    driverOne.setFinalPosition(1);
    driverTwo.setFinalPosition(2);

    return true;
  }

}
