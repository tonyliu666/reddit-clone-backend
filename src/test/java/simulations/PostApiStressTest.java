package simulations;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * Gatling stress test for the Post API endpoints.
 * Tests both GET and POST operations on /api/v1/posts
 */
public class PostApiStressTest extends Simulation {

    // 1. HTTP Protocol Configuration
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080") // Your Spring App URL
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    // 2. Scenario Definition - GET all posts
    ScenarioBuilder getPostsScenario = scenario("GET All Posts")
            .exec(http("Get All Posts")
                    .get("/api/v1/posts")
                    .check(status().is(200)) // Ensure we get a 200 OK
                    .check(jsonPath("$[*]").count().gte(0)) // Verify response is an array (0 or more elements)
            )
            .pause(1); // Simulate human "think time"

    // 3. Scenario Definition - CREATE a new post
    ScenarioBuilder createPostScenario = scenario("CREATE New Post")
            .exec(http("Create Post")
                    .post("/api/v1/posts")
                    .body(StringBody("""
                            {
                                "title": "Stress Test Post #{randomNumber}",
                                "content": "This is a test post created during stress testing at #{timestamp}"
                            }
                            """))
                    .asJson()
                    .check(status().is(200)) // Ensure we get a 200 OK
                    .check(jsonPath("$.id").exists()) // Verify ID is returned
                    .check(jsonPath("$.title").exists()) // Verify title is returned
                    .check(jsonPath("$.content").exists()) // Verify content is returned
            )
            .pause(1);

    // 4. Mixed Scenario - Combination of GET and POST
    ScenarioBuilder mixedScenario = scenario("Mixed GET and POST Operations")
            .exec(http("Get All Posts")
                    .get("/api/v1/posts")
                    .check(status().is(200)))
            .pause(Duration.ofMillis(500))
            .exec(http("Create Post")
                    .post("/api/v1/posts")
                    .body(StringBody("""
                            {
                                "title": "Mixed Test Post",
                                "content": "Testing mixed operations"
                            }
                            """))
                    .asJson()
                    .check(status().is(200)))
            .pause(Duration.ofMillis(500))
            .exec(http("Get All Posts Again")
                    .get("/api/v1/posts")
                    .check(status().is(200)))
            .pause(1);

    // 5. Load Simulation (The "Stress" part)
    {
        setUp(
                // Run GET requests with high load
                getPostsScenario.injectOpen(
                        nothingFor(Duration.ofSeconds(5)), // Warm up
                        atOnceUsers(10), // Initial burst
                        rampUsers(500).during(Duration.ofMinutes(2)) // Aggressive ramp-up
                ),

                // Run POST requests with moderate load
                createPostScenario.injectOpen(
                        nothingFor(Duration.ofSeconds(10)), // Start after GET scenario
                        atOnceUsers(5), // Initial burst
                        rampUsers(200).during(Duration.ofMinutes(2)) // Moderate ramp-up
                ),

                // Run mixed scenario with light load
                mixedScenario.injectOpen(
                        nothingFor(Duration.ofSeconds(15)), // Start last
                        rampUsers(100).during(Duration.ofMinutes(2)) // Light ramp-up
                )).protocols(httpProtocol)
                .assertions(
                        global().responseTime().percentile3().lt(500), // 95th percentile < 500ms
                        global().successfulRequests().percent().gt(99.0) // Success rate > 99%
                );
    }
}
