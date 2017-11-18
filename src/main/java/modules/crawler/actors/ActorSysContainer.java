package modules.crawler.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import modules.common.dao.HibernateSessionFactoryImpl;
import modules.common.dao.StationDataDao;
import modules.common.dao.StationDataDaoImpl;
import modules.common.utils.CallServiceImpl;
import modules.rest.service.CallerService;
import modules.rest.service.GIOSCallerServiceImpl;

public class ActorSysContainer {
  private static ActorSysContainer instance = null;
  private static String actorSysName = "crawler";
  private ActorRef dataCrawler;
  private ActorSystem sys;

  private ActorSysContainer() {
    sys = ActorSystem.create(ActorSysContainer.actorSysName);
  }

  public static String getActorSysName() {
    return actorSysName;
  }

  public static synchronized ActorSysContainer getInstance() {
    if (instance == null) {
      instance = new ActorSysContainer();
    }
    return instance;
  }

  public ActorSystem getSystem() {
    return sys;
  }

  public ActorRef getDataCrawler() {
    if(dataCrawler == null) {
      final CallerService callerService = new GIOSCallerServiceImpl(new CallServiceImpl());
      final StationDataDao stationDataDao = new StationDataDaoImpl(new HibernateSessionFactoryImpl());
      final Props props = Props
          .create(DataCrawler.class, callerService, stationDataDao)
          .withDeploy(ActorDeployment.getRandomDeployment());
      assert sys != null;
      dataCrawler = sys.actorOf(props);
    }
    return dataCrawler;
  }
}