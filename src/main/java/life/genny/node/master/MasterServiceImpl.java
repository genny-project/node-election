package life.genny.node.master;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public class MasterServiceImpl implements MasterService {


  @Override
  public void doWhatMastersDo(String hello, Handler<AsyncResult<JsonObject>> result) {
    JsonObject arr = new JsonObject();
    arr.put("proxynmae", "my Nmaessf");
    result.handle(Future.succeededFuture(arr));
    System.out.println("Done findActiveNodeInstances!");
  }

}
