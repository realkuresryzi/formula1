package cz.muni.pa165.race.data.repository;

import cz.muni.pa165.common_library.dtos.Location;
import cz.muni.pa165.race.data.model.Race;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Race repository.
 */
@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {

  @Query("SELECT r FROM Race r"
      + " JOIN r.raceInfo ri "
      + " JOIN r.driver1 do "
      + " JOIN r.driver2 dt "
      + "WHERE ri.location = :location AND (do.id = :driverId OR dt.id = :driverId)")
  List<Race> findAllRacesOfDriverInLocation(@Param("location") Location raceLocation,
                                           @Param("driverId") long driverId);

  @Query("SELECT r FROM Race r"
      + " JOIN r.raceInfo ri "
      + "WHERE ri.location = :location")
  List<Race> findRacesByLocation(@Param("location") Location raceLocation);
}
