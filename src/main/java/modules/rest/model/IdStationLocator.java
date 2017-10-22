package modules.rest.model;

public class IdStationLocator extends StationLocator {
    private int id;

    public IdStationLocator(int id) {
        this.id = id;
    }

    @Override
    protected void setType() {
        this.stationId = this.id;
    }
}
