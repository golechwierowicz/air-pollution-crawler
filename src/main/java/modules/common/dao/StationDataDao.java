package modules.common.dao;

import modules.rest.model.StationData;

import java.util.List;

public interface StationDataDao {
  int save(StationData stationData);
  List<StationData> getAll();
  StationData getById(int id);
}