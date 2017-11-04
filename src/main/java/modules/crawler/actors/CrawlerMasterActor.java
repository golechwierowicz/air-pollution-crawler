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

import java.util.*;

public class CrawlerMasterActor extends AbstractActor {
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private final List<WebContent> result = new ArrayList<>();
  private final Set<String> crawledUrls = new HashSet<>();
  private final UUID id;

  public CrawlerMasterActor(UUID id) {
    this.id = id;
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(CrawlingRequest.class, (cr -> {
          if (!crawled(cr.getUrl()))
            crawl(cr);
        }))
        .match(List.class, (list) -> {
          list.stream()
              .filter(o -> o instanceof WebContent)
              .forEach(wc -> result.add((WebContent) wc));
          log.info(String.format("Master identified with id: %s has content of size: %d",
              id.toString(),
              result.size()));
        })
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
    crawledUrls.add(crawlingRequest.getUrl());
    slaveActor.tell(crawlingRequest, getSelf());
  }

  private boolean crawled(String url) {
    return crawledUrls.contains(url);
  }
}