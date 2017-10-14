package actor;

import actors.CrawlerMaster;
import actors.SinglePageCrawlerActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import com.google.common.collect.ImmutableList;
import dto.CrawlingRequest;
import dto.GetResult;
import model.WebContent;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import service.CrawlerService;
import service.CrawlerServiceImpl;
import service.XPathQueryServiceImpl;

import java.util.List;
import java.util.UUID;

public class SinglePageCrawlerActorIT {
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
    public void testCrawling() throws Exception { // requires internet
        new TestKit(system) {{
            int depth = 1;
            CrawlerService crawlerService = new CrawlerServiceImpl(new XPathQueryServiceImpl());
            UUID requestId = UUID.randomUUID();
            String masterPath = getRef().path().toStringWithoutAddress();
            CrawlingRequest crawlingRequest = new CrawlingRequest(
                    "http://www.tvn24.pl",
                    ImmutableList.of("//*/article/h1/a"),
                    false,
                    0,
                    2,
                    null);
            final Props props = Props.create(CrawlerMaster.class);
            final ActorRef master  = system.actorOf(props);
            master.tell(crawlingRequest, getTestActor());
            Thread.sleep(10000);
            master.tell(new GetResult(), getTestActor());
            List result =  expectMsgClass(List.class);
            System.out.println(result);
        }};
    }
}