package modules.crawler.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableList;
import modules.crawler.model.CrawlingRequest;
import modules.crawler.model.WebContent;
import modules.crawler.service.CrawlerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
                        if (!cr.getFilterWords().isEmpty() && !cr.isFilterByKeywordOnly()) {
                            result = result
                                    .stream()
                                    .filter(w -> w.containsWord(cr.getFilterWords()))
                                    .collect(Collectors.toList());
                        }
                        getContext().actorSelection(masterPath).tell(result, self());
                        List<String> urls = result.size() > 0 ? result.get(0).getUrls() : ImmutableList.of();
                        int newDepth = cr.getDepth() - 1;
                        if (newDepth > 0) {
                            for (String s : urls) {
                                ActorRef slave = getContext().actorOf(Props.create(SinglePageCrawlerActor.class,
                                        crawlerService,
                                        requestId,
                                        masterPath
                                ));
                                CrawlingRequest crNew = CrawlingRequest.copyCrawlingRequest(cr);
                                crNew.setDepth(newDepth);
                                crNew.setUrl(s);
                                slave.tell(crNew, getSelf());
                            }
                        }
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