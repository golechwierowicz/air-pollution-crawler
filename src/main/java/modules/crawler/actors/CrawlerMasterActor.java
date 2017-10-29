package modules.crawler.actors;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import modules.crawler.model.CrawlingRequest;
import modules.crawler.model.GetResult;
import modules.crawler.model.WebContent;
import modules.crawler.service.CrawlerService;
import modules.crawler.service.CrawlerServiceImpl;
import modules.crawler.service.XPathQueryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrawlerMasterActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final List<WebContent> result = new ArrayList<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CrawlingRequest.class, this::crawl)
                .match(List.class, (list -> {
                    for (Object o : list) {
                        if(o instanceof WebContent)
                            result.add((WebContent) o);
                    }
                    log.info("Master state: " + result.size());
                }))
                .match(GetResult.class, (p -> {
                    p.result = result;
                    sender().tell(p, self());
                }))
                .matchAny(any -> log.info("Received unknown message...{}", any))
                .build();
    }

    private void crawl(CrawlingRequest crawlingRequest) {
        UUID requestUUID = crawlingRequest.getRequestUUID();
        String masterPath = akka.serialization.Serialization.serializedActorPath(self());
        CrawlerService crawlerService = new CrawlerServiceImpl(new XPathQueryServiceImpl());

        ActorRef slaveActor = getContext().actorOf(Props.create(SinglePageCrawlerActor.class,
                crawlerService,
                requestUUID,
                masterPath
                ).withDeploy(ActorDeployment.getRandomDeployment()));

        slaveActor.tell(crawlingRequest, getSelf());
    }
}