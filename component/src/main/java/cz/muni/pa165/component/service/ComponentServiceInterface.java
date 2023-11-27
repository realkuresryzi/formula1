package cz.muni.pa165.component.service;

import cz.muni.pa165.common_library.dtos.CarComponentRequestDto;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import java.util.List;


/**
 * Interface for the car component service.
 */
public interface ComponentServiceInterface {

  public CarComponentResponseDto postCarComponent(CarComponentRequestDto carComponentDto);

  public CarComponentResponseDto getCarComponentById(Long carComponentId);

  public List<CarComponentResponseDto> getAllCarComponents();

  public String deleteById(Long carComponentId);

  void fillTheDataBase();

  void clearDatabase();
}
