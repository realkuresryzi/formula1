package cz.muni.pa165.car.data.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cz.muni.pa165.car.data.model.Car;
import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;


@DataJpaTest
class CarRepositoryTest {

  @MockBean
  SecurityFilterChain securityFilterChain;

  @Autowired
  private TestEntityManager entityManager;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @Autowired
  private CarRepository carRepository;

  @Test
  public void saveTest() {
    Car car = Car.builder()
        .id(1L)
        .components(Set.of(2L, 3L, 4L))
        .drivers(Set.of(5L, 6L, 7L))
        .mainDriverId(6L)
        .build();

    Car savedCar = carRepository.save(car);

    Assertions.assertEquals(savedCar, car);
    Assertions.assertEquals(entityManager.find(Car.class, 1L).getId(), 1);
  }

  @Test
  public void addComponentAndSaveTest() {
    Car car = Car.builder()
        .id(1L)
        .components(Set.of(2L, 3L, 4L))
        .drivers(Set.of(6L))
        .mainDriverId(6L)
        .build();

    Car savedCar = carRepository.save(car);
    savedCar.getComponents().add(111L);
    Car carWithAddedComponent = carRepository.save(savedCar);
    assertTrue(carWithAddedComponent.getComponents().contains(111L));
  }

  @Test
  public void removeComponentAndSaveTest() {
    Car car = Car.builder()
        .id(1L)
        .components(Set.of(2L, 3L, 4L))
        .drivers(Set.of(6L))
        .mainDriverId(6L)
        .build();

    Car savedCar = carRepository.save(car);
    savedCar.getComponents().remove(111L);
    Car carWithAddedComponent = carRepository.save(savedCar);
    assertFalse(carWithAddedComponent.getComponents().contains(111L));
  }

  @Test
  public void getAllComponentsTest() {
    Car car = Car.builder()
        .id(1L)
        .components(Set.of(2L, 3L, 4L))
        .drivers(Set.of(6L))
        .mainDriverId(6L)
        .build();

    Car savedCar = carRepository.save(car);
    assertEquals(savedCar.getComponents(), Set.of(2L, 3L, 4L));
  }

  @Test
  public void addDriverAndSaveTest() {
    Car car = Car.builder()
        .id(1L)
        .components(Set.of())
        .drivers(Set.of(5L, 6L, 7L))
        .mainDriverId(6L)
        .build();

    Car savedCar = carRepository.save(car);
    savedCar.getDrivers().add(111L);
    Car carWithAddedDriver = carRepository.save(savedCar);
    assertTrue(carWithAddedDriver.getDrivers().contains(111L));
  }

  @Test
  public void removeDriverAndSaveTest() {
    Car car = Car.builder()
        .id(1L)
        .components(Set.of())
        .drivers(Set.of(5L, 6L, 7L))
        .mainDriverId(6L)
        .build();

    Car savedCar = carRepository.save(car);
    savedCar.getDrivers().remove(6L);
    Car carWithRemovedDriver = carRepository.save(savedCar);
    assertFalse(carWithRemovedDriver.getDrivers().contains(6L));
  }

  @Test
  public void setDriverAsMainAndSaveTest() {
    Car car = Car.builder()
        .id(1L)
        .components(Set.of())
        .drivers(Set.of(5L, 6L, 7L))
        .mainDriverId(6L)
        .build();

    Car savedCar = carRepository.save(car);
    assertEquals(6L, (long) savedCar.getMainDriverId());
    savedCar.setMainDriverId(7L);
    Car carWithReplacedMainDriver = carRepository.save(savedCar);
    assertEquals(7L, (long) savedCar.getMainDriverId());
  }

  @Test
  public void removeDriverAsMainAndSaveTest() {
    Car car = Car.builder()
        .id(1L)
        .components(Set.of())
        .drivers(Set.of(5L, 6L, 7L))
        .mainDriverId(6L)
        .build();

    Car savedCar = carRepository.save(car);
    assertEquals(6L, (long) savedCar.getMainDriverId());
    savedCar.setMainDriverId(null);
    Car carWithRemovedMainDriver = carRepository.save(savedCar);
    assertNull(carWithRemovedMainDriver.getMainDriverId());
  }
}
