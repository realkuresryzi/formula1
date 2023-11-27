package cz.muni.pa165.driver.rest;

import cz.muni.pa165.common_library.dtos.DriverAddDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.dtos.DriverUpdateDto;
import cz.muni.pa165.driver.data.model.Driver;
import java.util.HashMap;

/**
 * Util for getting driver objects.
 */
public class DriverTestUtil {

  /**
   * Get add driver dto.
   *
   * @return add driver dto object.
   */
  public static DriverAddDto getAddDriver() {
    return new DriverAddDto("Name",
        "Surname", "Nationality", new HashMap<>());
  }

  /**
   * Get insight driver dto.
   *
   * @return insight driver dto object.
   */
  public static DriverResponseDto getResponseDriver() {
    return new DriverResponseDto(1L, "Name",
        "Surname", "Nationality", new HashMap<>());
  }

  /**
   * Get update driver dto.
   *
   * @return update driver dto object.
   */
  public static DriverUpdateDto getUpdateDriver() {
    return new DriverUpdateDto("Name",
        "Surname", "Nationality", new HashMap<>());
  }

  /**
   * Get entity driver object.
   *
   * @return entity driver object.
   */
  public static Driver getDaoDriver() {
    return Driver.builder()
        .name("Name")
        .id(1L)
        .surname("Surname")
        .nationality("Nationality")
        .characteristics(new HashMap<>())
        .build();
  }
}
