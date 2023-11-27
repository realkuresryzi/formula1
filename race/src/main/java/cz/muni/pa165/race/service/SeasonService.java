package cz.muni.pa165.race.service;

import cz.muni.pa165.common_library.dtos.RaceNameDto;
import cz.muni.pa165.common_library.dtos.SeasonDto;
import cz.muni.pa165.common_library.exception.DatabaseException;
import cz.muni.pa165.race.data.model.Race;
import cz.muni.pa165.race.data.model.Season;
import cz.muni.pa165.race.data.repository.RaceRepository;
import cz.muni.pa165.race.data.repository.SeasonRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Season service.
 */
@Service
public class SeasonService implements SeasonServiceI {

  private final SeasonRepository seasonRepository;
  private final RaceRepository raceRepository;

  @Autowired
  SeasonService(SeasonRepository seasonRepository, RaceRepository raceRepository) {
    this.seasonRepository = seasonRepository;
    this.raceRepository = raceRepository;
  }

  /**
   * Calls repository to insert a season into the database.
   *
   * @param seasonDto season to insert.
   * @return inserted season.
   */
  @Transactional
  public SeasonDto postSeason(SeasonDto seasonDto) {
    seasonDto.setId(null);
    return seasonConverter(seasonRepository.save(seasonDtoConverter(seasonDto)));
  }

  /**
   * Calls repository to find a season by id in the database.
   *
   * @param seasonId season id.
   * @return found season.
   */
  @Transactional(readOnly = true)
  public SeasonDto getSeasonById(Long seasonId) {
    return seasonConverter(seasonRepository.findById(seasonId).orElseThrow(
        () -> new DatabaseException("Season with id " + seasonId + " was not found.")));
  }

  /**
   * Calls repository to find all season in the database.
   *
   * @return found seasons.
   */
  @Transactional(readOnly = true)
  public List<SeasonDto> getAllSeasons() {
    return seasonRepository.findAll()
        .stream()
        .map(this::seasonConverter)
        .toList();
  }

  /**
   * Calls repository to delete a season in the database.
   *
   * @param seasonId season id.
   * @return found season.
   */
  @Transactional
  public String deleteById(Long seasonId) {
    seasonRepository.deleteById(seasonId);
    return "Season with id " + seasonId + " was successfully deleted.";
  }

  /**
   * Calls repository to add a race into a season in the database.
   *
   * @param raceId   race id.
   * @param seasonId season id.
   * @return modified race.
   */
  public SeasonDto addRace(Long seasonId, Long raceId) {
    var season = seasonRepository.findById(seasonId)
        .orElseThrow(() -> new DatabaseException(
            "Season with id " + seasonId + " was not found."));

    var assignedRaces = getAllAssignedRaces();

    if (assignedRaces.contains(raceId)) {
      throw new ValidationException("Race already assigned to a season!");
    }

    var race = raceRepository.findById(raceId)
        .orElseThrow(() -> new DatabaseException(
            "Race with id " + raceId + " was not found."));
    season.getRaces().add(race);
    return seasonConverter(seasonRepository.save(season));
  }

  /**
   * Calls repository to add a race into a season in the database.
   *
   * @param raceId race id.
   * @param seasonId season id.
   * @return modified race.
   */
  public SeasonDto removeRace(Long seasonId, Long raceId) {
    var season = seasonRepository.findById(seasonId)
        .orElseThrow(() -> new DatabaseException(
            "Season with id " + seasonId + " was not found."));

    var race = raceRepository.findById(raceId)
        .orElseThrow(() -> new DatabaseException(
            "Race with id " + raceId + " was not found."));
    season.getRaces().remove(race);
    return seasonConverter(seasonRepository.save(season));
  }

  @Override
  public void fillTheDataBase() {
    int seasonYear = 2023;
    var races = raceRepository.findAll();
    Season season = new Season();
    var assignedRaces = getAllAssignedRaces();

    var racesToAdd = races.stream().filter(
        x -> !assignedRaces.contains(x.getId())).toList();

    if (!racesToAdd.isEmpty()) {
      season.setRaces(racesToAdd.subList(0, 1));
    } else {
      season.setRaces(racesToAdd);
    }
    season.setSeasonYear(seasonYear);
    seasonRepository.save(season);
  }


  @Override
  public void clearDatabase() {

    var assignedRaces = getAllAssignedRaces();
    if (!assignedRaces.isEmpty()) {
      throw new ValidationException("Cannot delete a race that is assigned to a season."
          + " Remove the race firstly with id: " + assignedRaces.stream().toList().get(0));
    }
    seasonRepository.deleteAll();
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

  private Season seasonDtoConverter(SeasonDto seasonDto) {
    return Season.builder()
        .id(seasonDto.getId())
        .seasonYear(seasonDto.getYear())
        .races(seasonDto.getRaces()
            .stream()
            .map(this::getRace)
            .toList())
        .build();
  }

  private SeasonDto seasonConverter(Season season) {
    return SeasonDto.builder()
        .id(season.getId())
        .year(season.getSeasonYear())
        .races(season.getRaces()
            .stream()
            .map(x -> RaceNameDto.builder()
                .name(x.getRaceInfo().getName())
                .id(x.getId())
                .build())
            .toList())
        .build();
  }

  private Race getRace(RaceNameDto raceNameDto) {
    return raceRepository.findById(raceNameDto.getId()).orElseThrow(
        () -> new DatabaseException("Race with id " + raceNameDto.getId() + " was not found."));
  }
}
