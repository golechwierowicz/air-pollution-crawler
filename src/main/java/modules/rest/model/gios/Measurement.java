package modules.rest.model.gios;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name="measurement")
public class Measurement {
  @Id
  @Column(name="m_id")
  private int id;
  @Column(name="m_value")
  private double value;
  @Column(name="timestamp")
  private DateTime timestamp;
  @ManyToOne()
  @JoinColumn(name="s_id", nullable = false)
  private Sensor sensor;

  public Measurement() {
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
}
