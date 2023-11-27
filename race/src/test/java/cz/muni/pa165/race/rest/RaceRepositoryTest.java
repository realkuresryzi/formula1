package cz.muni.pa165.race.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.race.data.model.Race;
import cz.muni.pa165.race.data.repository.RaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

@DataJpaTest
class RaceRepositoryTest {

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @MockBean
  SecurityFilterChain securityFilterChain;

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private RaceRepository raceRepository;

  @Test
  public void saveTest() {
    Race race = RaceTestUtil.getDaoRace();
    Race saved = raceRepository.save(race);
    Assertions.assertEquals(race, saved);
    Assertions.assertEquals(entityManager.find(Race.class, 1L).getId(), 1);
  }

  @Test
  public void getSavedEntityTest() {
    Race race = RaceTestUtil.getDaoRace();
    Race saved = raceRepository.save(race);
    var savedId = saved.getId();
    Race found = raceRepository.findById(savedId).get();
    Assertions.assertAll(
        () -> assertEquals(found.getRaceInfo().getName(), race.getRaceInfo().getName()),
        () -> assertEquals(found.getRaceInfo().getLocation(), race.getRaceInfo().getLocation()),
        () -> assertEquals(found.getRaceInfo().getPrizePool(), race.getRaceInfo().getPrizePool()),
        () -> assertEquals(found.getDriver1().getDriverId(), race.getDriver1().getDriverId()),
        () -> assertEquals(found.getDriver1().getCarId(), race.getDriver1().getCarId()),
        () -> assertEquals(found.getDriver1().getFinalPosition(),
            race.getDriver1().getFinalPosition()),
        () -> assertEquals(found.getDriver2().getDriverId(), race.getDriver2().getDriverId()),
        () -> assertEquals(found.getDriver2().getCarId(), race.getDriver2().getCarId()),
        () -> assertEquals(found.getDriver2().getFinalPosition(),
            race.getDriver2().getFinalPosition()));
  }

}
