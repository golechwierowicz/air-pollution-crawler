package modules.rest.model;

import modules.rest.model.gios.City;

import java.util.List;

public class StationData {
  private int stationId;
  public String stationName;
  public List<Measurement> measurements;
  public City city;
  public int getStationId() {
    return stationId;
  }

  public void setStationId(int stationId) {
    this.stationId = stationId;
  }
}
