package cz.muni.pa165.driver.rest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.dtos.DriverUpdateDto;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.driver.service.DriverService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = DriverController.class)
@AutoConfigureMockMvc(addFilters = false)
class DriverControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private DriverService driverService;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @Test
  void driverAddTest() throws Exception {

    var driver = DriverTestUtil.getAddDriver();
    var responseDriver = DriverTestUtil.getResponseDriver();
    given(driverService.addDriver(driver)).willReturn(responseDriver);

    String response = mockMvc.perform(post("/driver/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(driver)))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    DriverResponseDto driverResponseDto = objectMapper.readValue(response, DriverResponseDto.class);
    Assertions.assertAll(
        () -> Assertions.assertEquals(responseDriver.getId(), driverResponseDto.getId()),
        () -> Assertions.assertEquals(responseDriver.getName(), driverResponseDto.getName()),
        () -> Assertions.assertEquals(responseDriver.getSurname(), driverResponseDto.getSurname()),
        () -> Assertions.assertEquals(responseDriver.getNationality(),
            driverResponseDto.getNationality())
    );
  }

  @Test
  void badRequestTest() throws Exception {

    var driver = DriverTestUtil.getAddDriver();
    var responseDriver = DriverTestUtil.getResponseDriver();
    given(driverService.addDriver(driver)).willReturn(responseDriver);

    mockMvc.perform(post("/driver/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString("INVALID CONTENT")))
        .andExpect(status().isBadRequest());

  }

  @Test
  void driverGetAllTest() throws Exception {

    var driver = DriverTestUtil.getAddDriver();
    given(driverService.getAllDrivers()).willReturn(List.of(DriverTestUtil.getResponseDriver()));

    String response = mockMvc.perform(get("/driver/get/all"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    var driverResponseDto = objectMapper.readValue(response, DriverResponseDto[].class);
    Assertions.assertAll(
        () -> Assertions.assertEquals(1L, driverResponseDto[0].getId()),
        () -> Assertions.assertEquals(driver.getName(), driverResponseDto[0].getName()),
        () -> Assertions.assertEquals(driver.getSurname(), driverResponseDto[0].getSurname()),
        () -> Assertions.assertEquals(driver.getCharacteristics(),
            driverResponseDto[0].getCharacteristics()),
        () -> Assertions.assertEquals(1, driverResponseDto.length)
    );
  }

  @Test
  void driverDeleteTest() throws Exception {
    var responseDriver = DriverTestUtil.getResponseDriver();
    given(driverService.removeDriverById(1L)).willReturn(responseDriver);

    String response = mockMvc.perform(delete("/driver/remove/id")
            .param("id", String.valueOf(1L)))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    var driverResponseDto = objectMapper.readValue(response, DriverResponseDto.class);
    Assertions.assertAll(
        () -> Assertions.assertEquals(responseDriver.getId(), driverResponseDto.getId()),
        () -> Assertions.assertEquals(responseDriver.getName(), driverResponseDto.getName()),
        () -> Assertions.assertEquals(responseDriver.getSurname(), driverResponseDto.getSurname()),
        () -> Assertions.assertEquals(responseDriver.getNationality(),
            driverResponseDto.getNationality())
    );
  }

  @Test
  void invalidPathTest() throws Exception {
    mockMvc.perform(get("/invalidPath"))
        .andExpect(status().isNotFound());
  }

  @Test
  void driverGetByIdTest() throws Exception {

    var driver = DriverTestUtil.getAddDriver();
    given(driverService.getDriverById(anyLong())).willReturn(DriverTestUtil.getResponseDriver());

    String response = mockMvc.perform(get("/driver/get/id")
            .param("id", String.valueOf(1L)))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    var driverResponseDto = objectMapper.readValue(response, DriverResponseDto.class);
    Assertions.assertAll(
        () -> Assertions.assertEquals(1L, driverResponseDto.getId()),
        () -> Assertions.assertEquals(driver.getName(), driverResponseDto.getName()),
        () -> Assertions.assertEquals(driver.getSurname(), driverResponseDto.getSurname()),
        () -> Assertions.assertEquals(driver.getNationality(), driverResponseDto.getNationality()),
        () -> Assertions.assertEquals(driver.getCharacteristics(),
            driverResponseDto.getCharacteristics())
    );
  }

  @Test
  void driverUpdateByIdTest() throws Exception {
    DriverUpdateDto driverUpdateDto = DriverTestUtil.getUpdateDriver();
    var responseDriver = DriverTestUtil.getResponseDriver();
    given(driverService.updateDriverById(1L, driverUpdateDto)).willReturn(
        responseDriver);

    String response = mockMvc.perform(put("/driver/update/id")
            .param("id", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(driverUpdateDto)))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    var driverResponseDto = objectMapper.readValue(response, DriverResponseDto.class);
    Assertions.assertAll(
        () -> Assertions.assertEquals(1L, driverResponseDto.getId()),
        () -> Assertions.assertEquals(driverUpdateDto.getName(), driverResponseDto.getName()),
        () -> Assertions.assertEquals(driverUpdateDto.getSurname(), driverResponseDto.getSurname()),
        () -> Assertions.assertEquals(driverUpdateDto.getNationality(),
            driverResponseDto.getNationality()),
        () -> Assertions.assertEquals(driverUpdateDto.getCharacteristics(),
            driverResponseDto.getCharacteristics())
    );
  }
}
