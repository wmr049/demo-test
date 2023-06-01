package cucumber;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.util.TimeZone;

import static io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/"},
        plugin = {"pretty", "html:target/cucumber.html"},
        snippets = CAMELCASE,
        tags = "not @ignore")
public class CucumberComponentDemoTest {

    private static final Logger log = LoggerFactory.getLogger(CucumberComponentQrcodeTest.class);

    public static final Integer WIREMOCK_PORT = 9090;

    public static final Integer API_PORT = 8080;

    private CucumberComponentDemoTest(){}

    @ClassRule
    public static final DockerComposeContainer<?> environment =
            new DockerComposeContainer(new File("src/test/resources/docker-compose-qrcode.yml"))
                    .waitingFor("wiremock", Wait.forHealthcheck())
//                    .waitingFor("api", Wait.forHealthcheck())
                    .withExposedService("wiremock_1", WIREMOCK_PORT)
//                    .withExposedService("api_1", API_PORT)
                    .withLocalCompose(true);

    @BeforeClass
    public static void buildUp() {
        log.info("=== Starting Automated Tests ===");
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        final String wiremockHost = environment.getServiceHost("wiremock", WIREMOCK_PORT);
        WireMock.configureFor(wiremockHost, WIREMOCK_PORT);
    }

    @AfterClass
    public static void tearDown() {
        log.info("=== Automated Tests Finished ===");
    }

}
