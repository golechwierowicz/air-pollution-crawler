package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "location_point")
public class LocationPointDTO {
  @Id
  @Column(name = "lp_id")
  private int id;
  @Column(name = "lp_station_name")
  private String stationName;
  @Column(name = "lp_latitude")
  private double gegrLat;
  @Column(name = "lp_longitude")
  private double gegrLon;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "c_id", nullable = false)
  private City city;
  @Column(name = "lp_street_address")
  private String addressStreet;
  @JsonIgnore
  @Transient
  private DateTime dateStart;
  @JsonIgnore
  @Transient
  private DateTime dateEnd;
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "location_point")
  @JsonIgnore
  private Set<Sensor> sensors;

  public LocationPointDTO() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getStationName() {
    return stationName;
  }

  public void setStationName(String stationName) {
    this.stationName = stationName;
  }

  public double getGegrLat() {
    return gegrLat;
  }

  public void setGegrLat(double gegrLat) {
    this.gegrLat = gegrLat;
  }

  public double getGegrLon() {
    return gegrLon;
  }

  public void setGegrLon(double gegrLon) {
    this.gegrLon = gegrLon;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public String getAddressStreet() {
    return addressStreet;
  }

  public void setAddressStreet(String addressStreet) {
    this.addressStreet = addressStreet;
  }

  public DateTime getDateStart() {
    return dateStart;
  }

  public void setDateStart(DateTime dateStart) {
    this.dateStart = dateStart;
  }

  public DateTime getDateEnd() {
    return dateEnd;
  }

  public void setDateEnd(DateTime dateEnd) {
    this.dateEnd = dateEnd;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("stationName", stationName)
        .add("gegrLat", gegrLat)
        .add("gegrLon", gegrLon)
        .add("city", city)
        .add("addressStreet", addressStreet)
        .add("dateStart", dateStart)
        .add("dateEnd", dateEnd)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LocationPointDTO that = (LocationPointDTO) o;
    return id == that.id &&
        Double.compare(that.gegrLat, gegrLat) == 0 &&
        Double.compare(that.gegrLon, gegrLon) == 0 &&
        Objects.equal(stationName, that.stationName) &&
        Objects.equal(city, that.city) &&
        Objects.equal(addressStreet, that.addressStreet) &&
        Objects.equal(dateStart, that.dateStart) &&
        Objects.equal(dateEnd, that.dateEnd);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, stationName, gegrLat, gegrLon, city, addressStreet, dateStart, dateEnd);
  }
}