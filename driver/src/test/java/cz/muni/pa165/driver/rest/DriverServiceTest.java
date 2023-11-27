package cz.muni.pa165.driver.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.dtos.DriverUpdateDto;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.driver.data.model.Driver;
import cz.muni.pa165.driver.data.repository.DriverRepository;
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
class DriverServiceTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private DriverRepository driverRepository;

  @MockBean
  private CommonDbGetter commonDbGetter;

  @Test
  void driverAddTest() throws Exception {
    var driverAddDto = DriverTestUtil.getAddDriver();
    Driver driverDao = DriverTestUtil.getDaoDriver();
    given(driverRepository.save(any(Driver.class))).willReturn(
        driverDao);
    given(driverRepository.findById(anyLong())).willReturn(
        Optional.of(driverDao));

    String response = mockMvc.perform(post("/driver/add")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(driverAddDto)))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    var driverResponseDto = objectMapper.readValue(response, DriverResponseDto.class);
    //Assertions.assertEquals(1L, driverResponseDto.id());
  }

  @Test
  void driverGetAllTest() throws Exception {

    var driver = DriverTestUtil.getAddDriver();
    when(driverRepository.findAll()).thenReturn(List.of(DriverTestUtil.getDaoDriver()));

    String response = mockMvc.perform(get("/driver/get/all"))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    var driverResponseDto = objectMapper.readValue(response, DriverResponseDto[].class);
    Assertions.assertAll(
        () -> Assertions.assertEquals(1L, driverResponseDto[0].getId()),
        () -> Assertions.assertEquals(driver.getName(), driverResponseDto[0].getName()),
        () -> Assertions.assertEquals(driver.getSurname(), driverResponseDto[0].getSurname()),
        () -> Assertions.assertEquals(driver.getCharacteristics(),
            driverResponseDto[0].getCharacteristics())
    );
  }

  @Test
  void driverGetByIdTest() throws Exception {

    var driver = DriverTestUtil.getAddDriver();
    when(driverRepository.findById(1L)).thenReturn(Optional.of(DriverTestUtil.getDaoDriver()));

    String response = mockMvc.perform(get("/driver/get/id")
            .param("id", String.valueOf(1L)))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    var driverResponseDto = objectMapper.readValue(response, DriverResponseDto.class);
    Assertions.assertAll(
        () -> Assertions.assertEquals(1L, driverResponseDto.getId()),
        () -> Assertions.assertEquals(driver.getName(), driverResponseDto.getName()),
        () -> Assertions.assertEquals(driver.getSurname(), driverResponseDto.getSurname()),
        () -> Assertions.assertEquals(driver.getCharacteristics(),
            driverResponseDto.getCharacteristics())
    );
  }

  @Test
  void driverUpdateByIdTest() throws Exception {
    DriverUpdateDto driverUpdateDto = DriverTestUtil.getUpdateDriver();
    Driver driverDao = DriverTestUtil.getDaoDriver();
    given(driverRepository.save(driverDao)).willReturn(
        driverDao);
    given(driverRepository.findById(anyLong())).willReturn(
        Optional.of(driverDao));

    String response = mockMvc.perform(put("/driver/update/id")
            .param("id", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(driverUpdateDto)))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    var driverResponseDto = objectMapper.readValue(response, DriverResponseDto.class);
    Assertions.assertEquals(1L, driverResponseDto.getId());
  }

  @Test
  void driverDeleteTest() throws Exception {
    DriverUpdateDto driverUpdateDto = DriverTestUtil.getUpdateDriver();
    Driver driverDao = DriverTestUtil.getDaoDriver();
    given(driverRepository.findById(anyLong())).willReturn(
        Optional.of(driverDao));

    String response = mockMvc.perform(delete("/driver/remove/id")
            .param("id", String.valueOf(1L))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(driverUpdateDto)))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    var driverResponseDto = objectMapper.readValue(response, DriverResponseDto.class);
    Assertions.assertEquals(1L, driverResponseDto.getId());
  }
}
