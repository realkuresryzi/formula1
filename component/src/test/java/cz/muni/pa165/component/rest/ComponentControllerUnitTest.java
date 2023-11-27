package cz.muni.pa165.component.rest;

import static cz.muni.pa165.component.util.ComponentTestUtil.getComponent;
import static cz.muni.pa165.component.util.ComponentTestUtil.getComponentResponseDto;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.pa165.common_library.dtos.CarComponentRequestDto;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.component.data.model.CarComponent;
import cz.muni.pa165.component.service.ComponentService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ComponentController.class)
@AutoConfigureMockMvc(addFilters = false)
class ComponentControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @MockBean
  private ComponentService componentService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void createCarComponentTest() throws Exception {

    Long id = 1L;
    String name = "Engine";
    BigDecimal one = BigDecimal.ONE;
    String manufacturer = "Ferrari";
    var carComponent = getComponent(id, name, one, manufacturer);
    var carComponentResponseDto = getComponentResponseDto(id, name, one, manufacturer);

    given(componentService.postCarComponent(CarComponentRequestDto.builder().build()))
        .willReturn(carComponentResponseDto);
    mockMvc.perform(post("/component/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(carComponent)))
        .andExpect(status().isOk());
  }

  @Test
  void nonExistingPathTest() throws Exception {
    mockMvc.perform(post("/invalidPath"))
        .andExpect(status().isNotFound());
  }

  @Test
  void createComponentWithNullValuesTest() throws Exception {

    CarComponent carComponent = new CarComponent();
    CarComponentRequestDto carComponentRequestDto = new CarComponentRequestDto();
    CarComponentResponseDto carComponentResponseDto = new CarComponentResponseDto();

    given(componentService.postCarComponent(carComponentRequestDto))
        .willReturn(carComponentResponseDto);
    mockMvc.perform(post("/component/").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(carComponent)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteCarComponentTest() throws Exception {

    Long id = 1L;

    String expectedResponse = "Car component with id " + 1L + " was successfully deleted.";
    given(componentService.deleteById(id)).willReturn(expectedResponse);
    String actualResponse = mockMvc.perform(delete("/component/")
            .param("componentId", String.valueOf(id))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    Assertions.assertEquals(expectedResponse, actualResponse);
  }

  @Test
  void getCarComponentTest() throws Exception {

    Long id = 1L;
    String name = "Engine";
    BigDecimal one = BigDecimal.ONE;
    String manufacturer = "Ferrari";
    var carComponentResponseDto = getComponentResponseDto(id, name, one, manufacturer);

    given(componentService.getCarComponentById(id)).willReturn(carComponentResponseDto);
    mockMvc.perform(get("/component/id")
            .param("componentId", String.valueOf(id))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value(name))
        .andExpect(jsonPath("$.price").value(one))
        .andExpect(jsonPath("$.weight").value(one))
        .andExpect(jsonPath("$.manufacturer").value(manufacturer));
  }

  @Test
  void getAllCarComponentsTest() throws Exception {

    List<CarComponentResponseDto> components = new ArrayList<>();

    given(componentService.getAllCarComponents()).willReturn(components);
    mockMvc.perform(get("/component/")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

}

