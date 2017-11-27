package modules.crawler.actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import modules.common.dao.StationDataDao;
import modules.crawler.model.DataCrawl;
import modules.rest.model.IdStationLocator;
import modules.rest.model.LocationPoint;
import modules.rest.model.StationData;
import modules.rest.model.StationLocator;
import modules.rest.service.CallerService;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;
import java.util.stream.Collectors;

public class DataCrawler extends AbstractActor {
  private CallerService callerService;
  private StationDataDao stationDataDao;
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public DataCrawler(CallerService callerService, StationDataDao stationDataDao) {
    this.callerService = callerService;
    this.stationDataDao = stationDataDao;
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(DataCrawl.class, d -> {
          log.info("Crawling for values...");
          final List<LocationPoint> points = callerService.getPointsByCountry(null);
          log.info("Got " + points.size() + " points.");
          final List<ImmutablePair<StationLocator, LocationPoint>> locators = points.stream().map(p -> {
            final StationLocator stationLocator = new IdStationLocator(p.getId());
            stationLocator.stationName = p.getName();
            stationLocator.setStationCity(p.getCityName());
            return new ImmutablePair<StationLocator, LocationPoint>(stationLocator, p);

          }).collect(Collectors.toList());
          log.info("Locators size: " + locators.size());
          final List<ImmutablePair<StationData, LocationPoint>> stationDatas = locators.stream().map(p ->
              new ImmutablePair<StationData, LocationPoint>(callerService.getStationData(p.left), p.right)
          ).collect(Collectors.toList());
          log.info("Got station datas: " + stationDatas.size());
          stationDatas.forEach(sd -> stationDataDao.save(sd.left, sd.right));
        }).build();
  }
}
