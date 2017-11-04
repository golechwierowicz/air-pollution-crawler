package boot;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Boot {
  private static Config conf = ConfigFactory.load().getConfig("grizzly");
  private static final String HOST = conf.getString("host");
  private static final int PORT = conf.getInt("port");
  private static final String API_SUFFIX = conf.getString("api_suffix");
  private static final String BASE_URI = String.format("http://%s:%d/%s/", HOST, PORT, API_SUFFIX);

  private static HttpServer startServer() {
    final String[] packages = new String[]{"modules.crawler.resources", "modules.rest.resources"};
    final ResourceConfig rc = new ResourceConfig().packages(packages);
    return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
  }

  public static void main(String[] args) throws IOException {
    final HttpServer server = startServer();
    setUpGrizzly(server);
    System.out.println(String.format("Jersey app started with WADL available at "
        + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
    System.in.read();
    server.shutdown();
  }

  private static void setUpGrizzly(final HttpServer server) {
    Logger l = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
    l.setLevel(Level.FINE);
    l.setUseParentHandlers(false);
    ConsoleHandler ch = new ConsoleHandler();
    ch.setLevel(Level.ALL);
    l.addHandler(ch);

    HttpHandler staticHttpHandler = new CLStaticHttpHandler(HttpServer.class.getClassLoader(), "/static/");
    server.getServerConfiguration().addHttpHandler(staticHttpHandler, "/");
  }
}