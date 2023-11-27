package cz.muni.pa165.race.service;

import cz.muni.pa165.common_library.dtos.Location;
import cz.muni.pa165.common_library.dtos.RaceDto;
import java.util.List;
import java.util.Set;

/**
 * Race service interface.
 */
public interface RaceServiceI {

  RaceDto postRace(RaceDto raceDto);

  RaceDto findRaceById(Long raceId);

  List<RaceDto> findRaces();

  String deleteRace(Long raceId);

  RaceDto assignDriverOne(Long driverId, Long raceId, Long carId);

  RaceDto assignDriverTwo(Long driverId, Long raceId, Long carId);

  Set<Long> findMostSuitableDriver(Location location);

  RaceDto assignPositionForDriverTwo(Long raceId, Integer position);

  RaceDto assignPositionForDriverOne(Long raceId, Integer position);

  void fillTheDataBase();

  void clearDatabase();
}
