package cz.muni.pa165.component.service;

import cz.muni.pa165.common_library.dtos.CarComponentRequestDto;
import cz.muni.pa165.common_library.dtos.CarComponentResponseDto;
import cz.muni.pa165.common_library.dtos.CarResponseDto;
import cz.muni.pa165.common_library.exception.DatabaseException;
import cz.muni.pa165.common_library.knot.CommonDbGetter;
import cz.muni.pa165.component.data.model.CarComponent;
import cz.muni.pa165.component.data.repository.ComponentRepositoryInterface;
import cz.muni.pa165.component.mapper.CarComponentMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service for component initialization.
 */
@Service
public class ComponentService implements ComponentServiceInterface {

  private final ComponentRepositoryInterface componentRepository;
  private final CarComponentMapper carComponentMapper;
  private final CommonDbGetter dbGetter;
  private static final String MESSAGE_TEXT_1 = """
      Dear Manager,
      
      this is an autogenerated notification from the application.
      Be informed, that there is now new component in the stock:
      
      """;
  private static final String MESSAGE_TEXT_2 = """
      
      Log into the application to include this component in a car.
      
      Sincerely,
      FormulaOne Dev Team
      """;

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  ComponentService(ComponentRepositoryInterface componentRepository,
                   CarComponentMapper carComponentMapper,
                   CommonDbGetter dbGetter) {
    this.componentRepository = componentRepository;
    this.carComponentMapper = carComponentMapper;
    this.dbGetter = dbGetter;
  }

  /**
   * Create new component.
   *
   * @param carComponentDto dto to create it from
   * @return created component
   */
  public CarComponentResponseDto postCarComponent(CarComponentRequestDto carComponentDto) {
    var returnObject =  carComponentMapper.carComponentConverter(componentRepository.save(
        carComponentMapper.carComponentDtoConverterWithoutId(carComponentDto)));

    Optional<Set<String>> optionalEmails = dbGetter.getManagerEmails();
    System.out.println(optionalEmails.map(Set::size).orElse(0) + " emails retrieved");
    if (optionalEmails.isEmpty()) {
      return returnObject;
    }
    var emails = optionalEmails.get();
    for (var email : emails) {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("f1pa165@gmail.com");
      message.setTo(email);
      message.setSubject("New component on stock!");
      message.setText(MESSAGE_TEXT_1
          + "Name: " + carComponentDto.getName() + "\n"
          + "Manufacturer: " + carComponentDto.getManufacturer() + "\n"
          + "Price: " + carComponentDto.getPrice() + "\n"
          + MESSAGE_TEXT_2);
      mailSender.send(message);
      System.out.println("Email sent to " + email);
    }
    return returnObject;
  }

  /**
   * Finds and returns car component by id.
   *
   * @param carComponentId id of the component.
   * @return found car component.
   */
  @Transactional(readOnly = true)
  public CarComponentResponseDto getCarComponentById(Long carComponentId) {
    return carComponentMapper.carComponentConverter(
        componentRepository.findById(carComponentId).orElseThrow(
          () -> new DatabaseException("Component with id" + carComponentId + "was not found.")));
  }

  /**
   * Get all the car components in the repository.
   *
   * @return list of stored car components.
   */
  @Transactional(readOnly = true)
  public List<CarComponentResponseDto> getAllCarComponents() {
    return componentRepository.findAll()
        .stream()
        .map(carComponentMapper::carComponentConverter)
        .toList();
  }

  /**
   * Removes car component from repository based on the id.
   *
   * @param carComponentId of the car component for removal.
   */
  @Transactional
  public String deleteById(Long carComponentId) {
    // search all cars if the component is not being used.
    var response = dbGetter.getAllCars();
    if (response.isPresent()) {
      List<CarResponseDto> cars = response.get();
      for (CarResponseDto car : cars) {
        if (car.getComponentIdsNames().contains(carComponentId)) {
          throw new DatabaseException("Component with id " + carComponentId
          + " cannot be removed as it is used by car with id " + car.getId());
        }
      }
    }
    // if no car has component assigned, remove it
    componentRepository.findById(carComponentId).orElseThrow(
        () -> new DatabaseException("Component with id " + carComponentId + " was not found."));
    componentRepository.deleteById(carComponentId);
    return "Component with id " + carComponentId + " was successfully deleted.";
  }

  /**
   * Saves a few car components.
   */
  @Transactional
  public void fillTheDataBase() {
    // name, weight, price, manufacturer
    // Tyres
    var softTyres = new CarComponent(0L,
        "Soft Tyres",
        BigDecimal.valueOf(9.5),
        BigDecimal.valueOf(2700),
        "Pirelli");
    var mediumTyres = new CarComponent(1L,
        "Medium Tyres",
        BigDecimal.valueOf(9.7),
        BigDecimal.valueOf(3000),
        "Pirelli");
    var hardTyres = new CarComponent(2L,
        "Hard Tyres",
        BigDecimal.valueOf(10.1),
        BigDecimal.valueOf(2800),
        "Pirelli");
    var intermediateTyres = new CarComponent(3L,
        "Intermediate Tyres",
        BigDecimal.valueOf(10.9),
        BigDecimal.valueOf(4000),
        "Pirelli");
    var heavyWetTyres = new CarComponent(4L,
        "Heavy Wet Tyres",
        BigDecimal.valueOf(11.5),
        BigDecimal.valueOf(3700),
        "Pirelli");
    componentRepository.save(softTyres);
    componentRepository.save(mediumTyres);
    componentRepository.save(hardTyres);
    componentRepository.save(intermediateTyres);
    componentRepository.save(heavyWetTyres);

    // Engines
    var engineOne = new CarComponent(3L,
        "1.6 litre four-stroke turbocharged engine #1",
        BigDecimal.valueOf(250),
        BigDecimal.valueOf(150000),
        "Ferrari");
    var engineTwo = new CarComponent(4L,
        "1.6 litre four-stroke turbocharged engine #2",
        BigDecimal.valueOf(250),
        BigDecimal.valueOf(150000),
        "Ferrari");
    var engineThree = new CarComponent(5L,
        "1.6 litre four-stroke turbocharged engine #3",
        BigDecimal.valueOf(250),
        BigDecimal.valueOf(150000),
        "Ferrari");
    componentRepository.save(engineOne);
    componentRepository.save(engineTwo);
    componentRepository.save(engineThree);

    // Gearboxes
    var gearboxOne = new CarComponent(6L,
        "highly automated semi-automatic sequential gearbox",
        BigDecimal.valueOf(70),
        BigDecimal.valueOf(12000),
        "Ferrari");
    var gearboxTwo = new CarComponent(7L,
        "highly automated semi-automatic sequential gearbox",
        BigDecimal.valueOf(80),
        BigDecimal.valueOf(15000),
        "Ferrari");
    var gearboxThree = new CarComponent(8L,
        "highly automated semi-automatic sequential gearbox",
        BigDecimal.valueOf(50),
        BigDecimal.valueOf(17000),
        "Ferrari");
    componentRepository.save(gearboxOne);
    componentRepository.save(gearboxTwo);
    componentRepository.save(gearboxThree);
  }

  @Transactional
  public void clearDatabase() {
    componentRepository.deleteAll();
  }
}