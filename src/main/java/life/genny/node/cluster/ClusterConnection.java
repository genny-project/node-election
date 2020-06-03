package life.genny.node.cluster;

import com.hazelcast.config.Config;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import life.genny.node.ClusteredVertx;

public class ClusterConnection {

  static final Logger logger = LoggerFactory.getLogger( ClusterConnection.class );

  void joinClusterHandler(AsyncResult<Vertx> response, Promise<Void> promise) {
      if (response.succeeded()) {
        Vertx vertx = response.result();
        ClusteredVertx.INSTANCE.init(vertx);
        promise.complete();
      } else {
        logger.info("There has been a problem with joining or starting a cluster");
      }
  }

  public Future<Void> onConnection(){
    Future<Void> future = Future.future(promise -> joinCluster(promise));
    return future;
  }

  void joinCluster(Promise<Void> promise) {
    Config hazelcastConfig = new Config();
    ClusterManager mgr = new HazelcastClusterManager(hazelcastConfig);
    VertxOptions options = new VertxOptions().setClusterManager(mgr);
    Vertx.factory.clusteredVertx(options, d -> joinClusterHandler(d,promise));
  }

}
