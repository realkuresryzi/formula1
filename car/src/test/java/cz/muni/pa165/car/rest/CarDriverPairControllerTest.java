package cz.muni.pa165.car.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.pa165.car.data.repository.CarRepository;
import cz.muni.pa165.car.service.CarDriverPairService;
import cz.muni.pa165.car.utils.CarTestUtils;
import cz.muni.pa165.car.utils.DriverTestUtils;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.dtos.DriverDto;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CarDriverPairController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarDriverPairControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CarDriverPairService carDriverPairServiceMock;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @MockBean
  CarRepository carRepository;

  @Test
  void nonExistingPathPostTest() throws Exception {
    given(carDriverPairServiceMock.assignDriverToCar(1L, 1L)).willReturn(
        CarTestUtils.getTestCarResponseDto2());

    mockMvc.perform(put("/cardriver/invalidpath"))
        .andExpect(status().isNotFound());
  }

  @Test
  void assignDriverToCar() throws Exception {
    given(carDriverPairServiceMock.assignDriverToCar(1L, 1L)).willReturn(
        CarTestUtils.getTestCarResponseDto2()
    );

    String response = mockMvc.perform(put("/cardriver/assign")
            .param("driverId", String.valueOf(1L))
            .param("carId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    CarResponseDto carResponseDto = objectMapper.readValue(response, CarResponseDto.class);
    Assertions.assertEquals(1L, carResponseDto.getId());
    Assertions.assertEquals(List.of(1L), carResponseDto.getDriverIdsNames());
    Assertions.assertNull(carResponseDto.getMainDriverId());
  }

  @Test
  void unassignDriverFromCar() throws Exception {
    given(carDriverPairServiceMock.unassignDriverFromCar(1L, 1L)).willReturn(
        CarTestUtils.getTestCarResponseDto5()
    );

    String response = mockMvc.perform(put("/cardriver/unassign")
            .param("driverId", String.valueOf(1L))
            .param("carId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    CarResponseDto carResponseDto = objectMapper.readValue(response, CarResponseDto.class);
    Assertions.assertEquals(1L, carResponseDto.getId());
    Assertions.assertEquals(List.of(), carResponseDto.getDriverIdsNames());
    Assertions.assertNull(carResponseDto.getMainDriverId());
  }

  @Test
  void getAllDriversOfCar() throws Exception {
    given(carDriverPairServiceMock.getAllDriversOfCar(3L)).willReturn(
        List.of(DriverTestUtils.getDriverDto1(), DriverTestUtils.getDriverDto2()));

    String response = mockMvc.perform(put("/cardriver/alldrivers")
            .param("carId", String.valueOf(3L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    List<DriverDto> driverDtoListResponse =
        List.of(objectMapper.readValue(response, DriverDto[].class));
    Assertions.assertEquals(List.of(DriverTestUtils.getDriverDto1(),
        DriverTestUtils.getDriverDto2()), driverDtoListResponse);
  }

  @Test
  void setMainDriver() throws Exception {
    given(carDriverPairServiceMock.setMainDriver(3L, 1L)).willReturn(
        CarTestUtils.getTestCarResponseDto4()
    );

    String response = mockMvc.perform(put("/cardriver/setmain")
            .param("driverId", String.valueOf(1L))
            .param("carId", String.valueOf(3L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    CarResponseDto carResponseDto = objectMapper.readValue(response, CarResponseDto.class);
    Assertions.assertEquals(1L, carResponseDto.getMainDriverId());
  }

  @Test
  void removeMainDriver() throws Exception {
    given(carDriverPairServiceMock.removeMainDriver(1L)).willReturn(
        CarTestUtils.getTestCarResponseDto2()
    );

    String response = mockMvc.perform(put("/cardriver/removemain")
            .param("driverId", String.valueOf(1L))
            .param("carId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    CarResponseDto carResponseDto = objectMapper.readValue(response, CarResponseDto.class);
    Assertions.assertNull(carResponseDto.getMainDriverId());
  }
}