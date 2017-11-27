package modules.common.dao;

import modules.rest.model.LocationPoint;
import modules.rest.model.StationData;

import java.util.List;
import java.util.Optional;

public interface StationDataDao {
  int save(StationData stationData, LocationPoint locationPoint);

  List<StationData> getAll();

  Optional<StationData> getById(int id);
}