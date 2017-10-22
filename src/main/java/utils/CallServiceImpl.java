package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CallServiceImpl implements CallService {
    @Override
    public String getContent(String target) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(target);
        Invocation.Builder request = resource.request();
        request.accept(MediaType.APPLICATION_JSON);
        Response response = request.get();
        return response.readEntity(String.class);
    }
}
