package modules.rest.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StationData that = (StationData) o;
    return stationId == that.stationId &&
        Objects.equal(stationName, that.stationName) &&
        Objects.equal(measurements, that.measurements) &&
        Objects.equal(city, that.city);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(stationId, stationName, measurements, city);
  }
}
