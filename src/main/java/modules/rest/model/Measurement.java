package modules.rest.model;

import com.google.common.base.MoreObjects;
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
}
