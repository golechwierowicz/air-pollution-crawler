package boot;

import akka.actor.ActorRef;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import modules.crawler.actors.ActorSysContainer;
import modules.crawler.actors.DataCrawler;
import modules.crawler.model.DataCrawl;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import scala.concurrent.duration.FiniteDuration;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
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
    setUpCyclicDataFetch(3600);
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

  private static void setUpCyclicDataFetch(int seconds) {
    final ActorRef dataCrawler = ActorSysContainer.getInstance().getDataCrawler();
    ActorSysContainer
        .getInstance()
        .getSystem()
        .scheduler()
        .schedule(new FiniteDuration(0, TimeUnit.SECONDS),
            new FiniteDuration(seconds, TimeUnit.SECONDS),
            dataCrawler,
            new DataCrawl(),
            ActorSysContainer.getInstance().getSystem().dispatcher(),
            null);
  }
}