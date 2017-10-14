package resources;

import dto.CrawlingRequest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/crawl")
public class SimpleResource {
    @POST
    @Produces(MediaType.TEXT_HTML)
    public String getIt(CrawlingRequest crawlingRequest) {
        System.out.println(crawlingRequest.toString());
        return "<h1>Got it!</h1>";
    }
}