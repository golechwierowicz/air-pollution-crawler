package modules.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import modules.rest.model.LocationPoint;
import modules.rest.model.StationData;
import modules.rest.model.StationLocator;
import modules.rest.model.gios.LocationPointDTO;
import utils.CallService;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GIOSCallerServiceImpl extends CallerService {
    private final static Config conf = ConfigFactory.load().getConfig("rest.gios");
    private final static String HOST = String.format("http://%s", conf.getString("host"));
    private final CallService callService;

    public GIOSCallerServiceImpl(CallService callService) {
        this.callService = callService;
    }

    @Override
    public List<LocationPoint> getPointsByCountry(final String country) throws IOException {
        String target = String.format("%s/%s", HOST, "/station/findAll");
        String content = callService.getContent(target);
        return pointsDTOToLocationPoints(content);
    }

    @Override
    public StationData getStationData(StationLocator stationLocator) {
        return null;
    }

    private List<LocationPoint> pointsDTOToLocationPoints(final String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        LocationPointDTO[] points = mapper.readValue(content, LocationPointDTO[].class);
        return Arrays.stream(points).map(p -> {
            LocationPoint locationPoint = new LocationPoint();
            locationPoint.setName(p.getStationName());
            locationPoint.setValue(null);
            locationPoint.setLatitude(p.getGegrLat());
            locationPoint.setLongtitude(p.getGegrLon());
            locationPoint.setId(p.getId());
            return locationPoint;
        }).collect(Collectors.toList());
    }

}