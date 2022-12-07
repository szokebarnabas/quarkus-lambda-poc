import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class TestSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://changeme/Prod/products")
            .acceptHeader("application/json")
            .doNotTrackHeader("1")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate");

    ScenarioBuilder scn = scenario("BasicSimulation")
            .exec(http("request_1")
                    .get("/"))
            .pause(5);

    public static final int MAX_TPS = 100;

    {
        setUp(
                scn.injectOpen(
                        rampUsersPerSec(0).to(MAX_TPS).during(10),
                        constantUsersPerSec(MAX_TPS).during(60),
                        rampUsersPerSec(MAX_TPS).to(0).during(10)
                )
        ).protocols(httpProtocol);
    }
}