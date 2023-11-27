package cz.muni.pa165.race.rest;

import cz.muni.pa165.common_library.dtos.SeasonDto;
import cz.muni.pa165.common_library.exception.ResourceNotFoundException;
import cz.muni.pa165.race.service.SeasonServiceI;
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
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
 * Controller for managing seasons.
 */
@OpenAPIDefinition(
    info = @Info(title = "Season Microservice",
        version = "0.3",
        description = """
            Service for managing seasons.
            The API contains endpoints for:
              - creating a new season
              - deleting existing season
              - getting all created seasons
              - getting a season specified by id
              - adding a race to the specified season
            """,
        contact = @Contact(name = "Diana Gulčíková", email = "493356@mail.muni.cz"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
@RestController
@RequestMapping(path = "/season/")
@Validated
public class SeasonController {

  private final SeasonServiceI seasonService;

  @Autowired
  public SeasonController(SeasonServiceI seasonService) {
    this.seasonService = seasonService;
  }

  @Operation(
      summary = "Create a season",
      description = "Creates a season with specified attributes.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Season successfully added.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = SeasonDto.class))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid season's attribute(s).",
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
  public ResponseEntity<SeasonDto> createSeason(@Valid @RequestBody SeasonDto season) {
    return ResponseEntity.ok(seasonService.postSeason(season));
  }

  @Operation(
      summary = "Get a season",
      description = "Returns season specified by id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Season successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = SeasonDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing season id.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ResourceNotFoundException.class))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @GetMapping(path = "/id", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SeasonDto> getSeason(@RequestParam Long seasonId) {
    return ResponseEntity.ok(seasonService.getSeasonById(seasonId));
  }

  @Operation(
      summary = "Get all seasons",
      description = "Returns all existing seasons.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Seasons successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = List.class, subTypes = {SeasonDto.class}),
                  examples = @ExampleObject(name = "example", value = "[]"))),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ErrorMessage.class)))})
  @GetMapping(path = "/")
  public ResponseEntity<List<SeasonDto>> getAllSeasons() {
    return ResponseEntity.ok(seasonService.getAllSeasons());
  }

  @Operation(
      summary = "Delete season specified by id",
      description = "Removes season specified by its id.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Season successfully removed.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = String.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing season id.",
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
  public ResponseEntity<String> deleteSeason(@Valid @RequestParam Long seasonId) {
    return ResponseEntity.ok(seasonService.deleteById(seasonId));
  }

  @Operation(
      summary = "Add a race to a season",
      description = "Adds race to season, both specified by their ids.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Race successfully added to season.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = SeasonDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing id.",
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
  @PatchMapping(path = "/addRace", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SeasonDto> addRace(@Valid @RequestParam Long seasonId,
                                           @Valid @RequestParam Long raceId) {
    return ResponseEntity.ok(seasonService.addRace(seasonId, raceId));
  }

  @Operation(
      summary = "Removes a race to a season",
      description = "Removes race to season, both specified by their ids.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Race successfully removed from season.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = SeasonDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing id.",
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
  @PatchMapping(path = "/removeRace", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SeasonDto> removeRace(@Valid @RequestParam Long seasonId,
                                           @Valid @RequestParam Long raceId) {
    return ResponseEntity.ok(seasonService.removeRace(seasonId, raceId));
  }

  @PostMapping(path = "/fill")
  public void fillTheDataBase() {
    seasonService.fillTheDataBase();
  }

  @DeleteMapping(path = "/clear")
  public void clearDatabase() {
    seasonService.clearDatabase();
  }
}
