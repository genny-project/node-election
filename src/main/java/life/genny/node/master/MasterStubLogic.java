package life.genny.node.master;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import life.genny.node.ClusteredVertx;

public class MasterStubLogic {
  
  static final Logger logger = LoggerFactory.getLogger( MasterStubLogic.class );
  
  public static void executeHeavyStuff() {
    Vertx vertx = ClusteredVertx.INSTANCE.getInstance();
    vertx.executeBlocking(MasterStubLogic::heavyStuff, r -> {});
  }

  static  void heavyStuff(Promise<?> d) {
    try {
      logger.info("Executing heavy stuff");
      Thread.sleep(4000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
