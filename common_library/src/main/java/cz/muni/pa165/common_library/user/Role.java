package cz.muni.pa165.common_library.user;

import lombok.Getter;

/**
 * Roles for the system.
 */
public enum Role {
  USER("USER"),
  ADMIN("ADMIN"),
  MANAGER("MANAGER"),
  ENGINEER("ENGINEER");

  @Getter
  public final String value;

  Role(String value) {
    this.value = value;
  }


}
