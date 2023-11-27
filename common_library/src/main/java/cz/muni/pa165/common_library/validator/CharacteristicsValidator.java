package cz.muni.pa165.common_library.validator;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * A custom validator to check if all attributes in a Map of String and Integer pairs
 * contain valid characteristics as defined in the {@link ValidCharacteristics} annotation.
 */
public class CharacteristicsValidator
    implements ConstraintValidator<ValidCharacteristics, String> {

  private Set<String> validCharacteristics;

  @Override
  public void initialize(ValidCharacteristics constraintAnnotation) {
    this.validCharacteristics = Arrays.stream(Characteristic.values())
        .map(Characteristic::getShowName)
        .collect(Collectors.toSet());
  }

  /**
   * Checks if all attributes in the input Map contain valid characteristics.
   *
   * @param value  the input Map to be validated
   * @param context the validation context
   * @return true if the input Map is null or contain only valid characteristics, false otherwise
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return validCharacteristics.contains(value);
  }
}


