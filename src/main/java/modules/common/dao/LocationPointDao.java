package modules.common.dao;

import modules.rest.model.gios.LocationPointDTO;

import java.util.List;

public interface LocationPointDao {
  int save(LocationPointDTO locationPointDTO);

  LocationPointDTO getById(int id);

  List<LocationPointDTO> getAll();
}