package modules.common.dao;

import modules.rest.model.StationData;

import java.util.stream.Stream;

public interface StationDataDao {
  int save(StationData stationData);
  Stream<StationData> getAll();
  StationData getById(int id);
}