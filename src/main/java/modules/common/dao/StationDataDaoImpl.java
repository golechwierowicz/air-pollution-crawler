package modules.common.dao;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import modules.rest.model.StationData;
import modules.rest.model.gios.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
          Sensor sensor = new Sensor(m.id, stationData.getStationId(), m.measurementName, locationPoint, ImmutableSet.of(), null);
          sensor.setName(m.measurementName);
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

    sensors.forEach(s -> {
      if(session.get(Sensor.class, s.getId()) == null)
        session.save(s);
    });
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
    Session session = hibernateSessionFactory.getInstance().openSession();

    String hql = "select lp_id, lp_station_name, lp_latitude, lp_longitude, c_id, c_commune, c_name, s_id, m_id," +
        " m_timestamp, m_value, s_name from location_point lp left outer join city ci on ci.c_id = lp.city_id " +
        "inner join sensor s on lp.lp_id = s.locpoint_id join measurement me on s.s_id = me.sensor_id";
    List list = session.createNativeQuery(hql).list();
    List<StationData> stationDataList = new ArrayList<>();
    for (Object obj : list) {
      Object[] row = (Object[]) obj;
      LocationPointDTO locationPointDTO = new LocationPointDTO((int)row[0], (String)row[1], (double)row[2], (double)row[3], null, null, null, null);
      City city = new City((int)row[4], (String)row[6], (Commune)row[5]);
      Sensor sensor = new Sensor((int)row[7], (int)row[0], (String)row[11], null, new HashSet<>(), new ArrayList<>());
      Measurement measurement = new Measurement((double)row[10], new DateTime(row[9], DateTimeZone.UTC), null);
      addToResult(locationPointDTO, city, sensor, measurement, stationDataList);
    }
    session.close();
    return stationDataList;
  }

  private void addToResult(LocationPointDTO locationPointDTO,
                           City city,
                           Sensor sensor,
                           Measurement measurement,
                           List<StationData> stationDataList) {
    Stream<StationData> stationDataStream = stationDataList
        .stream()
        .filter(s -> s.getStationId() == locationPointDTO.getId());
    Optional<Boolean> a = stationDataStream.findFirst().map(s -> {
      Optional<modules.rest.model.Measurement> first = s
          .measurements
          .stream()
          .filter(m -> m.id == sensor.getId())
          .findFirst();
      if (first.isPresent()) {
        List<Value> valuesList = new ArrayList<>();
        valuesList.addAll(Arrays.asList(first.get().values));
        valuesList.add(new Value(measurement.getTimestamp(), measurement.getValue()));
        first.get().values = valuesList.toArray(new Value[valuesList.size()]);
      } else s.measurements.add(new modules.rest.model.Measurement(sensor.getId(),
          "", // FixMe: change to get params
          new Value[]{new Value(measurement.getTimestamp(), measurement.getValue())}));
      return true;
    });
    if(!a.isPresent())
      stationDataList.add(new StationData(locationPointDTO.getId(),
        locationPointDTO.getStationName(),
        ImmutableList.of(new modules.rest.model.Measurement(sensor.getId(),
            sensor.getName(),
            new Value[]{new Value(measurement.getTimestamp(), measurement.getValue())})),
        city));
  }

  @Override
  public StationData getById(int id) {
    return null;
  }
}
