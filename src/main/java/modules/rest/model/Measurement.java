package modules.rest.model;

import java.util.List;

public class Measurement {
    public String measurementName;
    public List<Double> measurementValue;
    public List<Long> measurementTimes;
    public String[] attributes;
}
