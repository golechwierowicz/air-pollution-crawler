package modules.common.dao;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import modules.rest.model.gios.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryImpl implements HibernateSessionFactory {
  private static Config config = ConfigFactory.load().getConfig("db");
  private SessionFactory configuration = new Configuration()
      .setProperty("hibernate.connection.username", config.getString("user"))
      .setProperty("hibernate.connection.password", config.getString("password"))
      .setProperty("hibernate.connection.driver_class", config.getString("driver"))
      .setProperty("hibernate.connection.url", config.getString("url"))
      .setProperty("hibernate.dialect", config.getString("dialect"))
      .setProperty("hibernate.hbm2ddl.auto", "update")
      .setProperty("hibernate.jdbc.time_zone", "UTC")
      .addAnnotatedClass(LocationPointDTO.class)
      .addAnnotatedClass(City.class)
      .addAnnotatedClass(Sensor.class)
      .addAnnotatedClass(Parameter.class)
      .addAnnotatedClass(Measurement.class)
      .buildSessionFactory();

  @Override
  public SessionFactory getInstance() {
    return configuration;
  }
}
