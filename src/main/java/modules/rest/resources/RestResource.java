package modules.rest.resources;

import modules.rest.model.LocationPoint;
import modules.rest.service.CallerService;
import modules.rest.service.GIOSCallerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CallServiceImpl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("rest")
public class RestResource {
    private static Logger log = LoggerFactory.getLogger(RestResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/loc_points")
    public Response getLocationsByCountry(@PathParam("country") String country) {
        CallerService callerService = new GIOSCallerServiceImpl(new CallServiceImpl());
        try {
            return Response.status(Response.Status.OK).entity(callerService.getPointsByCountry(country)).build();
        } catch (IOException e) {
            log.error("Error io", e);
            return Response.status(Response.Status.BAD_GATEWAY).entity("Too much load").build();
        }
    }
}