package cz.muni.pa165.authentication.service;

import cz.muni.pa165.authentication.api.ApiKeyDto;
import cz.muni.pa165.common_library.user.Role;
import java.util.List;
import java.util.Set;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * Api Key service.
 */
public interface ApiKeyService {

  /**
   * Creates api key from session.
   *
   * @param client session holder
   * @param user login user
   * @return created api key
   * @throws IllegalAccessException if invalid authorization.
   */
  ApiKeyDto createApiKey(OAuth2AuthorizedClient client, OidcUser user)
      throws IllegalAccessException;

  /**
   * Return sets of roles for given api key.
   *
   * @param apiKey apikey
   * @return sets of roles for given api key.
   */
  Set<Role> isAuthenticated(String apiKey);

  /**
   * Deletes given api key if admin scope.
   *
   * @param apiKey deleted api key.
   * @param client session holder.
   * @return message.
   */
  String delete(String apiKey, OAuth2AuthorizedClient client);

  /**
   * Deletes all api keys.
   *
   * @param client session holder
   * @return message
   */
  String deleteAll(OAuth2AuthorizedClient client);

  /**
   * Gets all existing api keys.
   *
   * @param client session holder
   * @return all api keys created.
   */
  List<ApiKeyDto> getAll(OAuth2AuthorizedClient client);

  /**
   * Grants roles for the api key.
   *
   * @param apiKey api key to be given roles
   * @param roles roles to give
   * @param client session holder
   * @return updated api key
   */
  ApiKeyDto setRoles(String apiKey, Set<Role> roles, OAuth2AuthorizedClient client);

  /**
   * Gets e-mails from all managers.
   *
   * @return e-mails
   */
  Set<String> findAllManagers();
}
