package cz.muni.pa165.common_library.validator;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This annotation is used to validate that the provided map of characteristics
 * contains only valid characteristics, i.e. characteristics that are specified
 * in the list of allowed characteristics provided as an argument to the
 * annotation.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = CharacteristicsValidator.class)
public @interface ValidCharacteristics {

  /**
   * The list of allowed characteristics.
   *
   * @return the list of allowed characteristics.
   */
  String[] value() default {};

  /**
   * The error message to be used if the validation fails.
   *
   * @return the error message.
   */
  String message() default "Invalid characteristic";

  /**
   * Wtf.
   *
   * @return lol.
   */
  Class<?>[] groups() default {};

  /**
   * Wtf.
   *
   * @return lol2.
   */
  Class<? extends Payload>[] payload() default {};

}

