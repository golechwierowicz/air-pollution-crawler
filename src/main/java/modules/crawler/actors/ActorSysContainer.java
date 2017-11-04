package modules.crawler.actors;

import akka.actor.ActorSystem;

public class ActorSysContainer {
  private static ActorSysContainer instance = null;
  private static String actorSysName = "crawler";
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
}