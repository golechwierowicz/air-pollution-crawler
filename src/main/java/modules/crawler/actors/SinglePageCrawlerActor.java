package modules.crawler.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Deploy;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import modules.crawler.model.CrawlingRequest;
import modules.crawler.model.WebContent;
import modules.crawler.service.CrawlerService;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SinglePageCrawlerActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final CrawlerService crawlerService;
    private final UUID requestId;
    private final String masterPath;

    public SinglePageCrawlerActor(CrawlerService crawlerService,
                                  UUID requestId,
                                  String masterPath) {
        super();
        this.requestId = requestId;
        this.masterPath = masterPath;
        this.crawlerService = crawlerService;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CrawlingRequest.class, cr -> {
                    log.info("Received message: {}", cr);
                    Optional<WebContent> wc = crawlerService.getWebPageContent(cr.getUrl());
                    if (wc.isPresent() && cr.getDepth() > 0) {

                        List<WebContent> result = cr.isFilterByKeywordOnly() ?
                                crawlerService.extractByFilterWordOnly(wc.get(), cr.getFilterWords())
                                : extractByXPaths(wc.get(), cr.getxPaths());
                        result.forEach(w -> w.setCrawledUrl(cr.getUrl()));
                        List<WebContent> toSend = result;
                        if (!cr.getFilterWords().isEmpty() && !cr.isFilterByKeywordOnly()) {
                            toSend = result
                                    .stream()
                                    .filter(w -> w.containsWord(cr.getFilterWords()))
                                    .collect(Collectors.toList());
                        }
                        getContext().actorSelection(masterPath).tell(toSend, self());
                        List<String> urls = wc.get().getUrls();
                        int newDepth = cr.getDepth() - 1;
                        if (newDepth > 0) {
                            Deploy deployment = ActorDeployment.getRandomDeployment();
                            for (String s : urls) {
                                ActorRef slave = getContext().actorOf(Props.create(SinglePageCrawlerActor.class,
                                        crawlerService,
                                        requestId,
                                        masterPath
                                ).withDeploy(deployment));
                                CrawlingRequest crNew = CrawlingRequest.copyCrawlingRequest(cr);
                                crNew.setDepth(newDepth);
                                crNew.setUrl(s);
                                if (cr.isThrottlingEnabled()) {
                                    getContext()
                                            .getSystem()
                                            .scheduler()
                                            .scheduleOnce(new FiniteDuration(
                                                            cr.getThrottlingSeconds(), TimeUnit.SECONDS),
                                                    slave,
                                                    crNew,
                                                    getContext().getSystem().dispatcher(),
                                                    self());
                                } else slave.tell(crNew, getSelf());
                            }
                        }
                        getContext().getSystem().stop(self());
                    }
                })
                .matchAny(any -> log.warning("Received unknown message...{}", any))
                .build();
    }

    private List<WebContent> extractByXPaths(WebContent webContent, List<String> XPaths) {
        List<WebContent> result = new ArrayList<>();

        for (String XPath : XPaths)
            result.addAll(crawlerService.extractByXPath(webContent, XPath));

        result.forEach(wc -> wc.setRequestID(requestId));

        return result;
    }
}