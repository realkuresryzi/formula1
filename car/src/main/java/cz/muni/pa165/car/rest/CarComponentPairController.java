package cz.muni.pa165.car.rest;

import cz.muni.pa165.car.service.CarComponentPairService;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.exception.ExError;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for Car and Component pairs.
 */
@OpenAPIDefinition(
    info = @Info(title = "Car Microservice",
        version = "0.3",
        description = """
            Service for managing car and component pairs.
            The API contains endpoints for:
              - adding a component to a car
              - removing a component from a car
              - getting all components of a car
              - getting all spare components for a car and component
            """,
        contact = @Contact(name = "Tomáš Marek", email = "kuresryzi@mail.muni.cz"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
@RestController
@Tag(name = "Car", description = "Microservice for managing components of cars")
@RequestMapping(path = "/carcomponent")
@Validated
public class CarComponentPairController {

  private final CarComponentPairService carComponentService;
  private final CommonDbGetter dbGetter;

  /**
   * Constructor for Car component pair Controller.
   *
   * @param carComponentService Car component pair service
   * @param dbGetter            dbGetter
   */
  @Autowired
  public CarComponentPairController(CarComponentPairService carComponentService,
                                    CommonDbGetter dbGetter) {
    this.carComponentService = carComponentService;
    this.dbGetter = dbGetter;
  }

  /**
   * Calls service to add a component to a car.
   *
   * @param componentId Id of the component.
   * @param carId       Id of the car.
   * @return Car entity.
   */
  @Operation(
      summary = "Add component to a car",
      description = """
          Adds a component specified by its id to a car specified by its id.
          Throws exception if component or car with given id does not exist.""",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Component successfully added.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = CarResponseDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Car or component with given id does not exist.",
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
  @PutMapping(
      path = "/addcomponent",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CarResponseDto> addComponent(@Valid @RequestParam Long componentId,
                                                     @Valid @RequestParam Long carId) {
    return ResponseEntity.ok(carComponentService.addComponent(componentId, carId));
  }

  /**
   * Calls service to remove a component from a car.
   *
   * @param componentId Id of the component.
   * @param carId       Id of the car.
   * @return Car entity.
   */
  @Operation(
      summary = "Remove component from a car",
      description = """
          Removes a component specified by its id from a car specified by its id.
          Throws exception if component or car with given id does not exist, 
          or if given component is not a part of given car.""",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Component successfully removed.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = CarResponseDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = """
                  Car or component with given id does not exist, or given component
                  is not a part of given car.""",
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
  @PutMapping(path = "/removecomponent",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CarResponseDto> removeComponent(@Valid @RequestParam Long componentId,
                                                        @Valid @RequestParam Long carId) {
    return ResponseEntity.ok(carComponentService.removeComponent(componentId, carId));
  }

  /**
   * Calls service to get all components of a car.
   *
   * @param carId Id of the car.
   * @return List of components.
   */
  @Operation(
      summary = "Get all components of a car",
      description = """
          Returns all components assigned to a given car. 
          Throws an exception if given car does not exist.""",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Component successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = List.class, subTypes = {CarComponentResponseDto.class}),
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
  @GetMapping(
      path = "/getcomponents",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CarComponentResponseDto>> getAllComponentsOfCar(
      @Valid @RequestParam Long carId) {
    return ResponseEntity.ok(carComponentService.getAllComponentsOfCar(carId));
  }

  /**
   * For a given car ID and a component ID of that car returns a list of components
   * of the same name.
   *
   * @param carId       Id of a car.
   * @param componentId Id of a component of the given car.
   * @return list of all spare components of the same name.
   */
  @Operation(
      summary = "Get all spare components for given component.",
      description = """
          Returns all spare components for a given component.
          Throws an exception if component does not exist.""",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Components successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = List.class, subTypes = {CarComponentResponseDto.class}),
                  examples = @ExampleObject(name = "example", value = "[]"))),
          @ApiResponse(
              responseCode = "404",
              description = "Component with given id does not exist.",
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
  @GetMapping(
      path = "/getsparecomponents",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CarComponentResponseDto>> getAllSpareCarComponents(
      @RequestParam Long carId,
      @RequestParam Long componentId) {
    return ResponseEntity.ok(carComponentService.getAllSpareCarComponents(
        carId, componentId));
  }


}
