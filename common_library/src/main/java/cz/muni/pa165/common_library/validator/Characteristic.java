package cz.muni.pa165.common_library.validator;

/**
 * Represents characteristics of F1 drivers.
 */
public enum Characteristic {
  TECHNICAL_KNOWLEDGE("technical_knowledge"),
  AGGRESSIVENESS("aggressiveness"),
  CONSISTENCY("consistency"),
  REACTIONS("reactions"),
  FITNESS("fitness"),
  TEAMWORK("teamwork"),
  ADAPTABILITY("adaptability");

  private final String showName;

  Characteristic(String showName) {
    this.showName = showName;
  }

  public String getShowName() {
    return showName;
  }
}
