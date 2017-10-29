package actor;

import modules.crawler.actors.CrawlerMasterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.google.common.collect.ImmutableList;
import modules.crawler.model.CrawlingRequest;
import modules.crawler.model.GetResult;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class CrawlerMasterActorIT {
    private static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testCrawlingDepthOne() throws Exception { // requires internet
        new TestKit(system) {{
            CrawlingRequest crawlingRequest = new CrawlingRequest(
                    ImmutableList.of("smog"),
                    "http://www.tvn24.pl",
                    ImmutableList.of("//*/article/h1/a"),
                    false,
                    0,
                    1,
                    null,
                    false);
            final Props props = Props.create(CrawlerMasterActor.class);
            final ActorRef master = system.actorOf(props);
            master.tell(crawlingRequest, getTestActor());
            Thread.sleep(2000);
            master.tell(new GetResult(), getTestActor());
            expectMsgClass(new FiniteDuration(100, TimeUnit.SECONDS), GetResult.class);
        }};
    }
}