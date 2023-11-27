package cz.muni.pa165.car.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.pa165.car.data.repository.CarRepository;
import cz.muni.pa165.car.service.CarComponentPairService;
import cz.muni.pa165.car.utils.CarTestUtils;
import cz.muni.pa165.car.utils.ComponentTestUtils;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CarComponentPairController.class)
@AutoConfigureMockMvc(addFilters = false)
class CarComponentPairControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private CarComponentPairService carComponentPairServiceMock;
  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @MockBean
  CarRepository carRepository;

  @Test
  void addComponent() throws Exception {
    given(carComponentPairServiceMock.addComponent(1L, 1L)).willReturn(
        CarTestUtils.getTestCarResponseDto());

    String response = mockMvc.perform(put("/carcomponent/addcomponent")
            .param("componentId", String.valueOf(1L))
            .param("carId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    CarResponseDto carResponseDto = objectMapper.readValue(response, CarResponseDto.class);
    Assertions.assertEquals(1L, carResponseDto.getId());
    Assertions.assertEquals(List.of(1L, 2L), carResponseDto.getComponentIdsNames());
  }

  @Test
  void removeComponent() throws Exception {
    given(carComponentPairServiceMock.removeComponent(1L, 1L)).willReturn(
        CarTestUtils.getTestCarResponseDto2());

    String response = mockMvc.perform(put("/carcomponent/removecomponent")
            .param("componentId", String.valueOf(1L))
            .param("carId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    CarResponseDto carResponseDto = objectMapper.readValue(response, CarResponseDto.class);
    Assertions.assertEquals(1L, carResponseDto.getId());
    Assertions.assertEquals(List.of(), carResponseDto.getComponentIdsNames());
  }

  @Test
  void getAllComponentsOfCar() throws Exception {
    given(carComponentPairServiceMock.getAllComponentsOfCar(1L)).willReturn(
        List.of(ComponentTestUtils.getTestComponent1(), ComponentTestUtils.getTestComponent2()));

    String response = mockMvc.perform(get("/carcomponent/getcomponents")
            .param("carId", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    List<CarComponentResponseDto> carComponentListResponse =
        List.of(objectMapper.readValue(response, CarComponentResponseDto[].class));
    Assertions.assertEquals(List.of(ComponentTestUtils.getTestComponent1(),
            ComponentTestUtils.getTestComponent2()),
        carComponentListResponse);
  }
}