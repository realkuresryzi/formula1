package cz.muni.pa165.component.repository;

import static cz.muni.pa165.component.util.ComponentTestUtil.getComponent;

import cz.muni.pa165.common_library.security.ApiKeyAuthManager;
import cz.muni.pa165.component.data.model.CarComponent;
import cz.muni.pa165.component.data.repository.ComponentRepositoryInterface;
import java.math.BigDecimal;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit tests for component repository.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ComponentRepositoryUnitTest {

  @MockBean
  SecurityFilterChain securityFilterChain;

  @Autowired
  private TestEntityManager entityManager;

  @MockBean
  ApiKeyAuthManager apiKeyAuthManager;

  @Autowired
  private ComponentRepositoryInterface componentRepository;

  @Test
  public void saveTest() {
    Long id = 1L;
    String name = "Engine";
    BigDecimal one = BigDecimal.ONE;
    String manufacturer = "Ferrari";
    var carComponent = getComponent(id, name, one, manufacturer);

    var savedComponent = componentRepository.save(carComponent);

    Assertions.assertEquals(savedComponent, carComponent);
    Assertions.assertEquals(entityManager.find(CarComponent.class, 1L).getId(), 1);
  }

  @Test
  public void findByIdTest() {

    Long id = 2L;
    String name = "Engine";
    BigDecimal one = BigDecimal.ONE;
    String manufacturer = "Ferrari";
    var carComponent = getComponent(id, name, one, manufacturer);

    var savedComponent = componentRepository.save(carComponent);
    var component = componentRepository.findById(savedComponent.getId()).orElseThrow();

    Assertions.assertAll(
        () -> Assertions.assertEquals(component.getId(), savedComponent.getId()),
        () -> Assertions.assertEquals(component.getName(), savedComponent.getName()),
        () -> Assertions.assertEquals(component.getPrice(), savedComponent.getPrice()),
        () -> Assertions.assertEquals(component.getWeight(), savedComponent.getWeight()),
        () -> Assertions.assertEquals(component.getManufacturer(), savedComponent.getManufacturer())
    );
  }

  @Test
  public void findByNonExistingIdTest() {
    Assertions.assertThrows(Exception.class, () -> componentRepository.findById(-1L).orElseThrow());
  }

  @Test
  public void deleteByIdTest() {
    Long id = 3L;
    String name = "Engine";
    BigDecimal one = BigDecimal.ONE;
    String manufacturer = "Ferrari";
    var carComponent = getComponent(id, name, one, manufacturer);

    var savedComponent = componentRepository.save(carComponent);
    componentRepository.delete(savedComponent);

    Assertions.assertThrows(Exception.class, () -> componentRepository
        .findById(savedComponent.getId()).orElseThrow());
  }

  @Test
  public void testFindAll() {
    Long id1 = 4L;
    Long id2 = 5L;
    String name = "Engine";
    BigDecimal one = BigDecimal.ONE;
    String manufacturer = "Ferrari";
    var carComponent1 = getComponent(id1, name, one, manufacturer);
    var carComponent2 = getComponent(id2, name, one, manufacturer);

    componentRepository.save(carComponent1);
    componentRepository.save(carComponent2);

    var components = componentRepository.findAll();
    Assertions.assertEquals(2, components.size());
  }

}
