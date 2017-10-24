package modules.rest.resources

import modules.rest.model.IdStationLocator
import modules.rest.service.GIOSCallerServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.CallServiceImpl

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("rest")
class RestResource {
    static Logger log = LoggerFactory.getLogger(RestResource.class)

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/loc_points")
    static def getLocationsByCountry(@PathParam("country") String country) {
        def callerService = new GIOSCallerServiceImpl(new CallServiceImpl())
        try {
            return Response.status(Response.Status.OK).entity(callerService.getPointsByCountry(country)).build()
        } catch (IOException e) {
            log.error("Error io", e)
            return Response.status(Response.Status.BAD_GATEWAY).entity("Too much load").build()
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/station/name/{name}/id/{id}/measurements")
    static def getMeasurementsForSenor(@PathParam("id") int id, @PathParam("name") String name) {
        def callerService = new GIOSCallerServiceImpl(new CallServiceImpl())
        def stationLocator = new IdStationLocator(id)
        stationLocator.stationName = name
        return Response.status(Response.Status.OK).entity(callerService.getStationData(stationLocator)).build()
    }
}