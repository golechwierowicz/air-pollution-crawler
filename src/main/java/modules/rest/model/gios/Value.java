package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.DateTime;

public class Value {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime date;
    private double value;

    public Value() {
    }

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
}