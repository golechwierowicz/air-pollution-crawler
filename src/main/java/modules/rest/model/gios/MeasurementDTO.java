package modules.rest.model.gios;

public class MeasurementDTO {
    private String key;
    private Value[] values;
    public MeasurementDTO() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Value[] getValues() {
        return values;
    }

    public void setValues(Value[] values) {
        this.values = values;
    }
}