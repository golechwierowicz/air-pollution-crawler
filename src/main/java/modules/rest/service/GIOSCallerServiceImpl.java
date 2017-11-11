package modules.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import modules.common.utils.CallService;
import modules.rest.exceptions.WrongStationLocatorException;
import modules.rest.model.LocationPoint;
import modules.rest.model.Measurement;
import modules.rest.model.StationData;
import modules.rest.model.StationLocator;
import modules.rest.model.gios.AirQualityIndex;
import modules.rest.model.gios.LocationPointDTO;
import modules.rest.model.gios.MeasurementDTO;
import modules.rest.model.gios.Sensor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class GIOSCallerServiceImpl extends CallerService {
  private final static Config conf = ConfigFactory.load().getConfig("rest.gios");
  private final static String HOST = String.format("http://%s", conf.getString("host"));
  private final static Logger log = LoggerFactory.getLogger(GIOSCallerServiceImpl.class.getName());
  private final CallService callService;

  public GIOSCallerServiceImpl(CallService callService) {
    this.callService = callService;
  }

  @Override
  public List<LocationPoint> getPointsByCountry(final String country) throws IOException, ExecutionException, InterruptedException {
    String target = createTarget();
    String content = callService.getContent(target);
    List<LocationPoint> locationPoints = pointsDTOToLocationPoints(content);
    List<CompletableFuture<LocationPoint>> collect = locationPoints
        .stream()
        .map(this::joinWithAirQualityIdx)
        .collect(Collectors.toList());
    return allOf(collect).get();
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
    stationData.setStationId(stationId);
    return stationData;
  }

  List<Measurement> fetchMeasurementsForSensors(List<Sensor> sensors) {
    if (sensors == null)
      return ImmutableList.of();

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JodaModule());
    return sensors
        .stream()
        .map(sensor -> new ImmutablePair<>(sensor, callService.getContentAsync(createMeasurementTarget(sensor.getId()))))
            .map(p -> new ImmutablePair<>(p.left, p.right.join()))
            .map(p -> {
              String m = p.right;
              Sensor sensor = p.left;
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
              measurement.id = sensor.getId();
              return measurement;
            }).collect(Collectors.toList());
  }

  String createStationData(int stationId) {
    return String.format("%s/%s/%d", HOST, "station/sensors", stationId);
  }

  private List<Sensor> mapSensorsDTOToSensors(String content) {
    if (content == null)
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
    if (content == null)
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

  String airQualityIdxTarget(int id) {
    return String.format("%s/%s/%d", HOST, "aqindex/getIndex", id);
  }

  private CompletableFuture<LocationPoint> joinWithAirQualityIdx(LocationPoint locationPoint) {
    CompletableFuture<String> contentAsync = callService.getContentAsync(airQualityIdxTarget(locationPoint.getId()));
    if (contentAsync == null)
      return CompletableFuture.supplyAsync(() -> locationPoint);
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JodaModule());
    return contentAsync.thenApply((aqidx) -> {
      try {
        AirQualityIndex airQualityIndex = mapper.readValue(aqidx, AirQualityIndex.class);
        locationPoint.setAirQualityIndex(airQualityIndex);
      } catch (IOException e) {
        log.error("Cannot read air quality idx", e);
      }
      return locationPoint;
    });
  }

  private <T> CompletableFuture<List<T>> allOf(List<CompletableFuture<T>> futuresList) {
    CompletableFuture<Void> allFuturesResult =
        CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[futuresList.size()]));
    return allFuturesResult.thenApply(v ->
        futuresList.stream().
            map(CompletableFuture::join).
            collect(Collectors.<T>toList())
    );
  }
}