package modules.rest.resources;

import modules.rest.model.IdStationLocator;
import modules.rest.model.StationLocator;
import modules.rest.service.CallerService;
import modules.rest.service.GIOSCallerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.CallServiceImpl;

import javax.ws.rs.*;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/station/name/{name}/id/{id}/measurements")
    public Response getMeasurementsForSenor(@PathParam("id") int id, @PathParam("name") String name) {
        CallerService callerService = new GIOSCallerServiceImpl(new CallServiceImpl());
        StationLocator stationLocator = new IdStationLocator(id);
        stationLocator.stationName = name;
        return Response.status(Response.Status.OK).entity(callerService.getStationData(stationLocator)).build();
    }
}