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

import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.race.data.model.Race;
import cz.muni.pa165.race.data.repository.RaceRepository;
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

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RaceServiceTest {

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  private String bodyContent = """
            {
              "id": 1,
              "raceInfo": {
                "location": "MONACO",
                "name": "Monaco Grand Prix 2023",
                "prizePool": 30000000
              }
            }
        """;
  private String expectedMessagePost = "{\"id\":1,\"raceInfo\":{\"location\":\"MONACO\","
      + "\"name\":\"Monaco Grand Prix 2023\",\"prizePool\":30000000},\"driverOne\""
      + ":{\"driverId\":1,\"carId\":1,\"position\":1},\"driverTwo\":{\"driverId\""
      + ":2,\"carId\":2,\"position\":2}}";
  private String expectedMessageAssignTwo = "{\"id\":1,\"raceInfo\":{\"location\":"
      + "\"MONACO\",\"name\":\"Monaco Grand Prix 2023\",\"prizePool\":30000000},"
      + "\"driverOne\":{\"driverId\":1,\"carId\":1,\"position\":1},\"driverTwo\""
      + ":{\"driverId\":1,\"carId\":1,\"position\":2}}";
  private String expectedMessageGet = "{\"id\":1,\"raceInfo\":{\"location\":\"MONACO\""
      + ",\"name\":\"Monaco Grand Prix 2023\",\"prizePool\":30000000},\"driverOne\""
      + ":{\"driverId\":1,\"carId\":1,\"position\":1},\"driverTwo\":{\"driverId\""
      + ":2,\"carId\":2,\"position\":2}}";
  private String expectedMessageGetAll = "[{\"id\":1,\"raceInfo\":{\"location\""
      + ":\"MONACO\",\"name\":\"Monaco Grand Prix 2023\",\"prizePool\":30000000},\""
      + "driverOne\":{\"driverId\":1,\"carId\":1,\"position\":1},\"driverTwo\":{\""
      + "driverId\":2,\"carId\":2,\"position\":2}}]";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RaceRepository raceRepository;

  @MockBean
  private CommonDbGetter dbGetter;

  @BeforeEach
  void setup() {
    Race raceDao = RaceTestUtil.getDaoRace();
    given(raceRepository.save(any(Race.class))).willReturn(
        raceDao);
    given(raceRepository.findById(anyLong())).willReturn(
        Optional.of(raceDao));
    given(raceRepository.findAll()).willReturn(List.of(raceDao));
    CarResponseDto carDao = RaceTestUtil.getCarDao();
    given(dbGetter.getCar(anyLong())).willReturn(Optional.of(carDao));
    DriverResponseDto driverDto = RaceTestUtil.getDriverDao();
    given(dbGetter.getDriverFromDb(anyLong())).willReturn(Optional.of(driverDto));
  }


  @Test
  void createRace() throws Exception {
    var request = post("/race/")
        .content(bodyContent)
        .contentType(MediaType.APPLICATION_JSON_VALUE);
    this.mockMvc.perform(request)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessagePost));
  }

  @Test
  void deleteRace() throws Exception {
    String expectedMessage = "Race with id 1 was successfully deleted.";
    var requestDelete = delete("/race/")
        .param("raceId", "1")
        .contentType(MediaType.APPLICATION_JSON_VALUE);
    this.mockMvc.perform(requestDelete)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessage));
  }

  @Test
  void getExistingRace() throws Exception {
    var requestGet = get("/race/id")
        .param("raceId", "1")
        .contentType(MediaType.APPLICATION_JSON_VALUE);
    this.mockMvc.perform(requestGet)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessageGet));
  }

  @Test
  void getAllRaces() throws Exception {
    var requestGet = get("/race/");
    this.mockMvc.perform(requestGet)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessageGetAll));
  }

  @Test
  void assignDriverOne() throws Exception {
    var requestAssign = patch("/race/assignDriverOne")
        .param("driverOneId", "1")
        .param("raceId", "1")
        .param("carId", "1")
        .contentType(MediaType.APPLICATION_JSON_VALUE);
    this.mockMvc.perform(requestAssign)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessagePost));
  }

  @Test
  void assignDriverTwo() throws Exception {
    var requestAssign = patch("/race/assignDriverTwo")
        .param("driverTwoId", "2")
        .param("raceId", "2")
        .param("carId", "2")
        .contentType(MediaType.APPLICATION_JSON_VALUE);
    this.mockMvc.perform(requestAssign)
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(expectedMessageAssignTwo));
  }

}
