package life.genny.node;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class EndpointsTest {

    //@Test
    public void testHelloEndpoint() {
        given()
          .when().get("/quarkus")
          .then()
             .statusCode(200)
             .body(is("hello"));
    }

}
