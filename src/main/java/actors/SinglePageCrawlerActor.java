package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableList;
import dto.CrawlingRequest;
import model.WebContent;
import service.CrawlerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SinglePageCrawlerActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final CrawlerService crawlerService;
    private int depth;
    private String url;
    private final String filterWord;
    private final UUID requestId;
    private final String masterPath;
    private int throttlingSeconds = 0;

    public SinglePageCrawlerActor(CrawlerService crawlerService, int depth, String url, String filterWord, UUID requestId, String masterPath, int throttlingSeconds) {
        super();
        this.depth = depth;
        this.url = url;
        this.filterWord = filterWord;
        this.requestId = requestId;
        this.masterPath = masterPath;
        this.throttlingSeconds = throttlingSeconds;
        this.crawlerService = crawlerService;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CrawlingRequest.class, cr -> {
                        Optional<WebContent> wc = crawlerService.getWebPageContent(cr.getUrl());
                        if(wc.isPresent() && depth > 0) {
                            List<WebContent> result = extractByXPaths(wc.get(), cr.getxPaths());
                            getSender().tell(result, sender());
                            List<String> urls = result.size() > 0 ? result.get(0).getUrls() : ImmutableList.of();
                            for (String s : urls) {
                                ActorRef slave = getContext().actorOf(Props.create(SinglePageCrawlerActor.class,
                                        crawlerService,
                                        depth - 1,
                                        url,
                                        filterWord,
                                        requestId,
                                        masterPath,
                                        throttlingSeconds
                                        ));
                                slave.tell(cr, sender());
                            }
                        }

                        log.info("Received message: {}", cr);})
                .matchAny(any -> log.info("Received unknown message...{}", any))
                .build();
    }

    private List<WebContent> extractByXPaths(WebContent webContent, List<String> XPaths) {
        List<WebContent> result = new ArrayList<>();

        for(String XPath : XPaths) {
            result.addAll(crawlerService.extractByXPath(webContent, XPath));
        }

        return result;
    }
}
