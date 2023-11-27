package cz.muni.pa165.authentication.rest;

import cz.muni.pa165.authentication.api.ApiKeyDto;
import cz.muni.pa165.authentication.service.ApiKeyService;
import cz.muni.pa165.common_library.user.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Authentication controller.
 */
@RestController
@RequestMapping(path = "/authenticate")
public class AuthenticationController {

  @Autowired
  ApiKeyService apiKeyServiceImpl;

  @PostMapping(path = "/create/apikey")
  public ResponseEntity<ApiKeyDto> createApiKey(
      @RegisteredOAuth2AuthorizedClient @Schema(hidden = true)
          OAuth2AuthorizedClient oauth2Client,
      @AuthenticationPrincipal @Schema(hidden = true) OidcUser user) throws IllegalAccessException {

    return ResponseEntity.ok(apiKeyServiceImpl.createApiKey(oauth2Client, user));
  }

  @GetMapping(path = "/apikey")
  public ResponseEntity<Set<Role>> authenticate(@RequestParam String apiKey) {
    return ResponseEntity.ok(apiKeyServiceImpl.isAuthenticated(apiKey));
  }

  @DeleteMapping(path = "/apikey/delete")
  public ResponseEntity<String> deleteApiKeys(
      @RegisteredOAuth2AuthorizedClient @Schema(hidden = true)
          OAuth2AuthorizedClient oauth2Client,
      @RequestParam String apiKey) {
    return ResponseEntity.ok(apiKeyServiceImpl.delete(apiKey, oauth2Client));
  }

  @DeleteMapping(path = "/apikey/deleteAll")
  public ResponseEntity<String> deleteAllApiKeys(
      @RegisteredOAuth2AuthorizedClient @Schema(hidden = true)
          OAuth2AuthorizedClient oauth2Client) {
    return ResponseEntity.ok(apiKeyServiceImpl.deleteAll(oauth2Client));
  }

  @PatchMapping(path = "/apikey/grantRole")
  public ResponseEntity<ApiKeyDto> grantRoles(
      @RegisteredOAuth2AuthorizedClient @Schema(hidden = true)
          OAuth2AuthorizedClient oauth2Client,
      @RequestParam String apiKey,
      @RequestParam Set<Role> roles) {
    return ResponseEntity.ok(apiKeyServiceImpl.setRoles(apiKey, roles, oauth2Client));
  }

  @GetMapping(path = "/apikey/getAll")
  public ResponseEntity<List<ApiKeyDto>> getAll(@RegisteredOAuth2AuthorizedClient
                                                @Schema(hidden = true)
                                                    OAuth2AuthorizedClient oauth2Client) {
    return ResponseEntity.ok(apiKeyServiceImpl.getAll(oauth2Client));
  }

  @GetMapping(path = "/apikey/getManagerEmails")
  public ResponseEntity<Set<String>> getAllManagerEmails() {
    return ResponseEntity.ok(apiKeyServiceImpl.findAllManagers());
  }
}
