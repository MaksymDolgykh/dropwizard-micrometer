package io.github.maksymdolgykh.dropwizard.micrometer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumSet;

import static org.junit.Assert.assertTrue;

@ExtendWith(DropwizardExtensionsSupport.class)
public class MicrometerJdbiTimingCollectorTest {

    private static int getFreePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
        } catch (IOException e) {
        }
        return socket.getLocalPort();
    }

    public static final DropwizardAppExtension<TestConfiguration> EXT = new DropwizardAppExtension<TestConfiguration>(TestApp.class,
            ResourceHelpers.resourceFilePath("testConfig.yaml"),
            ConfigOverride.config("server.applicationConnectors[0].port", String.valueOf(getFreePort())),
            ConfigOverride.config("server.adminConnectors[0].port", String.valueOf(getFreePort())));

    int adminPort = EXT.getAdminPort();
    int appPort = EXT.getLocalPort();
    Client client = EXT.client();

    PrometheusConfiguration prometheusConfiguration = EXT.getConfiguration().getPrometheusConfiguration();


    String ping = client.target("http://localhost:" + appPort + "/ping")
            .request().get(String.class);

    String scrape = client.target("http://localhost:"+ adminPort + prometheusConfiguration.getEndpoint())
            .request().get(String.class);
    

    @Test
    public void serverAcceptsAppConnection() throws IOException {
        Socket ableToConnect = new Socket("localhost", appPort);
        assertTrue(ableToConnect.isConnected());
        ableToConnect.close();
    }

    @Test
    public void serverAcceptsAdminConnection() throws IOException {
        Socket ableToConnect = new Socket("localhost", adminPort);
        assertTrue(ableToConnect.isConnected());
        ableToConnect.close();
    }

    @Test
    public void testScrappingEndpoint() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .contains("# HELP", "# TYPE");
    }

    @Test
    public void testJDBIMetrics() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .contains("jdbi_requests_seconds");
    }

    public static class TestConfiguration extends Configuration implements MicrometerBundleConfiguration {

        @JsonProperty("prometheus")
        private PrometheusConfiguration prometheus = new PrometheusConfiguration();

        @Override
        public PrometheusConfiguration getPrometheusConfiguration() {
            return prometheus;
        }
    }

    @Path("/")
    public static class TestApp extends Application<TestConfiguration> {

        @Path("/ping")
        public static class PingResource {
            @GET
            public String ping() {
                return "pong";
            }
        }

        @Override
        public void initialize(Bootstrap<TestConfiguration> bootstrap){
            bootstrap.addBundle(new MicrometerBundle());
        }

        @Override
        public void run(TestConfiguration configuration, Environment environment) throws Exception {
            Jdbi jdbi = Jdbi.create("jdbc:h2:mem:test");
            jdbi.setTimingCollector(new MicrometerJdbiTimingCollector());
            environment.jersey().register(new PingResource());

            FilterRegistration.Dynamic prometheusFilter = environment.servlets().addFilter("MicrometerHttpFilter", new MicrometerHttpFilter());
            prometheusFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        }
    }
}
