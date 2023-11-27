package cz.muni.pa165.race.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.pa165.common_library.dtos.SeasonDto;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.race.data.repository.RaceRepository;
import cz.muni.pa165.race.data.repository.SeasonRepository;
import cz.muni.pa165.race.service.SeasonServiceI;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(SeasonController.class)
@ExtendWith(MockitoExtension.class)
class SeasonControllerTest {

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SeasonServiceI seasonServiceMock;

  @MockBean
  private SeasonRepository seasonRepository;

  @MockBean
  private RaceRepository raceRepository;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  void createSeason() throws Exception {
    var seasonDto = SeasonTestUtil.getDaoSeasonDto();
    given(seasonServiceMock.postSeason(seasonDto)).willReturn(seasonDto);

    String response = mockMvc.perform(post("/season/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(seasonDto)))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    SeasonDto seasonDtoResponse = objectMapper.readValue(response, SeasonDto.class);
    Assertions.assertEquals(seasonDtoResponse, seasonDto);
  }

  @Test
  void getSeason() throws Exception {
    var seasonDto = SeasonTestUtil.getDaoSeasonDto();
    given(seasonServiceMock.getSeasonById(1L)).willReturn(seasonDto);

    String response = mockMvc.perform(get("/season/id")
            .param("seasonId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    SeasonDto seasonDtoResponse = objectMapper.readValue(response, SeasonDto.class);
    Assertions.assertEquals(seasonDtoResponse, seasonDto);
  }

  @Test
  void getAllSeasons() throws Exception {
    var seasonDto = SeasonTestUtil.getDaoSeasonDto();
    given(seasonServiceMock.getAllSeasons()).willReturn(List.of(seasonDto));

    String response = mockMvc.perform(get("/season/")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    List<SeasonDto> seasonDtoResponse =
        List.of(objectMapper.readValue(response, SeasonDto[].class));
    Assertions.assertEquals(seasonDtoResponse, List.of(seasonDto));
  }

  @Test
  void deleteSeason() throws Exception {
    var seasonDto = SeasonTestUtil.getDaoSeasonDto();
    given(seasonServiceMock.deleteById(1L)).willReturn(
        "Season with id: " + 1L + "was succesfully deleted");

    String response = mockMvc.perform(delete("/season/")
            .param("seasonId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    Assertions.assertEquals(response, "Season with id: " + 1L + "was succesfully deleted");
  }

  @Test
  void addRace() throws Exception {
    var seasonDto = SeasonTestUtil.getDaoSeasonDto();
    given(seasonServiceMock.addRace(1L, 1L)).willReturn(seasonDto);

    String response = mockMvc.perform(patch("/season/addRace")
            .param("seasonId", String.valueOf(1L))
            .param("raceId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    SeasonDto seasonDtoResponse = objectMapper.readValue(response, SeasonDto.class);
    Assertions.assertEquals(seasonDtoResponse, seasonDto);
  }
}
