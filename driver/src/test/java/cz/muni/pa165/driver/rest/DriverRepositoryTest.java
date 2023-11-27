package cz.muni.pa165.driver.rest;

import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.driver.data.model.Driver;
import cz.muni.pa165.driver.data.repository.DriverRepository;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
class DriverRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @MockBean
  SecurityFilterChain securityFilterChain;

  @Autowired
  private DriverRepository driverRepository;

  @Test
  void testSave() {
    Driver driver = Driver.builder()
        .name("name")
        .surname("surname")
        .nationality("nationality")
        .characteristics(Map.of()).build();

    Driver savedDriver = driverRepository.save(driver);

    Assertions.assertEquals(savedDriver, driver);
    Assertions.assertEquals(entityManager.find(Driver.class, savedDriver.getId()), savedDriver);
  }

  @Test
  void testUpdate() {
    Driver driver = Driver.builder()
        .name("Name")
        .surname("Surname")
        .nationality("Nationality")
        .characteristics(Map.of()).build();

    Driver savedDriver = driverRepository.save(driver);

    savedDriver.setName("Updated name");
    savedDriver.setSurname("Updated surname");
    savedDriver.setNationality("Updated nat");

    driverRepository.save(savedDriver);

    var updatedDriver = driverRepository.findById(savedDriver.getId()).orElseThrow();
    Assertions.assertNotEquals(DriverTestUtil.getDaoDriver(), updatedDriver);
    var original = DriverTestUtil.getDaoDriver();
    Assertions.assertAll(
        () -> Assertions.assertNotEquals(original.getName(), updatedDriver.getName()),
        () -> Assertions.assertNotEquals(original.getSurname(), updatedDriver.getSurname()),
        () -> Assertions.assertNotEquals(original.getNationality(), updatedDriver.getNationality())
    );
    Assertions.assertEquals(entityManager.find(Driver.class, savedDriver.getId()), savedDriver);

  }

  @Test
  void testNonExistingFind() {
    Assertions.assertThrows(Exception.class, () -> driverRepository.findById(-1L).orElseThrow());
  }

  @Test
  void testDelete() {
    Driver driver = Driver.builder()
        .name("Name")
        .surname("Surname")
        .nationality("Nationality")
        .characteristics(Map.of()).build();

    Driver savedDriver = driverRepository.save(driver);
    driverRepository.delete(savedDriver);

    Assertions.assertThrows(Exception.class, () -> driverRepository
        .findById(savedDriver.getId()).orElseThrow());
  }

  @Test
  void testFindAll() {
    Driver driver = Driver.builder()
        .name("Name")
        .surname("Surname")
        .nationality("Nationality")
        .characteristics(Map.of()).build();

    Driver driver2 = Driver.builder()
        .name("Name2")
        .surname("Surname2")
        .nationality("Nationality2")
        .characteristics(Map.of()).build();

    var savedDriver1 = driverRepository.save(driver);
    var savedDriver2 = driverRepository.save(driver2);

    var savedDrivers = driverRepository.findAll();
    Assertions.assertEquals(2, savedDrivers.size());

    Assertions.assertEquals(entityManager.find(Driver.class, savedDriver1.getId()), savedDriver1);
    Assertions.assertEquals(entityManager.find(Driver.class, savedDriver2.getId()), savedDriver2);
  }
}
