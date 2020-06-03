package life.genny.node;

import java.util.Optional;
import java.util.function.Supplier;
import io.vertx.core.Vertx;

public enum ClusteredVertx {

  INSTANCE;

  private Vertx vertx;

  public Vertx getInstance() {
    return vertx;
  }

  public void init(Vertx vertx) {
    Supplier<Vertx> v = () -> vertx;
    Optional<Vertx> notNull = Optional.ofNullable(this.vertx);
    this.vertx = notNull.orElseGet(v);
  }

}
