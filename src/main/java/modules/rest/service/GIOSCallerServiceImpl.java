package modules.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import modules.rest.exceptions.WrongStationLocatorException;
import modules.rest.model.LocationPoint;
import modules.rest.model.Measurement;
import modules.rest.model.StationData;
import modules.rest.model.StationLocator;
import modules.rest.model.gios.LocationPointDTO;
import modules.rest.model.gios.MeasurementDTO;
import modules.rest.model.gios.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CallService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GIOSCallerServiceImpl extends CallerService {
  private final static Config conf = ConfigFactory.load().getConfig("rest.gios");
  private final static String HOST = String.format("http://%s", conf.getString("host"));
  private final static Logger log = LoggerFactory.getLogger(GIOSCallerServiceImpl.class.getName());
  private final CallService callService;

  public GIOSCallerServiceImpl(CallService callService) {
    this.callService = callService;
  }

  @Override
  public List<LocationPoint> getPointsByCountry(final String country) throws IOException {
    String target = createTarget();
    String content = callService.getContent(target);
    return pointsDTOToLocationPoints(content);
  }

  @Override
  public StationData getStationData(StationLocator stationLocator) {
    Optional<Integer> stationIdOpt = stationLocator.getStationId();
    if (!stationIdOpt.isPresent())
      throw new WrongStationLocatorException("Expected int based locator, received string based");
    int stationId = stationIdOpt.get();
    String target = createStationData(stationId);
    String content = callService.getContent(target);
    List<Sensor> sensorsForStation = mapSensorsDTOToSensors(content);
    List<Measurement> measurements = fetchMeasurementsForSensors(sensorsForStation);
    StationData stationData = new StationData();
    stationData.measurements = measurements;
    stationData.stationName = stationLocator.stationName;
    return stationData;
  }

  List<Measurement> fetchMeasurementsForSensors(List<Sensor> sensors) {
    if(sensors == null)
      return ImmutableList.of();

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JodaModule());
    return sensors
        .stream()
        .map(sensor -> callService.getContentAsync(createMeasurementTarget(sensor.getId())))
        .map(CompletableFuture::join)
        .map(m -> {
          Measurement measurement = new Measurement();
          MeasurementDTO measurementDTO;
          try {
            measurementDTO = mapper.readValue(m, MeasurementDTO.class);
          } catch (IOException e) {
            log.error("Err when mapping measurement dto", e);
            return new Measurement();
          }
          measurement.values = measurementDTO.getValues();
          measurement.measurementName = measurementDTO.getKey();
          return measurement;
        }).collect(Collectors.toList());
  }

  String createStationData(int stationId) {
    return String.format("%s/%s/%d", HOST, "station/sensors", stationId);
  }

  private List<Sensor> mapSensorsDTOToSensors(String content) {
    if(content == null)
      return ImmutableList.of();
    ObjectMapper mapper = new ObjectMapper();
    Sensor[] sensors = new Sensor[0];
    try {
      sensors = mapper.readValue(content, Sensor[].class);
    } catch (IOException e) {
      log.error("Err mapping to sensors", e);
    }
    return Arrays.asList(sensors);
  }

  private List<LocationPoint> pointsDTOToLocationPoints(final String content) throws IOException {
    if(content == null)
      return ImmutableList.of();
    ObjectMapper mapper = new ObjectMapper();
    LocationPointDTO[] points = mapper.readValue(content, LocationPointDTO[].class);
    return Arrays.stream(points).map(p -> {
      LocationPoint locationPoint = new LocationPoint();
      locationPoint.setName(p.getStationName());
      locationPoint.setValue(null);
      locationPoint.setLatitude(p.getGegrLat());
      locationPoint.setLongtitude(p.getGegrLon());
      locationPoint.setId(p.getId());
      return locationPoint;
    }).collect(Collectors.toList());
  }

  String createMeasurementTarget(int id) {
    return String.format("%s/%s/%d", HOST, "data/getData", id);
  }

  String createTarget() {
    return String.format("%s/%s", HOST, "station/findAll");
  }
}