package cz.muni.pa165.race.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.race.data.model.Race;
import cz.muni.pa165.race.data.model.Season;
import cz.muni.pa165.race.data.repository.RaceRepository;
import cz.muni.pa165.race.data.repository.SeasonRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests for season controller.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SeasonServiceTest {

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  private String bodyContent = """
          {
          "id": 1,
          "year": 2020,
          "races": []
        }
      """;
  private String expectedMessagePost = "{\"id\":1,\"year\":2023,\"races\":[]}";
  private String expectedMessageDelete = "Season with id 1 was successfully deleted.";
  private String expectedMessageGet = "{\"id\":1,\"year\":2023,\"races\":[]}";
  private String expectedMessageGetAll = "[{\"id\":1,\"year\":2023,\"races\":[]}]";
  private String expectedMessageAddRace = "{\"id\":1,\"year\":2023,\"races\":[{\""
      + "id\":1,\"name\":\"Monaco Grand Prix 2023\"}]}";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SeasonRepository seasonRepository;

  @MockBean
  private RaceRepository raceRepository;

  @MockBean
  private CommonDbGetter commonDbGetter;

  @BeforeEach
  void setup() {
    Season seasonDao = SeasonTestUtil.getDaoSeason();
    Race raceDao = RaceTestUtil.getDaoRace();
    given(seasonRepository.save(any(Season.class))).willReturn(
        seasonDao);
    given(seasonRepository.findById(anyLong())).willReturn(
        Optional.of(seasonDao));
    given(seasonRepository.findAll()).willReturn(List.of(seasonDao));
    given(raceRepository.findById(anyLong())).willReturn(
        Optional.of(raceDao));
  }


  @Test
  void createSeason() throws Exception {
    var request = post("/season/")
        .content(bodyContent)
        .contentType(MediaType.APPLICATION_JSON_VALUE);
    this.mockMvc.perform(request)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessagePost));
  }

  @Test
  void deleteRace() throws Exception {
    var requestDelete = delete("/season/")
        .param("seasonId", "1")
        .contentType(MediaType.APPLICATION_JSON_VALUE);
    this.mockMvc.perform(requestDelete)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessageDelete));
  }

  @Test
  void getExistingRace() throws Exception {
    var requestGet = get("/season/id")
        .param("seasonId", "1")
        .contentType(MediaType.APPLICATION_JSON_VALUE);
    this.mockMvc.perform(requestGet)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessageGet));
  }

  @Test
  void getAllRaces() throws Exception {
    var requestGet = get("/season/");
    this.mockMvc.perform(requestGet)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessageGetAll));
  }

  @Test
  void addRace() throws Exception {
    var requestPatch = patch("/season/addRace")
        .param("seasonId", "1")
        .param("raceId", "1");
    this.mockMvc.perform(requestPatch)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessageAddRace));
  }
}