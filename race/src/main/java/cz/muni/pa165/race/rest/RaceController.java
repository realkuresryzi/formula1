package cz.muni.pa165.race.rest;


import cz.muni.pa165.common_library.dtos.Location;
import cz.muni.pa165.common_library.dtos.RaceDto;
import cz.muni.pa165.common_library.exception.ResourceNotFoundException;
import cz.muni.pa165.race.service.RaceServiceI;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing races.
 */
@OpenAPIDefinition(
    info = @Info(title = "Race Microservice",
        version = "0.3",
        description = """
            Service for managing races.
            The API contains endpoints for:
              - creating a new race
              - deleting existing race
              - getting all races
              - getting a race specified by id
              - assigning a driver as number one/two for a race
              - assigning position for a driver in a race
              - finding most suitable driver for a locations
            """,
        contact = @Contact(name = "Diana Gulčíková", email = "493356@mail.muni.cz"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
@RestController
@RequestMapping(path = "/race/")
@Validated
public class RaceController {

  private final RaceServiceI raceService;

  @Autowired
  public RaceController(RaceServiceI raceService) {
    this.raceService = raceService;
  }

  @Operation(
      summary = "Create a race",
      description = "Creates a race with specified attributes.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Race successfully added.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = RaceDto.class))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid race's attribute(s).",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ResourceNotFoundException.class))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RaceDto> createRace(
                                            @Valid @RequestBody RaceDto race) {
    return ResponseEntity.ok(raceService.postRace(race));
  }

  @Operation(
      summary = "Delete a race",
      description = "Removes a race specified by its id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Race successfully removed.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = String.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing race id.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ResourceNotFoundException.class))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @DeleteMapping(path = "/")
  public ResponseEntity<String> deleteRace(@Valid @RequestParam Long raceId) {
    return ResponseEntity.ok(raceService.deleteRace(raceId));
  }

  @Operation(
      summary = "Get a race",
      description = "Returns race specified by id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Race successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = RaceDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing race id.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ResourceNotFoundException.class))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @GetMapping(path = "/id",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RaceDto> getRace(@Valid @RequestParam Long raceId) {
    return ResponseEntity.ok(raceService.findRaceById(raceId));
  }

  @Operation(
      summary = "Get all races",
      description = "Returns all existing races.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Races successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = List.class, subTypes = {RaceDto.class}),
                  examples = @ExampleObject(name = "example", value = "[]"))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @GetMapping(path = "/",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<RaceDto>> getRaces() {
    return ResponseEntity.ok(raceService.findRaces());
  }

  @Operation(
      summary = "Assign driver as driver number one for a race",
      description = "Assign driver as driver number one for a race in a car. "
          + "All specified by their ids.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Driver successfully assigned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = RaceDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing id.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ResourceNotFoundException.class))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @PatchMapping(path = "/assignDriverOne",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RaceDto> assignDriverOne(@RequestParam Long driverOneId,
                                                 @RequestParam Long raceId,
                                                 @RequestParam Long carId) {
    return ResponseEntity.ok(raceService.assignDriverOne(driverOneId, raceId, carId));
  }

  @Operation(
      summary = "Assign driver as driver number two for a race",
      description = "Assign driver as driver number two for a race in a car. "
          + "All specified by their ids.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Driver successfully assigned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = RaceDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing id.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ResourceNotFoundException.class))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @PatchMapping(path = "/assignDriverTwo",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RaceDto> assignDriverTwo(@RequestParam Long driverTwoId,
                                                 @RequestParam Long raceId,
                                                 @RequestParam Long carId) {
    return ResponseEntity.ok(raceService.assignDriverTwo(driverTwoId, raceId, carId));
  }

  @Operation(
      summary = "Assign position for driver two",
      description = "Assign position for driver number two in a given race.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Position successfully assigned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = RaceDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing race id.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ResourceNotFoundException.class))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @PatchMapping(path = "/assignPointsDriverTwo",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RaceDto> assignPositionDriverTwo(@RequestParam Long raceId,
                                                         @RequestParam Integer position) {
    return ResponseEntity.ok(raceService.assignPositionForDriverTwo(raceId, position));
  }

  @Operation(
      summary = "Assign position for driver one",
      description = "Assign position for driver number one in a given race.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Position successfully assigned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = RaceDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing race id.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ResourceNotFoundException.class))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @PatchMapping(path = "/assignPointsDriverOne",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RaceDto> assignPositionDriverOne(@RequestParam Long raceId,
                                                         @RequestParam Integer position) {
    return ResponseEntity.ok(raceService.assignPositionForDriverOne(raceId, position));
  }

  @Operation(
      summary = "get races",
      description = "Find most suitable drivers for a given location.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Drivers successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = RaceDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing request.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = Set.class, subTypes = {Long.class}),
                  examples = @ExampleObject(name = "example", value = "[]"))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @GetMapping(path = "/findMostSuitableDriversForLocation")
  public ResponseEntity<Set<Long>> getMostSuitableDriversForLocation(
      @RequestParam Location location) {
    return ResponseEntity.ok(raceService.findMostSuitableDriver(location));
  }

  @PostMapping(path = "/fill")
  public void fillTheDataBase() {
    raceService.fillTheDataBase();
  }

  @DeleteMapping(path = "/clear")
  public void clearDatabase() {
    raceService.clearDatabase();
  }
}
