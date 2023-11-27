package cz.muni.pa165.race.data.model;

import cz.muni.pa165.common_library.dtos.Location;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Race class.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "race")
public class Race implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "race_info_id")
  private RaceInfo raceInfo;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "driver_one_id")
  private RaceDriverInfo driver1;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "driver_two_id")
  private RaceDriverInfo driver2;

  /**
   * Information about race driver.
   */
  @Entity
  @Table(name = "racedriverinfo")
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class RaceDriverInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long driverId;

    private Long carId;

    private Integer finalPosition;
  }

  /**
   * Information about race.
   */
  @Entity
  @Table(name = "raceinfo")
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class RaceInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Location location;

    @NotNull
    @Column(length = 100, nullable = false)
    private String name;

    @NotNull
    private Long prizePool;
  }
}

