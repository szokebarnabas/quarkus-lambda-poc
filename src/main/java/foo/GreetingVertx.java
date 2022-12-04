package foo;

import static io.quarkus.vertx.web.Route.HttpMethod.*;

import io.quarkus.vertx.web.Route;
import io.vertx.ext.web.RoutingContext;

import java.util.UUID;

public class GreetingVertx {
    @Route(path = "/vertx/hello", methods = GET, produces = "application/json")
    void hello(RoutingContext context) {
        context.response().headers().set("Content-Type", "application/json");
        String uuid = UUID.randomUUID().toString();
        context.response().setStatusCode(200).end("{\"name\":\"" + uuid + "\"}");
    }

}
