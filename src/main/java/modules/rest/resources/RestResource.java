package modules.rest.resources;

import modules.rest.model.LocationPoint;
import modules.rest.service.CallerService;
import modules.rest.service.GIOSCallerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("rest")
public class RestResource {
    private static Logger log = LoggerFactory.getLogger(RestResource.class);

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{country}")
    public String getLocationsByCountry(@PathParam("country") String country) {
        CallerService callerService = new GIOSCallerServiceImpl();
        try {
            callerService.getPointsByCountry(country);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "dupa";
    }
}