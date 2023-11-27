package cz.muni.pa165.car.rest;

import cz.muni.pa165.car.service.CarService;
import cz.muni.pa165.common_library.dtos.CarRequestDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for car.
 */
@OpenAPIDefinition(
    info = @Info(title = "Car Microservice",
        version = "0.3",
        description = """
            Service for managing car and component pairs.
            The API contains endpoints for:
              - adding a car
              - removing a car
              - getting all cars
              - getting a car by id
            """,
        contact = @Contact(name = "Tomáš Marek", email = "kuresryzi@mail.muni.cz"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
@RestController
@Tag(name = "Car", description = "Microservice for managing cars")
@RequestMapping(path = "/car")
@Validated
public class CarController {

  private final CarService carService;

  /**
   * Constructor for Car Controller.
   *
   * @param carService Car service
   */
  @Autowired
  public CarController(CarService carService) {
    this.carService = carService;
  }

  /**
   * Creates a new car.
   *
   * @param carRequestDto the DTO representing the car to be created
   * @return a ResponseEntity containing the DTO of the created car
   */
  @Operation(
      summary = "Creates a new car.",
      description = "Creates a new car.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Car successfully created.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = CarResponseDto.class))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid car's attribute(s).",
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
  @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CarResponseDto> createCar(@Valid @RequestBody CarRequestDto carRequestDto) {
    return ResponseEntity.ok(carService.postCar(carRequestDto));
  }

  /**
   * Deletes a car by its ID.
   *
   * @param carId the ID of the car to be deleted
   * @return a ResponseEntity containing a message indicating whether the car was deleted or not
   */
  @Operation(
      summary = "Deletes a car specified by its id.",
      description = """
          Deletes car specified by its id.
          Throws an exception if car does not exist.""",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Car successfully deleted.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = String.class))),
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
  @DeleteMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> deleteCar(@Valid @RequestParam Long carId) {
    return ResponseEntity.ok(carService.deleteById(carId));
  }

  /**
   * Retrieves a car by its ID.
   *
   * @param carId the ID of the car to retrieve
   * @return a ResponseEntity containing the DTO of the retrieved car
   */
  @Operation(
      summary = "Get a car specified by its id.",
      description = """
          Returns car specified by its id.
          Throws an exception if car does not exist.""",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Car successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = CarResponseDto.class))),
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
  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CarResponseDto> getCar(@Valid @RequestParam Long carId) {
    return ResponseEntity.ok(carService.getCarById(carId));
  }

  /**
   * Retrieves a list of all cars.
   *
   * @return a ResponseEntity containing a list of DTOs representing all cars
   */
  @Operation(
      summary = "Get all existing car",
      description = "Returns all existing cars. ",
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
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ExError.class)))})
  @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CarResponseDto>> getAllCars() {
    return ResponseEntity.ok(carService.getAllCars());
  }

  @PostMapping(path = "/fill")
  public void fillTheDataBase() {
    carService.fillTheDataBase();
  }

  @DeleteMapping(path = "/clear")
  public void clearDatabase() {
    carService.clearDatabase();
  }

}
