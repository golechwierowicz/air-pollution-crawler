package modules.rest.model.gios;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "measurement", uniqueConstraints = @UniqueConstraint(columnNames = {"m_timestamp", "sensor_id"}))
public class Measurement {
  @Id
  @Column(name = "m_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  @Column(name = "m_value")
  private double value;
  @Column(name = "m_timestamp")
  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime timestamp;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sensor_id", nullable = false)
  private Sensor sensor;

  public Measurement() {
  }

  public Measurement(double value, DateTime timestamp, Sensor sensor) {
    this.value = value;
    this.timestamp = timestamp;
    this.sensor = sensor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Measurement that = (Measurement) o;
    return id == that.id &&
        Double.compare(that.value, value) == 0 &&
        Objects.equal(timestamp, that.timestamp) &&
        Objects.equal(sensor, that.sensor);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, value, timestamp, sensor);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("value", value)
        .add("timestamp", timestamp)
        .add("sensor", sensor)
        .toString();
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public void setTimestamp(DateTime timestamp) {
    this.timestamp = timestamp;
  }

  public void setSensor(Sensor sensor) {
    this.sensor = sensor;
  }

  public int getId() {
    return id;
  }

  public double getValue() {
    return value;
  }

  public DateTime getTimestamp() {
    return timestamp;
  }

  public Sensor getSensor() {
    return sensor;
  }
}
