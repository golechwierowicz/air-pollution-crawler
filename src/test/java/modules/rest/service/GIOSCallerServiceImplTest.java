package modules.rest.service;

import com.google.common.collect.ImmutableList;
import modules.rest.model.*;
import modules.rest.model.gios.Sensor;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import modules.common.utils.CallService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GIOSCallerServiceImplTest {
  private GIOSCallerServiceImpl callerService;
  private CallService callService = mock(CallService.class);

  @Before
  public void before() {
    callerService = new GIOSCallerServiceImpl(callService);
  }

  @Test
  public void getPointsByCountryNone() throws Exception {
    String target = callerService.createTarget();
    when(callService.getContent(target)).thenReturn("[]");
    List<LocationPoint> pointsByCountry = callerService.getPointsByCountry("");
    assertEquals(0, pointsByCountry.size());
  }

  @Test
  public void getPointsByCountryOne() throws Exception {
    String target = callerService.createTarget();
    when(callService.getContent(target))
        .thenReturn("[{\"id\": 1, \"stationName\": \"x\", \"gegrLat\": 1.0, \"gegrLon\": 1.0, \"city\": {\"id\": 2, \"name\": \"Bialystok\", " +
            "\"commune\": {\"communeName\": \"X\", \"districtName\": \"DisctrictX\", \"provinceName\": null} " +
            "}, \"addressStreet\": \"add\" }]");
    List<LocationPoint> pointsByCountry = callerService.getPointsByCountry("");
    assertEquals(1, pointsByCountry.size());
    LocationPoint locationPoint = pointsByCountry.get(0);
    assertEquals(1, locationPoint.getId());
    assertEquals("x", locationPoint.getName());
    assertEquals(1.0, locationPoint.getLatitude(), 0.00001);
    assertEquals(1.0, locationPoint.getLongtitude(), 0.00001);
    assertEquals(null, locationPoint.getValue());
  }

  @Test
  public void getPointsByCountryMultiple() throws Exception {
    String target = callerService.createTarget();
    String first = "{\"id\": 1, \"stationName\": \"x\", \"gegrLat\": 1.0, \"gegrLon\": 1.0, \"city\": {\"id\": 2, \"name\": \"Bialystok\", " +
        "\"commune\": {\"communeName\": \"X\", \"districtName\": \"DisctrictX\", \"provinceName\": null} " +
        "}, \"addressStreet\": \"add\" }";

    String second = "{\"id\": 2, \"stationName\": \"x\", \"gegrLat\": 1.0, \"gegrLon\": 1.0, \"city\": {\"id\": 2, \"name\": \"Bialystok\", " +
        "\"commune\": {\"communeName\": \"X\", \"districtName\": \"DisctrictX\", \"provinceName\": null} " +
        "}, \"addressStreet\": \"add\" }";

    String third = "{\"id\": 3, \"stationName\": \"x\", \"gegrLat\": 1.0, \"gegrLon\": 1.0, \"city\": {\"id\": 2, \"name\": \"Bialystok\", " +
        "\"commune\": {\"communeName\": \"X\", \"districtName\": \"DisctrictX\", \"provinceName\": null} " +
        "}, \"addressStreet\": \"add\" }";

    when(callService.getContent(target))
        .thenReturn(String.format("[%s, %s, %s]", first, second, third));
    List<LocationPoint> pointsByCountry = callerService.getPointsByCountry("");
    assertEquals(3, pointsByCountry.size());
    assertEquals(1, pointsByCountry.get(0).getId());
    assertEquals(2, pointsByCountry.get(1).getId());
    assertEquals(3, pointsByCountry.get(2).getId());
  }

  @Test
  public void fetchMeasurementsForSensorsNone() throws Exception {
    List<Measurement> measurements = callerService.fetchMeasurementsForSensors(ImmutableList.of());
    assertEquals(0, measurements.size());
  }

  @Test
  public void fetchMeasurementsForSensorsNull() throws Exception {
    List<Measurement> measurements = callerService.fetchMeasurementsForSensors(null);
    assertEquals(0, measurements.size());
  }

  @Test
  public void fetchMeasurementsForSensorsOne() throws Exception {
    Sensor sensor = new Sensor();
    sensor.setId(1);
    sensor.setStationId(1);
    String measurementTarget = callerService.createMeasurementTarget(1);
    when(callService.getContentAsync(measurementTarget))
        .thenReturn(CompletableFuture.supplyAsync(() -> "{\"key\": \"ASD\", \"values\": [{\"value\": 0.5, \"date\":" +
            " \"2017-11-10 10:00:01\"}]}"));
    List<Measurement> measurements = callerService.fetchMeasurementsForSensors(ImmutableList.of(sensor));
    assertEquals(1, measurements.size());
    Measurement measurement = measurements.get(0);
    assertEquals("ASD", measurement.measurementName);
    assertEquals(1, measurement.values.length);
    assertEquals(0.5, measurement.values[0].getValue(), 0.0001);
    assertEquals(DateTime.parse("2017-11-10T10:00:01Z"), measurement.values[0].getDate());
  }

  @Test
  public void getStationDataTest() throws Exception {
    StationLocator stationLocator = new IdStationLocator(1);
    stationLocator.stationName = "test";
    String target = callerService.createMeasurementTarget(1);
    when(callService.getContentAsync(target))
        .thenReturn(CompletableFuture.supplyAsync(() -> "{\"key\": \"ASD\", \"values\": [{\"value\": 0.5, \"date\":" +
        " \"2017-11-10 10:00:01\"}]}"));
    when(callService.getContent(callerService.createStationData(1)))
        .thenReturn("[{\"id\": 1, \"stationId\": 2}]");
    StationData stationData = callerService.getStationData(stationLocator);
    assertEquals("test", stationData.stationName);
    assertEquals(1, stationData.measurements.size());
  }

}