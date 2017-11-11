package modules.crawler.service.dao;

import modules.common.dao.HibernateSessionFactoryImpl;
import modules.common.dao.LocationPointDao;
import modules.rest.model.gios.City;
import modules.rest.model.gios.Commune;
import modules.rest.model.gios.LocationPointDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LocationPointDaoTest {
  @Before
  public void before() {
    Session session = new HibernateSessionFactoryImpl()
        .getInstance()
        .openSession();
    Transaction transaction = session.beginTransaction();
    session
        .createNativeQuery("TRUNCATE TABLE city CASCADE")
        .executeUpdate();
    session
        .createNativeQuery("TRUNCATE TABLE location_point CASCADE")
        .executeUpdate();
    transaction.commit();
  }

  @Test
  public void save() throws Exception {
    LocationPointDao locationPointDao = new LocationPointDao(new HibernateSessionFactoryImpl());
    LocationPointDTO locationPointDTO = new LocationPointDTO();
    locationPointDTO.setId(1);
    locationPointDTO.setAddressStreet("sample_address");
    DateTime dateTimeBegin = DateTime.now().minusDays(1);
    DateTime dateTimeEnd = DateTime.now();
    locationPointDTO.setDateEnd(dateTimeEnd);
    locationPointDTO.setDateStart(dateTimeBegin);
    locationPointDTO.setGegrLat(1.0);
    locationPointDTO.setGegrLon(2.0);
    locationPointDTO.setStationName("sample_station_name");
    City city = new City();
    city.setId(1);
    city.setName("sample_commune");
    Commune commune = new Commune();
    commune.setCommuneName("commune_name");
    commune.setDistrictName("districtive_name");
    commune.setProvinceName("province_name");
    city.setCommune(commune);
    locationPointDTO.setCity(city);
    int id = locationPointDao.save(locationPointDTO);
    LocationPointDTO byId = locationPointDao.getById(id);
    assertNotNull(byId);
    assertEquals(1, locationPointDao.getAll().size());
    assertEquals("sample_address", byId.getAddressStreet());
  }

}