package cz.muni.pa165.driver.rest;

import cz.muni.pa165.common_library.dtos.DriverAddDto;
import cz.muni.pa165.common_library.dtos.DriverResponseDto;
import cz.muni.pa165.common_library.dtos.DriverUpdateDto;
import cz.muni.pa165.common_library.exception.ExError;
import cz.muni.pa165.driver.service.DriverService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



/**
 * Rest controller for driver.
 */
@OpenAPIDefinition(
    info = @Info(title = "Driver Microservice",
        version = "0.3",
        description = """
            Service for managing drivers of F1 team.
            The API contains endpoints for:
              - signing a new driver
              - dismissing a driver
              - updating an existing driver attributes
              - getting all currently signed drivers
              - getting a signed driver specified by id
            """,
        contact = @Contact(name = "Andrej Šimurka", email = "492781@mail.muni.cz"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
@RestController
@Tag(name = "Driver", description = "Microservice for managing drivers")
@RequestMapping(path = "/driver")
public class DriverController {

  private final DriverService driverService;

  private static final String EXAMPLE_JSON = """
      {
      "name":"Fernando",
      "surname": "Alonso",
      "nationality": "Spanish",
      "characteristics" : {
                          "technical_knowledge" : 1,
                          "aggressiveness" : 1,
                          "consistency" : 1,
                          "reactions" : 1,
                          "fitness" : 1,
                          "teamwork" : 1,
                          "adaptability" : 1
                          }
      }""";

  @Autowired
  public DriverController(DriverService driverService) {
    this.driverService = driverService;
  }


  @Operation(
      summary = "Add driver to the team",
      description = "Adds the specified driver into the team. "
          + "Fails if the driver name is not unique.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Driver successfully created.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = DriverResponseDto.class))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid driver's attribute(s).",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ExError.class))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ExError.class)))})
  @PostMapping(path = "/add", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DriverResponseDto> createDriver(
      @RequestBody @Valid @Schema(
          description = "New driver attributes",
          example = EXAMPLE_JSON)
          DriverAddDto driver) {
    return ResponseEntity.ok(driverService.addDriver(driver));
  }


  @Operation(
      summary = "Update driver specified by id",
      description = "Updates attributes of specified driver, that are specified in JSON"
          + "attributes. Attributes that are not specified in the JSON will not be updated.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Attributes successfully updated.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = DriverResponseDto.class))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid driver's attribute(s).",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ExError.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Driver with given id does not exist.",
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
      path = "/update/id",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DriverResponseDto> updateDriverById(
      @Valid @RequestParam @Schema(
          description = "ID of driver to be updated")
          Long id,
      @RequestBody @Valid @Schema(
          description = "Driver new attributes",
          example = EXAMPLE_JSON)
          DriverUpdateDto driver) {
    return ResponseEntity.ok(driverService.updateDriverById(id, driver));
  }


  @Operation(
      summary = "Remove a driver specified by id",
      description = "Removes driver specified by his id from the list of drivers.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Driver successfully removed.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = DriverResponseDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Driver with given id does not exist.",
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
  @DeleteMapping(
      path = "/remove/id",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DriverResponseDto> removeDriverById(
      @Valid @RequestParam @Schema(
          description = "ID of driver to be dismissed")
          Long id) {
    return ResponseEntity.ok(driverService.removeDriverById(id));
  }


  @Operation(
      summary = "Get all drivers",
      description = "Returns all drivers that are currently signed in the team.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Drivers successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = List.class, subTypes = {DriverResponseDto.class}),
                  examples = @ExampleObject(name = "example", value = "[]"))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ExError.class)))})
  @GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DriverResponseDto>> getAllDrivers() {
    return ResponseEntity.ok(driverService.getAllDrivers());
  }

  @Operation(
      summary = "Get driver specified by id",
      description = "Returns driver with a specific id if exists.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Driver successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = DriverResponseDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Driver with given id does not exist.",
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
      path = "/get/id",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DriverResponseDto> getDriverById(
      @Valid @RequestParam @Schema(
          description = "ID of requested driver")
          Long id) {
    return ResponseEntity.ok(driverService.getDriverById(id));
  }

  @PostMapping(path = "/fill")
  public void fillTheDataBase() {
    driverService.fillTheDatabase();
  }

  @DeleteMapping(path = "/clear")
  public void clearDatabase() {
    driverService.clearDatabase();
  }
}
