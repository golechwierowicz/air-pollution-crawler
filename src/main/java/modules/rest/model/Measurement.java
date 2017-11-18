package modules.rest.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import modules.rest.model.gios.Value;

public class Measurement {
  public int id; // sensor id
  public String measurementName;
  public Value[] values;
  public String[] attributes;

  public Measurement(int id, String measurementName, Value[] values) {
    this.id = id;
    this.measurementName = measurementName;
    this.values = values;
  }

  public Measurement() {
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("measurementName", measurementName)
        .add("values", values)
        .add("attributes", attributes)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Measurement that = (Measurement) o;
    return id == that.id &&
        Objects.equal(measurementName, that.measurementName) &&
        compareArrays(values, that.values);
  }

  private boolean compareArrays(Value[] values, Value[] values1) {
    if(values.length != values1.length)
      return false;

    for(int i = 0; i < values.length; i++) {
      if(!values[i].equals(values1[i]))
        return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, measurementName, values, attributes);
  }
}
