package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;

public class Sensor {
    private int id;
    private int stationId;
    private Parameter param;
    @JsonIgnore
    private DateTime sensorDateStart;
    @JsonIgnore
    private DateTime sensorDateEnd;

    public Sensor() {
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
}
