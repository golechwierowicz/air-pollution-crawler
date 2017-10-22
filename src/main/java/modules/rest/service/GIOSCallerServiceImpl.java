package modules.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import modules.rest.model.LocationPoint;
import modules.rest.model.StationData;
import modules.rest.model.StationLocator;
import modules.rest.model.gios.LocationPointDTO;
import org.glassfish.jersey.client.ClientResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

public class GIOSCallerServiceImpl extends CallerService {
    private final static Config conf = ConfigFactory.load();

    private final static String HOST = String.format("http://%s", conf.getString("rest.gios.host"));

    @Override
    public List<LocationPoint> getPointsByCountry(final String country) throws IOException {
        Client client = ClientBuilder.newClient();
        String suffix = "/station/findAll";
        String target = String.format("%s/%s", HOST, suffix);
        WebTarget resource = client.target(target);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        Response response = request.get();
        String result = response.readEntity(String.class);
        LocationPointDTO[] res = mapper.readValue(result, LocationPointDTO[].class);
        for (LocationPointDTO re : res) {
            System.out.println(re);
        }
        return null;
    }

    @Override
    public StationData getStationData(StationLocator stationLocator) {
        return null;
    }






}