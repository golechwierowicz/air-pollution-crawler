package modules.rest.model;

import modules.rest.model.gios.AirQualityIndex;

public class LocationPoint {
  private int id;
  private double latitude;
  private double longtitude;
  private String name;
  private Long value;
  private AirQualityIndex airQualityIndex;

  public AirQualityIndex getAirQualityIndex() {
    return airQualityIndex;
  }

  public void setAirQualityIndex(AirQualityIndex airQualityIndex) {
    this.airQualityIndex = airQualityIndex;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongtitude() {
    return longtitude;
  }

  public void setLongtitude(double longtitude) {
    this.longtitude = longtitude;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getValue() {
    return value;
  }

  public void setValue(Long value) {
    this.value = value;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
