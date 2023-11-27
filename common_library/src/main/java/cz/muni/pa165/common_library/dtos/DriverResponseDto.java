package cz.muni.pa165.common_library.dtos;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Driver Response dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponseDto {
  private Long id;
  private String name;
  private String surname;
  private String nationality;
  private Map<String, Integer> characteristics;
}
