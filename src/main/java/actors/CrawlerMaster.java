package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import dto.CrawlingRequest;
import dto.GetResult;
import model.WebContent;
import service.CrawlerService;
import service.CrawlerServiceImpl;
import service.XPathQueryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrawlerMaster extends AbstractActor {
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
                }))
                .match(GetResult.class, (p -> {
                    sender().tell(result, self());
                }))
                .matchAny(any -> log.info("Received unknown message...{}", any))
                .build();
    }

    private UUID generateCrawlingRequestID() {
        return UUID.randomUUID();
    }

    private void crawl(CrawlingRequest crawlingRequest) {
        UUID requestUUID = generateCrawlingRequestID();
        String masterPath = akka.serialization.Serialization.serializedActorPath(self());
        CrawlerService crawlerService = new CrawlerServiceImpl(new XPathQueryServiceImpl());

        ActorRef slaveActor = getContext().actorOf(Props.create(SinglePageCrawlerActor.class,
                crawlerService,
                requestUUID,
                masterPath
                ));

        slaveActor.tell(crawlingRequest, getSelf());
    }
}
