package cz.muni.pa165.driver.data.model;

import cz.muni.pa165.common_library.validator.Characteristic;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Driver Class.
 */
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "driver")
public class Driver implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String surname;

  private String nationality;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "characteristics")
  @MapKeyColumn(name = "ch_name")
  private Map<String, Integer> characteristics;

  /**
   * Basic constructor when initializing driver.
   *
   * @param name        driver name
   * @param surname     driver surname
   * @param nationality driver nationality
   */
  public Driver(String name, String surname, String nationality) {
    this.name = name;
    this.surname = surname;
    this.nationality = nationality;
    this.characteristics = new HashMap<>() {
      {
        for (Characteristic c : Characteristic.values()) {
          put(c.getShowName(), 1);
        }
      }
    };
  }
}

