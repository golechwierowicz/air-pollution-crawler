package modules.rest.model;

public class CityStationLocator extends StationLocator {
    private String city;

    public CityStationLocator(String cityName) {
        city = cityName;
        setType();
    }

    @Override
    protected void setType() {
        this.stationCity = this.city;
    }
}
