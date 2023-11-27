package cz.muni.pa165.authentication.api;

import cz.muni.pa165.common_library.user.Role;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Api key dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyDto {

  private Long id;

  private Date expirationDate;

  @Email
  private String email;

  private Set<Role> roles = new HashSet<>();

  private String apiKeyValue;
}
