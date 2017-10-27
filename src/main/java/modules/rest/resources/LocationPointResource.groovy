package modules.rest.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import modules.rest.model.IdStationLocator
import modules.rest.service.GIOSCallerServiceImpl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import utils.CallServiceImpl

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("rest")
class LocationPointResource {
    static Logger log = LoggerFactory.getLogger(LocationPointResource.class)

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
        def data = callerService.getStationData(stationLocator)
        def mapper = new ObjectMapper()
        mapper.registerModule(new JodaModule())
        return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(data)).build()
    }
}