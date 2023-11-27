package cz.muni.pa165.race.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.race.data.model.Season;
import cz.muni.pa165.race.data.repository.SeasonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;

@DataJpaTest
class SeasonRepositoryTest {

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @MockBean
  SecurityFilterChain securityFilterChain;

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private SeasonRepository seasonRepository;

  @Test
  public void saveTest() {
    Season season = SeasonTestUtil.getDaoSeason();
    Season saved = seasonRepository.save(season);
    Assertions.assertEquals(season, saved);
    Assertions.assertEquals(entityManager.find(Season.class, 1L).getId(), 1);
  }

  @Test
  public void getSavedEntityTest() {
    Season season = SeasonTestUtil.getDaoSeason();
    Season saved = seasonRepository.save(season);
    var savedId = saved.getId();
    Season found = seasonRepository.findById(savedId).get();
    Assertions.assertAll(
        () -> assertEquals(found.getSeasonYear(), season.getSeasonYear())
    );
  }
}
