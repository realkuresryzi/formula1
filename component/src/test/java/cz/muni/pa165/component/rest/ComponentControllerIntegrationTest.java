package cz.muni.pa165.component.rest;

import static cz.muni.pa165.component.util.ComponentTestUtil.getComponent;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.component.data.model.CarComponent;
import cz.muni.pa165.component.data.repository.ComponentRepositoryInterface;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ComponentControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @MockBean
  private ComponentRepositoryInterface componentRepositoryMock;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CommonDbGetter commonDbGetter;

  @Test
  void postIntegrationTest() throws Exception {

    Long id = 1L;
    String name = "Engine";
    BigDecimal one = BigDecimal.ONE;
    String manufacturer = "Ferrari";
    var carComponent = getComponent(id, name, one, manufacturer);

    given(componentRepositoryMock.save(any(CarComponent.class))).willReturn(carComponent);

    String response = mockMvc.perform(post("/component/")
            .content(objectMapper.writeValueAsString(carComponent))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    CarComponent componentResponse = objectMapper.readValue(response, CarComponent.class);
    assertAll(
        () -> assertEquals(carComponent.getId(), componentResponse.getId()),
        () -> assertEquals(carComponent.getName(), componentResponse.getName()),
        () -> assertEquals(carComponent.getWeight(), componentResponse.getWeight()),
        () -> assertEquals(carComponent.getPrice(), componentResponse.getPrice()),
        () -> assertEquals(carComponent.getManufacturer(), componentResponse.getManufacturer())
    );
  }

  @Test
  void deleteIntegrationTest() throws Exception {

    Long id = 1L;
    String name = "Engine";
    BigDecimal one = BigDecimal.ONE;
    String manufacturer = "Ferrari";
    var carComponent = getComponent(id, name, one, manufacturer);

    given(componentRepositoryMock.findById(anyLong())).willReturn(
        Optional.of(carComponent));

    String expectedResponse = "Component with id " + 1L + " was successfully deleted.";
    String actualResponse = mockMvc.perform(delete("/component/")
            .param("componentId", String.valueOf(id))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    Assertions.assertEquals(expectedResponse, actualResponse);
  }

  @Test
  void getComponentByIdTest() throws Exception {

    Long id = 1L;
    String name = "Engine";
    BigDecimal one = BigDecimal.ONE;
    String manufacturer = "Ferrari";
    var carComponent = getComponent(id, name, one, manufacturer);

    given(componentRepositoryMock.findById(id)).willReturn(Optional.of(carComponent));

    String response = mockMvc.perform(get("/component/id")
            .param("componentId", String.valueOf(id))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    var componentResponse = objectMapper.readValue(response, CarComponent.class);

    Assertions.assertAll(
        () -> Assertions.assertEquals(id, componentResponse.getId()),
        () -> Assertions.assertEquals(name, componentResponse.getName()),
        () -> Assertions.assertEquals(one, componentResponse.getWeight()),
        () -> Assertions.assertEquals(one, componentResponse.getPrice()),
        () -> Assertions.assertEquals(manufacturer, componentResponse.getManufacturer())
    );
  }

  @Test
  void getAllComponentsTest() throws Exception {

    Long id = 1L;
    String name = "Engine";
    BigDecimal one = BigDecimal.ONE;
    String manufacturer = "Ferrari";
    var carComponent = getComponent(id, name, one, manufacturer);

    given(componentRepositoryMock.findAll()).willReturn(List.of(carComponent));

    String response = mockMvc.perform(get("/component/")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();

    var componentResponse = objectMapper.readValue(response, CarComponent[].class);
    Assertions.assertAll(
        () -> Assertions.assertEquals(id, componentResponse[0].getId()),
        () -> Assertions.assertEquals(name, componentResponse[0].getName()),
        () -> Assertions.assertEquals(one, componentResponse[0].getWeight()),
        () -> Assertions.assertEquals(one, componentResponse[0].getPrice()),
        () -> Assertions.assertEquals(manufacturer, componentResponse[0].getManufacturer())
    );
  }

}

