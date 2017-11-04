package modules.rest.service;

import modules.rest.model.LocationPoint;
import modules.rest.model.StationData;
import modules.rest.model.StationLocator;

import java.io.IOException;
import java.util.List;

public abstract class CallerService {
  public abstract List<LocationPoint> getPointsByCountry(final String country) throws IOException;

  public abstract StationData getStationData(final StationLocator stationLocator);
}