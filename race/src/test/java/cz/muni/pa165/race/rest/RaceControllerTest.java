package cz.muni.pa165.race.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.pa165.common_library.dtos.Location;
import cz.muni.pa165.common_library.dtos.RaceDto;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.race.data.repository.RaceRepository;
import cz.muni.pa165.race.data.repository.SeasonRepository;
import cz.muni.pa165.race.service.RaceServiceI;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RaceController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class RaceControllerTest {

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RaceServiceI raceServiceMock;

  @MockBean
  private SeasonRepository seasonRepository;

  @MockBean
  private RaceRepository raceRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void initialize() {
  }

  @Test
  void createRace() throws Exception {
    var raceDto = RaceTestUtil.getDaoRaceDto();
    given(raceServiceMock.postRace(raceDto)).willReturn(raceDto);

    String response = mockMvc.perform(post("/race/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(raceDto)))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    RaceDto raceDtoResponse = objectMapper.readValue(response, RaceDto.class);
    Assertions.assertEquals(raceDtoResponse, raceDto);
  }

  @Test
  void deleteRace() throws Exception {
    var raceDto = RaceTestUtil.getDaoRaceDto();
    given(raceServiceMock.deleteRace(1L)).willReturn(
        "Race with id: " + 1L + "was succesfully deleted");

    String response = mockMvc.perform(delete("/race/")
            .param("raceId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    Assertions.assertEquals(response, "Race with id: " + 1L + "was succesfully deleted");
  }

  @Test
  void getRace() throws Exception {
    var raceDto = RaceTestUtil.getDaoRaceDto();
    given(raceServiceMock.findRaceById(1L)).willReturn(raceDto);

    String response = mockMvc.perform(get("/race/id")
            .param("raceId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    RaceDto raceDtoResponse = objectMapper.readValue(response, RaceDto.class);
    Assertions.assertEquals(raceDtoResponse, raceDto);
  }


  @Test
  void getRaces() throws Exception {
    var raceDto = RaceTestUtil.getDaoRaceDto();
    given(raceServiceMock.findRaces()).willReturn(List.of(raceDto));

    String response = mockMvc.perform(get("/race/")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    List<RaceDto> raceDtoResponse = List.of(objectMapper.readValue(response, RaceDto[].class));
    Assertions.assertEquals(raceDtoResponse.get(0), raceDto);
  }

  @Test
  void assignDriverOne() throws Exception {
    var raceDto = RaceTestUtil.getDaoRaceDto();
    given(raceServiceMock.assignDriverOne(1L, 1L, 1L)).willReturn(raceDto);

    String response = mockMvc.perform(patch("/race/assignDriverOne")
            .param("driverOneId", String.valueOf(1L))
            .param("carId", String.valueOf(1L))
            .param("raceId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    RaceDto raceDtoResponse = objectMapper.readValue(response, RaceDto.class);
    Assertions.assertEquals(raceDtoResponse, raceDto);
  }

  @Test
  void assignDriverTwo() throws Exception {
    var raceDto = RaceTestUtil.getDaoRaceDto();
    given(raceServiceMock.assignDriverTwo(2L, 1L, 2L)).willReturn(raceDto);

    String response = mockMvc.perform(patch("/race/assignDriverTwo")
            .param("driverTwoId", String.valueOf(2L))
            .param("carId", String.valueOf(2L))
            .param("raceId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    RaceDto raceDtoResponse = objectMapper.readValue(response, RaceDto.class);
    Assertions.assertEquals(raceDtoResponse, raceDto);
  }

  @Test
  void assignPositionDriverTwo() throws Exception {
    var raceDto = RaceTestUtil.getDaoRaceDto();
    given(raceServiceMock.assignPositionForDriverTwo(1L, 2)).willReturn(raceDto);

    String response = mockMvc.perform(patch("/race/assignPointsDriverTwo")
            .param("position", String.valueOf(2))
            .param("raceId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    RaceDto raceDtoResponse = objectMapper.readValue(response, RaceDto.class);
    Assertions.assertEquals(raceDtoResponse, raceDto);
  }

  @Test
  void assignPositionDriverOne() throws Exception {
    var raceDto = RaceTestUtil.getDaoRaceDto();
    given(raceServiceMock.assignPositionForDriverOne(1L, 1)).willReturn(raceDto);

    String response = mockMvc.perform(patch("/race/assignPointsDriverOne")
            .param("position", String.valueOf(1))
            .param("raceId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    RaceDto raceDtoResponse = objectMapper.readValue(response, RaceDto.class);
    Assertions.assertEquals(raceDtoResponse, raceDto);
  }

  @Test
  void getMostSuitableDriversForLocation() throws Exception {
    given(raceServiceMock.findMostSuitableDriver(Location.MONACO)).willReturn(Set.of(1L));

    String response = mockMvc.perform(get("/race/findMostSuitableDriversForLocation")
            .param("location", String.valueOf(Location.MONACO))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    var responseLongs = Set.of(objectMapper.readValue(response, Long[].class));
    Assertions.assertEquals(Set.of(1L), responseLongs);
  }
}
