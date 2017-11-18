package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sensor")
public class Sensor {
  @Id
  @Column(name = "s_id")
  private int id;
  @Transient
  private int stationId;
  @Transient
  private Parameter param;
  @JsonIgnore
  @Transient
  private DateTime sensorDateStart;
  @Transient
  @JsonIgnore
  private DateTime sensorDateEnd;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "locpoint_id", nullable = false)
  @JsonIgnore
  private LocationPointDTO locationPointDTO;
  @ManyToMany()
  @JoinTable(
      name="sensor_parameter",
      joinColumns = { @JoinColumn(name="s_id")},
      inverseJoinColumns = { @JoinColumn(name="p_id")}
  )
  @JsonIgnore
  private Set<Parameter> params;
  @OneToMany(mappedBy = "sensor")
  @JsonIgnore
  private List<Measurement> measurements;
  @Column(name="s_name")
  private String name;


  public Sensor() {
  }

  public Sensor(int id, int stationId, Parameter param, LocationPointDTO locationPointDTO, Set<Parameter> params, List<Measurement> measurements) {
    this.id = id;
    this.stationId = stationId;
    this.param = param;
    this.locationPointDTO = locationPointDTO;
    this.params = params;
    this.measurements = measurements;
  }

  public Sensor(int id, int stationId, String name, LocationPointDTO locationPointDTO, Set<Parameter> params, List<Measurement> measurements) {
    this.id = id;
    this.stationId = stationId;
    this.name = name;
    this.locationPointDTO = locationPointDTO;
    this.params = params;
    this.measurements = measurements;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getStationId() {
    return stationId;
  }

  public void setStationId(int stationId) {
    this.stationId = stationId;
  }

  public Parameter getParam() {
    return param;
  }

  public void setParam(Parameter param) {
    this.param = param;
  }

  public DateTime getSensorDateStart() {
    return sensorDateStart;
  }

  public void setSensorDateStart(DateTime sensorDateStart) {
    this.sensorDateStart = sensorDateStart;
  }

  public DateTime getSensorDateEnd() {
    return sensorDateEnd;
  }

  public void setSensorDateEnd(DateTime sensorDateEnd) {
    this.sensorDateEnd = sensorDateEnd;
  }

  public LocationPointDTO getLocationPointDTO() {
    return locationPointDTO;
  }

  public void setLocationPointDTO(LocationPointDTO locationPointDTO) {
    this.locationPointDTO = locationPointDTO;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("stationId", stationId)
        .add("param", param)
        .add("sensorDateStart", sensorDateStart)
        .add("sensorDateEnd", sensorDateEnd)
        .add("locationPointDTO", locationPointDTO)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sensor sensor = (Sensor) o;
    return id == sensor.id &&
        stationId == sensor.stationId &&
        Objects.equal(param, sensor.param) &&
        Objects.equal(sensorDateStart, sensor.sensorDateStart) &&
        Objects.equal(sensorDateEnd, sensor.sensorDateEnd) &&
        Objects.equal(locationPointDTO, sensor.locationPointDTO);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, stationId, param, sensorDateStart, sensorDateEnd, locationPointDTO);
  }

  public Set<Parameter> getParams() {
    return params;
  }

  public void setParams(Set<Parameter> params) {
    this.params = params;
  }

  public List<Measurement> getMeasurements() {
    return measurements;
  }

  public void setMeasurements(List<Measurement> measurements) {
    this.measurements = measurements;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
