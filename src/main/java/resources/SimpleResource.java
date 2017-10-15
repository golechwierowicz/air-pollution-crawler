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

@Path("/crawl")
public class SimpleResource {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String crawl(CrawlingRequest crawlingRequest) {
        System.out.println(crawlingRequest.toString());
        UUID reqUUID = UUID.randomUUID();
        System.out.println("Received request with UUID: " + reqUUID);
        final Props props = Props.create(CrawlerMasterActor.class);
        crawlingRequest.setRequestUUID(reqUUID);
        final ActorRef master = ActorSysContainer.getInstance()
                .getSystem()
                .actorOf(props, crawlingRequest.getRequestUUID().toString());
        master.tell(crawlingRequest, ActorSysContainer.getInstance().getSystem().guardian());
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

        final Timeout timeout = new Timeout(55, TimeUnit.SECONDS);
        final Future<Object> future = Patterns.ask(requestedMaster, new GetResult(), timeout);
        return ((GetResult) Await.result(future, timeout.duration())).result;
    }
}