package modules.rest.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import modules.common.dao.*;
import modules.common.streams.ExportMeasurementsStreamWriter;
import modules.common.utils.CallServiceImpl;
import modules.rest.model.IdStationLocator;
import modules.rest.model.StationData;
import modules.rest.model.StationLocator;
import modules.rest.service.CallerService;
import modules.rest.service.GIOSCallerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Path("rest")
public class LocationPointResource {
  private static Logger log = LoggerFactory.getLogger(LocationPointResource.class);

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/loc_points")
  public Response getLocationsByCountry() throws JsonProcessingException {
    CallerService giosCallerService = new GIOSCallerServiceImpl(new CallServiceImpl());
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JodaModule());

    try {
      return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(giosCallerService.getPointsByCountry(""))).build();
    } catch (IOException e) {
      log.error("Error io. Fallbacking to db", e);
      LocationPointDao locationPointDao = new LocationPointDaoImpl(new HibernateSessionFactoryImpl());
      return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(locationPointDao.getAll())).build();
    } catch (InterruptedException | ExecutionException e) {
      log.error("e", e);
      return Response.status(Response.Status.BAD_GATEWAY).entity("Too much load").build();
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/station/name/{name}/id/{id}/measurements")
  public Response getMeasurementsForSenor(@PathParam("id") int id, @PathParam("name") String name) throws JsonProcessingException {
    CallerService callerService = new GIOSCallerServiceImpl(new CallServiceImpl());
    StationLocator stationLocator = new IdStationLocator(id);
    stationLocator.stationName = name;
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JodaModule());
    StationData stationData;
    try {
      stationData = callerService.getStationData(stationLocator);
    } catch (Exception e) {
      StationDataDao stationDataDao = new StationDataDaoImpl(new HibernateSessionFactoryImpl());
      stationData = stationDataDao.getById(stationLocator.stationId).orElse(null);
    }
    return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(stationData)).build();
  }

  @GET
  @Produces("text/comma-separated-values")
  @Path("/measurements.csv")
  public Response getData() {
    return Response
        .status(Response.Status.OK)
        .entity(new ExportMeasurementsStreamWriter(new HibernateSessionFactoryImpl()))
        .build();
  }
}