package cz.muni.pa165.component.rest;

import cz.muni.pa165.common_library.dtos.CarComponentRequestDto;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.common_library.exception.ExError;
import cz.muni.pa165.component.service.ComponentServiceInterface;
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
 * Controller for managing the components.
 */
@OpenAPIDefinition(
    info = @Info(title = "Car component Microservice",
        version = "0.3",
        description = """
            Service for managing components of cars of F1 team. 
            The API contains endpoints for:
              - creating a new component
              - deleting existing component
              - getting all currently existing components
              - getting a car component specified by id
            """,
        contact = @Contact(name = "Oto Stanko", email = "493068@mail.muni.cz"),
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")))
@RestController
@RequestMapping(path = "/component")
@Validated
public class ComponentController {

  private final ComponentServiceInterface componentService;

  @Autowired
  ComponentController(ComponentServiceInterface componentFacade) {
    this.componentService = componentFacade;
  }

  @Operation(
      summary = "Create new component",
      description = "Add car component with specified attributes to the database.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Component successfully created.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = CarComponentResponseDto.class))),
          @ApiResponse(
              responseCode = "400",
              description = "Invalid component's attribute(s).",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = ExError.class))),
          @ApiResponse(responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ExError.class)))})
  @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CarComponentResponseDto> createComponent(
      @Valid @RequestBody CarComponentRequestDto carComponent) {
    return ResponseEntity.ok(componentService.postCarComponent(carComponent));
  }

  @Operation(
      summary = "Delete car component specified by id",
      description = "Removes car component specified by its id from the store.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Component successfully removed.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = String.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing component id.",
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
  @DeleteMapping(path = "/",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> deleteComponent(@Valid @RequestParam long componentId) {
    return ResponseEntity.ok(componentService.deleteById(componentId));
  }

  @Operation(
      summary = "Get car component specified by id",
      description = "Returns car component with a specific id if exists.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Component successfully returned.",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(
                      implementation = CarComponentResponseDto.class))),
          @ApiResponse(
              responseCode = "404",
              description = "Non-existing component id.",
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
  @GetMapping(path = "/id",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CarComponentResponseDto> getComponent(
      @Valid @RequestParam long componentId) {
    return ResponseEntity.ok(componentService.getCarComponentById(componentId));
  }

  @Operation(
      summary = "Get all components",
      description = "Returns all car components currently in the store.",
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
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(
                  schema = @Schema(
                      implementation = ExError.class)))})
  @GetMapping(path = "/",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CarComponentResponseDto>> getComponents() {
    return ResponseEntity.ok(componentService.getAllCarComponents());
  }

  @PostMapping(path = "/fill")
  public void fillTheDataBase() {
    componentService.fillTheDataBase();
  }

  @DeleteMapping(path = "/clear")
  public void clearDatabase() {
    componentService.clearDatabase();
  }
}
