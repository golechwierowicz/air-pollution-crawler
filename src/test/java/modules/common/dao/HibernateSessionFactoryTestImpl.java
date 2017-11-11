package modules.common.dao;

import modules.rest.model.gios.City;
import modules.rest.model.gios.LocationPointDTO;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryTestImpl implements HibernateSessionFactory {
  @Override
  public SessionFactory getInstance() {
    Configuration configuration = new Configuration();
    configuration
        .addAnnotatedClass(LocationPointDTO.class)
        .addAnnotatedClass(City.class);

    configuration.setProperty("hibernate.dialect",
        "org.hibernate.dialect.H2Dialect");
    configuration.setProperty("hibernate.connection.driver_class",
        "org.h2.Driver");
    configuration.setProperty("hibernate.connection.url", "jdbc:h2:mem:./test");
    configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
    return configuration.buildSessionFactory();
  }
}
