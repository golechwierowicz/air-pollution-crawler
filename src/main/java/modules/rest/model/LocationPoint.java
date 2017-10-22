package modules.rest.model;

public class LocationPoint {
    private long latitude;
    private long longtitude;
    private String name;
    private Long value;

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(long longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
