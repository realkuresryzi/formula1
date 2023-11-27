package cz.muni.pa165.race.service;

import cz.muni.pa165.common_library.dtos.SeasonDto;
import java.util.List;

/**
 * Interface for season service.
 */
public interface SeasonServiceI {

  SeasonDto postSeason(SeasonDto seasonDto);

  SeasonDto getSeasonById(Long seasonId);

  List<SeasonDto> getAllSeasons();

  String deleteById(Long seasonId);

  SeasonDto addRace(Long seasonId, Long raceId);

  SeasonDto removeRace(Long seasonId, Long raceId);

  void fillTheDataBase();

  void clearDatabase();
}
