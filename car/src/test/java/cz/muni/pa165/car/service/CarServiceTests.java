package cz.muni.pa165.car.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.pa165.car.data.model.Car;
import cz.muni.pa165.car.data.repository.CarRepository;
import cz.muni.pa165.car.utils.CarTestUtils;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest()
@AutoConfigureMockMvc(addFilters = false)
class CarServiceTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @MockBean
  private CarRepository carRepository;

  @MockBean
  private CommonDbGetter dbGetter;

  @Test
  void carAddTest() throws Exception {

    Car car1 = CarTestUtils.getTestCar1();
    Car car2 = CarTestUtils.getTestCar2();
    given(carRepository.save(car1)).willReturn(car2);
    given(carRepository.findById(1L)).willReturn(
        Optional.of(car2));
    DriverResponseDto driverInsightDto =
        new DriverResponseDto(1L, "name", "surname", "nationality", Map.of());
    CarComponentResponseDto carComponentDto = CarComponentResponseDto.builder().build();

    given(dbGetter.getDriverFromDb(anyLong())).willReturn(Optional.of(driverInsightDto));
    given(dbGetter.getComponentFromDb(anyLong())).willReturn(Optional.ofNullable(carComponentDto));

    String response = mockMvc.perform(post("/car/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(CarTestUtils.getTestCarRequestDto())))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    var carResponse = objectMapper.readValue(response, CarResponseDto.class);
    Assertions.assertAll(
        () -> Assertions.assertEquals(carResponse.getId(), CarTestUtils.getTestCar2().getId()),
        () -> Assertions.assertEquals(carResponse.getComponentIdsNames(),
            CarTestUtils.getTestCar2().getComponents().stream().toList()),
        () -> Assertions.assertEquals(carResponse.getDriverIdsNames(),
            CarTestUtils.getTestCar2().getDrivers().stream().toList()),
        () -> Assertions.assertEquals(carResponse.getMainDriverId(),
            CarTestUtils.getTestCar2().getMainDriverId())
    );
  }

  @Test
  void carGetAllTest() throws Exception {
    Car car2 = CarTestUtils.getTestCar2();
    given(carRepository.findAll()).willReturn(List.of(car2));

    String response = mockMvc.perform(get("/car/all"))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    var carResponse = List.of(objectMapper.readValue(response, CarResponseDto[].class));
    Assertions.assertAll(
        () -> Assertions.assertEquals(carResponse.get(0).getId(), car2.getId()),
        () -> Assertions.assertEquals(carResponse.get(0).getComponentIdsNames(),
            car2.getComponents().stream().toList()),
        () -> Assertions.assertEquals(carResponse.get(0).getDriverIdsNames(),
            car2.getDrivers().stream().toList()),
        () -> Assertions.assertEquals(carResponse.get(0).getMainDriverId(), car2.getMainDriverId())
    );
  }

  @Test
  void carGetByIdTest() throws Exception {
    Car car2 = CarTestUtils.getTestCar2();
    given(carRepository.findById(1L)).willReturn(Optional.of(car2));

    String response = mockMvc.perform(get("/car/")
            .param("carId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    var carResponse = objectMapper.readValue(response, CarResponseDto.class);
    Assertions.assertAll(
        () -> Assertions.assertEquals(carResponse.getId(), car2.getId()),
        () -> Assertions.assertEquals(carResponse.getComponentIdsNames(),
            car2.getComponents().stream().toList()),
        () -> Assertions.assertEquals(carResponse.getDriverIdsNames(),
            car2.getDrivers().stream().toList()),
        () -> Assertions.assertEquals(carResponse.getMainDriverId(), car2.getMainDriverId())
    );
  }

  @Test
  void carDeleteByIdTest() throws Exception {
    Car car2 = CarTestUtils.getTestCar2();
    given(carRepository.findById(1L)).willReturn(Optional.of(car2));

    String response = mockMvc.perform(delete("/car/")
            .param("carId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    Assertions.assertEquals("Car with id " + 1L + " was successfully deleted.", response);
  }
}
