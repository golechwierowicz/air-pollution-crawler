package modules.crawler.actors;

import akka.actor.ActorSystem;

public class ActorSysContainer {
    private ActorSystem sys;
    private static ActorSysContainer instance = null;
    private static String actorSysName = "crawler";

    private ActorSysContainer() {
        sys = ActorSystem.create(ActorSysContainer.actorSysName);
    }

    public ActorSystem getSystem() {
        return sys;
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
}