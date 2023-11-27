package cz.muni.pa165.component.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Configuration for tests.
 */
@Configuration
public class TestConfig {

  private DataSource dataSource;

  @Autowired
  public void setDataSource() {
    this.dataSource = dataSource();
  }

  /**
   * Data source for factory.
   *
   * @return data source
   */
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl("jdbc:h2:mem:component;DB_CLOSE_DELAY=-1");
    dataSource.setUsername("admin");
    dataSource.setPassword("admin");
    return dataSource;
  }

  /**
   * Custom entity manager factory.
   *
   * @return factory bean
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("cz.muni.pa165.component");
    factory.setDataSource(dataSource);

    return factory;
  }
}

