package modules.crawler.resources;

import modules.crawler.actors.ActorSysContainer;
import modules.crawler.actors.CrawlerMasterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import com.google.common.collect.ImmutableList;
import modules.crawler.model.CrawlingRequest;
import modules.crawler.model.GetResult;
import modules.crawler.model.RequestUUID;
import modules.crawler.model.WebContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Path("/crawl")
public class CrawlingResource {
    private static Logger log = LoggerFactory.getLogger(CrawlingResource.class);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RequestUUID crawl(CrawlingRequest crawlingRequest) {
        log.info("Received crawling request: " + crawlingRequest.toString());
        UUID reqUUID = UUID.randomUUID();
        log.info("Assigned request with UUID: " + reqUUID);
        final Props props = Props.create(CrawlerMasterActor.class);
        crawlingRequest.setRequestUUID(reqUUID);
        final ActorRef master = ActorSysContainer.getInstance()
                .getSystem()
                .actorOf(props, crawlingRequest.getRequestUUID().toString());
        master.tell(crawlingRequest, null);
        return new RequestUUID(reqUUID.toString());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{requestUUID}")
    public List<WebContent> getStatus(@PathParam("requestUUID") String requestUUID) throws Exception {
        final String actorSystemName = ActorSysContainer.getActorSysName();
        final ActorSelection requestedMaster = ActorSysContainer.getInstance()
                .getSystem()
                .actorSelection(String.format("akka://%s/user/%s", actorSystemName, requestUUID));

        final Timeout timeout = new Timeout(60, TimeUnit.SECONDS);
        final Future<Object> future = Patterns.ask(requestedMaster, new GetResult(), timeout);

        List<WebContent> result = ((GetResult) Await.result(future, timeout.duration())).result;
        result.forEach(wc -> wc.setUrls(ImmutableList.of()));
        return result;
    }
}