package life.genny.node;

import static java.lang.System.out;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;
import life.genny.node.cluster.ClusterConnection;
import life.genny.node.election.LeaderElection;

@QuarkusMain
public class Service {

  public static void main(String... args) {

    LeaderElection election = new LeaderElection();
    ClusterConnection con = new ClusterConnection();

    con.onConnection().onComplete(d -> election.nodeDiscovery());

    out.println("Running main method");
    Quarkus.run(args);

  }


}
