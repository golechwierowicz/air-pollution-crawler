package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Locale;

public class Value {
  private DateTime date;
  private double value;

  public Value() {
  }

  public Value(DateTime date, double value) {
    this.date = date;
    this.value = value;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime getDate() {
    return date;
  }

  public void setDate(DateTime date) {
    this.date = date;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("date", date)
        .add("value", value)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Value value1 = (Value) o;
    return Double.compare(value1.value, value) == 0 &&
        date.equals(value1.date);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(date, value);
  }
}