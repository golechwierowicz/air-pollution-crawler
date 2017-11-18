package modules.rest.model;

import com.google.common.base.MoreObjects;
import modules.rest.model.gios.City;
import java.util.List;

public class StationData {
  private int stationId;
  public String stationName;
  public List<Measurement> measurements;
  public City city;

  public StationData(int stationId, String stationName, List<Measurement> measurements, City city) {
    this.stationId = stationId;
    this.stationName = stationName;
    this.measurements = measurements;
    this.city = city;
  }

  public StationData() {
  }

  public int getStationId() {
    return stationId;
  }
  public void setStationId(int stationId) {
    this.stationId = stationId;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("stationId", stationId)
        .add("stationName", stationName)
        .add("measurements", measurements)
        .add("city", city)
        .toString();
  }
}
