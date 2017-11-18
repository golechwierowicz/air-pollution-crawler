package modules.common.dao;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import modules.rest.model.Measurement;
import modules.rest.model.StationData;
import modules.rest.model.gios.Value;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;
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
    DateTime now = new DateTime();
    StationData stationData = new StationData();
    stationData.setStationId(1);
    Measurement measurement = new Measurement();
    measurement.id = 1;
    measurement.measurementName = "A";
    measurement.values = new Value[]{new Value(now, 1.0), new Value(now.minusDays(1), 2.0)};
    stationData.measurements = ImmutableList.of(measurement);
    stationData.stationName = "save";
    stationDataDao.save(stationData);
    List<StationData> result = stationDataDao.getAll();
    result.forEach(System.out::println);
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  public void getAll() throws Exception {
  }

  @Test
  public void getById() throws Exception {
  }

}