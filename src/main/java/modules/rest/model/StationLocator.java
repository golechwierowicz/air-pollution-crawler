package modules.rest.model;

import java.util.Optional;

public abstract class StationLocator {
    protected Integer stationId = null;
    protected String stationCity = null;
    public String stationName;

    public Optional<Integer> getStationId() {
        return Optional.ofNullable(stationId);
    }

    public Optional<String> getStationCity() {
        return Optional.ofNullable(stationCity);
    }

    protected abstract void setType();
}
