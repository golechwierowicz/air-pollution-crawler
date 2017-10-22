package modules.rest.model;

import java.util.Optional;

public abstract class StationLocator {
    Integer stationId = null;
    String stationCity = null;

    public Optional<Integer> getStationId() {
        return Optional.ofNullable(stationId);
    }

    public Optional<String> getStationCity() {
        return Optional.ofNullable(stationCity);
    }

    protected abstract void setType();
}
