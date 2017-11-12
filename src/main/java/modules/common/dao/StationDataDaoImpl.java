package modules.common.dao;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import modules.rest.model.StationData;
import modules.rest.model.gios.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class StationDataDaoImpl implements StationDataDao {
  private HibernateSessionFactory hibernateSessionFactory;

  public StationDataDaoImpl(HibernateSessionFactory hibernateSessionFactory) { // refactor to use location point dao and write city dao and write measurement dao
    this.hibernateSessionFactory = hibernateSessionFactory;
  }

  @Override
  public int save(StationData stationData) {
    Session session = hibernateSessionFactory.getInstance().openSession();
    LocationPointDTO locationPoint = new LocationPointDTO();
    locationPoint.setId(stationData.getStationId());
    locationPoint.setStationName(stationData.stationName);
    City city = new City();
    city.setName(stationData.stationName);
    locationPoint.setCity(city);
    List<Sensor> sensors = stationData
        .measurements
        .stream()
        .map(m -> {
          Sensor sensor = new Sensor(m.id, stationData.getStationId(), null, locationPoint, ImmutableSet.of(), null);
          List<Measurement> measurements = valuesToMeasurements(sensor, Arrays.asList(m.values));
          sensor.setMeasurements(measurements);
          assert sensor.getMeasurements() != null;
          return sensor;
        }).collect(Collectors.toList());
    locationPoint.setSensors(new HashSet<>(sensors));
    Transaction transaction = session.beginTransaction();
    City existingCity;
    try {
      existingCity = (City) session
          .createQuery(" from city where c_name = :city_name")
          .setParameter("city_name", city.getName())
          .uniqueResult();
      if (existingCity == null)
        session.save(city);
    } catch (IllegalArgumentException e) {
      session.save(city);
    }
    LocationPointDTO locationPointDTO = session.get(LocationPointDTO.class, locationPoint.getId());
    if (locationPointDTO == null)
      session.save(locationPoint);

    sensors.forEach(session::save);
    sensors.forEach(sensor -> sensor.getMeasurements().forEach(session::save));
    transaction.commit();
    return locationPoint.getId();
  }

  private List<Measurement> valuesToMeasurements(final Sensor sensor, final List<Value> values) {
    if (values == null)
      return ImmutableList.of();

    return values.stream()
        .map(v ->
            new Measurement(v.getValue(), v.getDate(), sensor)
        )
        .collect(Collectors.toList());
  }

  @Override
  public List<StationData> getAll() {
    return null;
  }

  @Override
  public StationData getById(int id) {
    return null;
  }
}
