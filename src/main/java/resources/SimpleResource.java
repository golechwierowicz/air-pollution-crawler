package resources;

import actors.ActorSysContainer;
import actors.CrawlerMasterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import dto.CrawlingRequest;
import dto.GetResult;
import model.WebContent;
import scala.concurrent.Await;
import scala.concurrent.Future;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Path("/crawl")
public class SimpleResource {
    private static Logger log = Logger.getLogger(SimpleResource.class.getName());

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String crawl(CrawlingRequest crawlingRequest) {
        log.info("Received crawling request: " + crawlingRequest.toString());
        UUID reqUUID = UUID.randomUUID();
        log.info("Assigned request with UUID: " + reqUUID);
        final Props props = Props.create(CrawlerMasterActor.class);
        crawlingRequest.setRequestUUID(reqUUID);
        final ActorRef master = ActorSysContainer.getInstance()
                .getSystem()
                .actorOf(props, crawlingRequest.getRequestUUID().toString());
        master.tell(crawlingRequest, null);
        return reqUUID.toString();
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
        return ((GetResult) Await.result(future, timeout.duration())).result;
    }
}