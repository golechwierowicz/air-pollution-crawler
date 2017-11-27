package modules.common.dao;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import modules.rest.model.LocationPoint;
import modules.rest.model.Measurement;
import modules.rest.model.StationData;
import modules.rest.model.gios.City;
import modules.rest.model.gios.Value;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StationDataDaoImplTest {
  @Before
  public void before() {
    Session session = new HibernateSessionFactoryImpl()
        .getInstance()
        .openSession();
    Transaction transaction = session.beginTransaction();
    ImmutableSet<String> tables = ImmutableSet.of("sensor", "location_point", "parameter", "measurement", "city");
    tables.forEach(t -> {
      session
          .createNativeQuery("DROP TABLE " + t + " CASCADE")
          .executeUpdate();
    });
    transaction.commit();
  }

  @Test
  public void save() throws Exception {
    StationDataDao stationDataDao = new StationDataDaoImpl(new HibernateSessionFactoryImpl());
    DateTime now = new DateTime(DateTimeZone.UTC);
    StationData stationData = new StationData();
    stationData.setStationId(1);
    Measurement measurement = new Measurement();
    measurement.id = 1;
    measurement.measurementName = "A";
    measurement.values = new Value[]{new Value(now, 1.0), new Value(now.minusDays(1), 2.0)};
    stationData.measurements = ImmutableList.of(measurement);
    stationData.stationName = "save";
    City city = new City();
    city.setName(stationData.stationName);
    stationData.city = city;
    stationDataDao.save(stationData, new LocationPoint());
    List<StationData> result = stationDataDao.getAll();
    assertNotNull(result);
    assertEquals(1, result.size());
    StationData stationDataResult = result.get(0);
    assertEquals(stationData, stationDataResult);
  }

  @Test
  public void getAll() throws Exception {
  }

  @Test
  public void getById() throws Exception {
  }

}