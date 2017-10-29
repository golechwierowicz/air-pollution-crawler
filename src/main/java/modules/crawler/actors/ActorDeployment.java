package modules.crawler.actors;

import akka.actor.Address;
import akka.actor.Deploy;
import akka.remote.RemoteScope;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActorDeployment {
    private static Config config = ConfigFactory.load().getConfig("crawler.deployment");
    private static String protocol = config.getString("protocol");
    private static String system = config.getString("system");
    private static List<Integer> ports = config.getIntList("ports");
    private static List<String> hosts = config.getStringList("hosts");
    private static List<Address> deploymentAddresses = new ArrayList<>();
    private static Random r = new Random();

    static {
        for (int i = 0; i < hosts.size(); i++)
            deploymentAddresses.add(new Address(protocol, system, hosts.get(i), ports.get(i)));
    }

    private ActorDeployment() {
    }

    public static Deploy getRandomDeployment() {
        int idx = r.nextInt(deploymentAddresses.size());
        return new Deploy(new RemoteScope(deploymentAddresses.get(idx)));
    }
}