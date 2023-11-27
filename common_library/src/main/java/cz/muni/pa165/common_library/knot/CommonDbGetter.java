package cz.muni.pa165.common_library.knot;

import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.dtos.RaceDto;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Class for retrieving data from other modules using RestTemplate.
 */
@Component
public class CommonDbGetter {

  private final RestTemplate client = new RestTemplate();

  /**
   * Could be localhost or race (when hosting on docker).
   */
  @Value("${server.services.race.host}")
  private String raceHost;
  @Value("${server.services.race.port}")
  private String racePort;

  /**
   * Could be localhost or driver (when hosting on docker).
   */
  @Value("${server.services.driver.host}")
  private String driverHost;
  @Value("${server.services.driver.port}")
  private String driverPort;

  /**
   * Could be localhost or component (when hosting on docker).
   */
  @Value("${server.services.component.host}")
  private String componentHost;
  @Value("${server.services.component.port}")
  private String componentPort;

  /**
   * Could be localhost or car (when hosting on docker).
   */
  @Value("${server.services.car.host}")
  private String carHost;
  @Value("${server.services.car.port}")
  private String carPort;

  /**
   * Could be localhost or authenticate (when hosting on docker).
   */
  @Value("${server.services.authentication.host}")
  private String authenticationHost;
  @Value("${server.services.authentication.port}")
  private String authenticationPort;

  @Value("${formula.authenticate.api-key}")
  private String apiKey;

  /**
   * Get a driver using RestTemplate client.
   *
   * @param id driver id
   * @return Driver object
   */
  public Optional<DriverResponseDto> getDriverFromDb(Long id) {
    try {
      return fetchDriver(driverHost, id);
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  private Optional<DriverResponseDto> fetchDriver(String source, Long id) {
    String url = "http://" + source + ":" + driverPort + "/driver/get/id?id=" + id;

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    var driver = client.exchange(
        url, HttpMethod.GET, new HttpEntity<>(headers),
        DriverResponseDto.class).getBody();
    return Optional.ofNullable(driver);
  }

  /**
   * Calls endpoint of the Driver module and retrieves all the drivers in db.
   *
   * @return List of all drivers in database.
   */
  public Optional<List<DriverResponseDto>> getAllDriversFromDb() {
    try {
      return fetchAllDrivers(driverHost);
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  private Optional<List<DriverResponseDto>> fetchAllDrivers(String source) {
    String url = "http://" + source + ":" + driverPort + "/driver/get/all";

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    var drivers = client.exchange(
        url, HttpMethod.GET, new HttpEntity<>(headers),
        DriverResponseDto[].class).getBody();
    return Optional.of(Arrays.stream(drivers).toList());
  }

  /**
   * Fills drivers in db.
   */
  public void fillDriversInDb() {
    try {
      fillDrivers(driverHost);
    } catch (Exception ignored) {
      ignored.printStackTrace();
    }
  }

  private void fillDrivers(String source) {
    String url = "http://" + source + ":" + driverPort + "/driver/fill";

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    client.exchange(
        url, HttpMethod.POST, new HttpEntity<>(headers), Void.class);

  }

  /**
   * Get a component using RestTemplate client.
   *
   * @param id component id
   * @return Component object
   */
  public Optional<CarComponentResponseDto> getComponentFromDb(Long id) {
    try {
      return fetchComponent(componentHost, id);
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  private Optional<CarComponentResponseDto> fetchComponent(String source, Long id) {
    String url = "http://" + source + ":" + componentPort + "/component/id?componentId=" + id;

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    var components = client.exchange(
        url, HttpMethod.GET, new HttpEntity<>(headers),
        CarComponentResponseDto.class).getBody();
    return Optional.ofNullable(components);
  }

  /**
   * Calls endpoint of the CarComponent module and retrieves all the car components in db.
   *
   * @return List of all car components in database.
   */
  public Optional<List<CarComponentResponseDto>> getAllComponentsFromDb() {
    try {
      return fetchAllComponents(componentHost);
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  /**
   * Calls endpoint of the CarComponent module and fills components in db.
   *
   */
  public void fillComponentsFromDb() {
    try {
      fillComponents(componentHost);
    } catch (Exception ignored) {
      ignored.printStackTrace();
    }
  }

  private void fillComponents(String source) {
    String url = "http://" + source + ":" + componentPort + "/component/fill";

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    client.exchange(
        url, HttpMethod.POST, new HttpEntity<>(headers), Void.class);

  }

  private Optional<List<CarComponentResponseDto>> fetchAllComponents(String source) {
    String url = "http://" + source + ":" + componentPort + "/component/";

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    var components = client.exchange(
        url, HttpMethod.GET, new HttpEntity<>(headers),
        CarComponentResponseDto[].class).getBody();
    return Optional.of(Arrays.stream(components).toList());
  }

  /**
   * Calls endpoint of the car module to retrieve all cars form the database.
   *
   * @return optionally list of all existing cars.
   */
  public Optional<List<RaceDto>> getAllRaces() {
    try {
      return fetchAllRaces(raceHost);
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  private Optional<List<RaceDto>> fetchAllRaces(String source) {
    String url = "http://" + source + ":" + racePort + "/race/";

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    var races = client.exchange(
        url, HttpMethod.GET, new HttpEntity<>(headers),
        RaceDto[].class).getBody();
    return Optional.of(Arrays.stream(races).toList());
  }

  /**
   * Calls car module for a car given by id.
   *
   * @param carId id of a car.
   * @return carDto.
   */
  public Optional<CarResponseDto> getCar(Long carId) {
    try {
      return fetchCar(carHost, carId);
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  private Optional<CarResponseDto> fetchCar(String source, Long carId) {
    String url = "http://" + source + ":" + carPort + "/car/?carId=" + carId;
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    var car = client.exchange(
        url, HttpMethod.GET, new HttpEntity<>(headers),
        CarResponseDto.class).getBody();
    return Optional.ofNullable(car);
  }

  /**
   * Fills cars in db.
   */
  public void fillCarsInDb() {
    try {
      fillCars(carHost);
    } catch (Exception ignored) {
      ignored.printStackTrace();
    }
  }

  private void fillCars(String source) {
    String url = "http://" + source + ":" + carPort + "/car/fill";

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    client.exchange(
        url, HttpMethod.POST, new HttpEntity<>(headers), Void.class);
  }

  /**
   * Calls endpoint of the car module to retrieve all cars form the database.
   *
   * @return optionally list of all existing cars.
   */
  public Optional<List<CarResponseDto>> getAllCars() {
    try {
      return fetchAllCars(carHost);
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  private Optional<List<CarResponseDto>> fetchAllCars(String source) {
    String url = "http://" + source + ":" + carPort + "/car/all";

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    var cars = client.exchange(
        url, HttpMethod.GET, new HttpEntity<>(headers),
        CarResponseDto[].class).getBody();
    return Optional.of(Arrays.stream(cars).toList());
  }

  /**
   * Calls endpoint of the authentication module to retrieve all manager's
   * emails form the database.
   *
   * @return optionally list of all emails.
   */
  public Optional<Set<String>> getManagerEmails() {
    try {
      return fetchEmails(authenticationHost);
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  private Optional<Set<String>> fetchEmails(String source) {
    String url =
        "http://" + source + ":" + authenticationPort + "/authenticate/apikey/getManagerEmails";

    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("API_KEY", apiKey);

    var emailsResponse = client.exchange(
        url, HttpMethod.GET, new HttpEntity<>(headers),
        String[].class).getBody();
    Set<String> emails = new HashSet<>(Arrays.asList(emailsResponse));
    return Optional.of(emails);
  }
}
