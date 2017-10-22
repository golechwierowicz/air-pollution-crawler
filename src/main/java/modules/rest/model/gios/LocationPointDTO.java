package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import org.joda.time.DateTime;

public class LocationPointDTO {
    public LocationPointDTO() {}
    private int id;
    private String stationName;
    private double gegrLat;
    private double gegrLon;
    private City city;
    private String addressStreet;
    @JsonIgnore
    private DateTime dateStart;
    @JsonIgnore
    private DateTime dateEnd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public double getGegrLat() {
        return gegrLat;
    }

    public void setGegrLat(double gegrLat) {
        this.gegrLat = gegrLat;
    }

    public double getGegrLon() {
        return gegrLon;
    }

    public void setGegrLon(double gegrLon) {
        this.gegrLon = gegrLon;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public DateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(DateTime dateStart) {
        this.dateStart = dateStart;
    }

    public DateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(DateTime dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("stationName", stationName)
                .add("gegrLat", gegrLat)
                .add("gegrLon", gegrLon)
                .add("city", city)
                .add("addressStreet", addressStreet)
                .add("dateStart", dateStart)
                .add("dateEnd", dateEnd)
                .toString();
    }
}