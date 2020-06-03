package life.genny.node.election;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import io.vertx.servicediscovery.Status;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ServiceBinder;
import life.genny.node.ClusteredVertx;
import life.genny.node.master.MasterService;
import life.genny.node.master.MasterServiceImpl;
import life.genny.node.master.MasterStubLogic;
import life.genny.node.worker.WorkerStubLogic;

public class LeaderElection {

  static final Logger logger = LoggerFactory.getLogger( LeaderElection.class );

  enum ServiceType {
    MASTER,
    WORKER
  }
  
  static ServiceType serviceType;

  static Record record;
  
  static ServiceDiscovery discovery;



  public void nodeDiscovery() {
    JsonObject filterObject = new JsonObject();
    filterObject.put("name", "master-service");
    discovery = ServiceDiscovery.create(ClusteredVertx.INSTANCE.getInstance());

    Future<Void> onChecked = Future.<Void>future(promise -> discovery.getRecord(
        filterObject ,
        d -> discoverElectedMaster(d,promise)));

    onChecked.onComplete(Void -> {
      if(serviceType == ServiceType.MASTER) masterElection();
      else WorkerStubLogic.workerElection();
    });
  }

  void discoverElectedMaster(AsyncResult<Record> response,Promise<Void> promise) {
    if (response.succeeded()) {
      if (response.result() != null) {
        Record record = response.result();
        logger.info("Service Object found. Becoming Worker...");
        serviceType = ServiceType.WORKER;
        ServiceReference reference = discovery.getReference(record);
        WorkerStubLogic.masterService = reference.getAs(MasterService.class);
      } else {
        logger.info("No matching Service Object. Becoming Master...");
        serviceType = ServiceType.MASTER;
      }
      promise.complete();
    }
  }
  
  
  void masterElection() {
    Vertx vertx = ClusteredVertx.INSTANCE.getInstance();
    Record record = EventBusService.createRecord(
        "master-service", 
        "master-service-address",
        MasterService.class );
    record.setStatus(Status.OUT_OF_SERVICE);
    discovery.publish(record, LeaderElection::broadcastRecordHandler);
    MasterStubLogic.executeHeavyStuff();
    logger.info("Finished heavy stuff");
    record.setStatus(Status.UP);
    discovery.update(record, LeaderElection::broadcastRecordHandler);
    MasterService service = new MasterServiceImpl();
    new ServiceBinder(vertx)
      .setAddress("master-service-address")
      .register(MasterService.class, service);
  }
  

  static void  broadcastRecordHandler(AsyncResult<Record> response) {
    if (response.succeeded()) {
      Record publishedRecord = response.result();
      logger.info("Successfully publish record "+ publishedRecord.getName());
    } else {
      logger.info("Failed in publishing record ");
    }
  }

}
