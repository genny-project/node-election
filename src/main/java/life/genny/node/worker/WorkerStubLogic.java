package life.genny.node.worker;

import life.genny.node.master.MasterService;

public class WorkerStubLogic {

  public static MasterService masterService;
  
  public static void workerElection() {
    masterService.doWhatMastersDo("hello", d -> {
      System.out.println(d.result());
    });
  }

}
