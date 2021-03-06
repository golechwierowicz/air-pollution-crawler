package modules.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

public class CallServiceImpl implements CallService {
  private final static Logger log = LoggerFactory.getLogger(CallServiceImpl.class.getName());

  @Override
  public String getContent(String target) {
    Invocation.Builder request = createBuilder(target);
    Response response = request.get();
    return response.readEntity(String.class);
  }

  @Override
  public CompletableFuture<String> getContentAsync(String target) {
    return CompletableFuture.supplyAsync(() -> {
      Invocation.Builder request = createBuilder(target);
      Response response = request.get();
      return response.readEntity(String.class);
    }).exceptionally(e -> {
      log.error("Error fetching website", e);
      return "";
    });
  }

  private Invocation.Builder createBuilder(final String target) {
    Client client = ClientBuilder.newClient();
    WebTarget resource = client.target(target);
    Invocation.Builder request = resource.request();
    request.accept(MediaType.APPLICATION_JSON);
    return request;
  }
}