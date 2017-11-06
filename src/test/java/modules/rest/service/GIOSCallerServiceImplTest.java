package modules.rest.service;

import org.junit.Before;
import org.junit.Test;
import utils.CallService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GIOSCallerServiceImplTest {
  @Test
  public void getPointsByCountry() throws Exception {
    CallService callService = mock(CallService.class);
    String target = "";
    when(callService.getContent(target)).thenReturn("[]");
    assertEquals("[]", callService.getContent(target));
  }

  @Test
  public void getStationData() throws Exception {
  }

  @Test
  public void pointsDTOToLocationPoints() throws Exception {
  }

  @Test
  public void fetchMeasurementsForSensors() throws Exception {
  }

  @Test
  public void createMeasurementTarget() throws Exception {
  }

}