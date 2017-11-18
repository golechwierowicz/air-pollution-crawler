package modules.crawler.actors;

import akka.actor.AbstractActor;
import modules.common.dao.StationDataDao;
import modules.crawler.model.DataCrawl;
import modules.rest.model.IdStationLocator;
import modules.rest.model.LocationPoint;
import modules.rest.model.StationData;
import modules.rest.model.StationLocator;
import modules.rest.service.CallerService;

import java.util.List;
import java.util.stream.Collectors;

public class DataCrawler extends AbstractActor {
  private CallerService callerService;
  private StationDataDao stationDataDao;

  public DataCrawler(CallerService callerService, StationDataDao stationDataDao) {
    this.callerService = callerService;
    this.stationDataDao = stationDataDao;
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(DataCrawl.class, d -> {
          final List<LocationPoint> points = callerService.getPointsByCountry(null);
          final List<StationLocator> locators = points.stream().map(p -> {
            final StationLocator stationLocator = new IdStationLocator(p.getId());
            stationLocator.stationName = p.getName();
            stationLocator.setStationCity(stationLocator.stationName);
            return stationLocator;
          }).collect(Collectors.toList());
          final List<StationData> stationDatas = locators.stream().map(id ->
              callerService.getStationData(id)
          ).collect(Collectors.toList());
          stationDatas.forEach(sd -> stationDataDao.save(sd));
        }).build();
  }
}
