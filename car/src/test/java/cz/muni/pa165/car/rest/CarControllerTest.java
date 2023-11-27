package cz.muni.pa165.car.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.pa165.car.data.repository.CarRepository;
import cz.muni.pa165.car.service.CarService;
import cz.muni.pa165.car.utils.CarTestUtils;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
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

@WebMvcTest(CarController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CarService carServiceMock;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @MockBean
  CarRepository carRepository;

  @Test
  void nonExistingPathPostTest() throws Exception {
    given(carServiceMock.postCar(CarTestUtils.getTestCarRequestDto()))
        .willReturn(CarTestUtils.getTestCarResponseDto());

    mockMvc.perform(put("/car/invalidPath/"))
        .andExpect(status().isNotFound());
  }


  @Test
  void createCarTest() throws Exception {
    given(carServiceMock.postCar(CarTestUtils.getTestCarRequestDto()))
        .willReturn(CarTestUtils.getTestCarResponseDto());

    String response = mockMvc.perform(post("/car/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(CarTestUtils.getTestCarRequestDto())))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    CarResponseDto carResponseDtoResponse = objectMapper.readValue(response, CarResponseDto.class);
    Assertions.assertEquals(carResponseDtoResponse, CarTestUtils.getTestCarResponseDto());
  }

  @Test
  void deleteExistingCarTest() throws Exception {
    given(carServiceMock.deleteById(1L)).willReturn(
        "Car with id " + 1L + " was successfully deleted.");

    String response = mockMvc.perform(delete("/car/")
            .param("carId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    Assertions.assertEquals("Car with id " + 1L + " was successfully deleted.", response);
  }

  @Test
  void getCarTest() throws Exception {
    given(carServiceMock.getCarById(1L)).willReturn(CarTestUtils.getTestCarResponseDto());

    String response = mockMvc.perform(get("/car/")
            .param("carId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    CarResponseDto carResponseDtoResponse = objectMapper.readValue(response, CarResponseDto.class);
    Assertions.assertEquals(carResponseDtoResponse, CarTestUtils.getTestCarResponseDto());
  }

  @Test
  void getAllCarsTest() throws Exception {
    List<CarResponseDto> responses = List.of(
        CarTestUtils.getTestCarResponseDto2(),
        CarTestUtils.getTestCarResponseDto3()
    );
    given(carServiceMock.getAllCars()).willReturn(responses);

    String response = mockMvc.perform(get("/car/all"))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    List<CarResponseDto> responseList =
        List.of(objectMapper.readValue(response, CarResponseDto[].class));
    Assertions.assertEquals(responseList, responses);
  }
}