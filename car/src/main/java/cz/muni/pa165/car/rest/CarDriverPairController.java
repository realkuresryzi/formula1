package cz.muni.pa165.car.rest;

import cz.muni.pa165.car.service.CarDriverPairService;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.dtos.DriverDto;
import cz.muni.pa165.common_library.exception.ExError;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Rest Controller for Driver Manager.
 */
@OpenAPIDefinition(
    info = @Info(title = "Car Microservice",
        version = "0.3",
        description = """
            Service for managing car and component pairs.
            The API contains endpoints for:
              - assigning a driver to a car
              - unassigning a driver from a car
              - getting all drivers of a car
              - setting a main driver of a car
              - removing a main driver of a car
            """,
        contact = @Contact(name = "Tomáš Marek", email = "kuresryzi@mail.muni.cz"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
@RestController
@Tag(name = "Car", description = "Microservice for managing components of cars")
@RequestMapping(path = "/cardriver")
@Validated
public class CarDriverPairController {

  private final CarDriverPairService carDriverPairService;

  /**
   * Constructor for Car driver pair Controller.
   *
   * @param carDriverPairService Car driver pair service
   */
  @Autowired
  public CarDriverPairController(CarDriverPairService carDriverPairService) {
    this.carDriverPairService = carDriverPairService;
  }

  /**
   * Calls service to assign a driver to a car.
   *
   * @param driverId Id of the driver.
   * @param carId    Id of the car.
   * @return Car entity.
   */
  @Operation(
      summary = "Assigns driver to a car",
      description = """
          Assigns driver specified by id to a car specified by id.
          Throws exception if car or driver does not exist.""",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Driver successfully assigned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = CarResponseDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Car or driver with given id does not exist.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ExError.class))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ExError.class)))})
  @PutMapping(path = "/assign",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CarResponseDto> assignDriverToCar(@Valid @RequestParam Long driverId,
                                                          @Valid @RequestParam Long carId) {
    return ResponseEntity.ok(carDriverPairService.assignDriverToCar(driverId, carId));
  }

  /**
   * Calls service to unassing a driver from a car.
   *
   * @param driverId Id of the driver.
   * @param carId    Id of the car.
   * @return Car entity.
   */
  @Operation(
      summary = "Unassigns driver from a car",
      description = """
          Unassigns driver specified by id from a car specified by id.
          Throws exception if car or driver does not exist.""",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Driver successfully unassigned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = CarResponseDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Car or driver with given id does not exist.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ExError.class))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ExError.class)))})
  @PutMapping(path = "/unassign",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CarResponseDto> unassignDriverFromCar(@Valid @RequestParam Long driverId,
                                                              @Valid @RequestParam Long carId) {
    return ResponseEntity.ok(carDriverPairService.unassignDriverFromCar(driverId, carId));
  }

  /**
   * Calls service to return all drivers assigned to a car.
   *
   * @param carId Id of the car.
   * @return List of drivers.
   */
  @Operation(
      summary = "Returns all drivers of given car",
      description = "Returns all existing cars. ",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Drivers successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = List.class, subTypes = {DriverDto.class}),
                  examples = @ExampleObject(name = "example", value = "[]"))),
          @ApiResponse(
              responseCode = "404",
              description = "Car with given id does not exist.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ExError.class))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ExError.class)))})
  @PutMapping(path = "/alldrivers",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DriverDto>> getAllDriversOfCar(@Valid @RequestParam Long carId) {
    return ResponseEntity.ok(carDriverPairService.getAllDriversOfCar(carId));
  }

  /**
   * Sets the main driver of the car identified by the given ID.
   *
   * @param carId    The ID of the car to set the main driver for.
   * @param driverId The ID of the driver to set as the main driver.
   * @return A ResponseEntity containing a CarDto object representing the updated car.
   */
  @Operation(
      summary = "Sets main driver of a car",
      description = """
          Sets driver specified by id as a main driver of a car specified by its id.
          Throws exception if car or driver with specified id does not exist.""",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Cars successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = List.class, subTypes = {CarResponseDto.class}),
                  examples = @ExampleObject(name = "example", value = "[]"))),
          @ApiResponse(
              responseCode = "404",
              description = "Car or driver with given id does not exist.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ExError.class))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ExError.class)))})
  @PutMapping(path = "/setmain",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CarResponseDto> setMainDriver(@Valid @RequestParam Long carId,
                                                      @Valid @RequestParam Long driverId) {
    return ResponseEntity.ok(carDriverPairService.setMainDriver(carId, driverId));
  }

  /**
   * Removes the main driver from the car identified by the given ID.
   *
   * @param carId The ID of the car to remove the main driver from.
   * @return A ResponseEntity containing a CarDto object representing the updated car.
   */
  @Operation(
      summary = "Removes driver as main driver.",
      description = """
          Removes driver specified by id as a main driver of a car specified by its id.
          Throws exception if car or driver with specified id does not exist.""",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Cars successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = CarResponseDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Car or driver with given id does not exist.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ExError.class))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ExError.class)))})
  @PutMapping(path = "/removemain",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CarResponseDto> removeMainDriver(@Valid @RequestParam Long carId) {
    return ResponseEntity.ok(carDriverPairService.removeMainDriver(carId));
  }

}
