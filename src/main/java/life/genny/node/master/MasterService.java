package life.genny.node.master;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface MasterService {
  
  // A couple of factory methods to create an instance and a proxy
  public static MasterService create(Vertx vertx) {
    return new MasterServiceImpl();
  }

  public static MasterService createProxy(Vertx vertx,
    String address) {
    return new MasterServiceVertxEBProxy(vertx, address);
  }

  void doWhatMastersDo(String hello,Handler<AsyncResult<JsonObject>> result);
    
}
