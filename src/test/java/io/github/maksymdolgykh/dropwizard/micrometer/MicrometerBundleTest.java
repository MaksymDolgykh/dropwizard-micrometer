package io.github.maksymdolgykh.dropwizard.micrometer;


import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;

import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.EnumSet;

import static org.junit.Assert.assertTrue;

@ExtendWith(DropwizardExtensionsSupport.class)
public class MicrometerBundleTest {

    private static int getFreePort() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
        } catch (IOException e) {
        }
        return socket.getLocalPort();
    }

    public static final DropwizardAppExtension EXT = new DropwizardAppExtension(TestApp.class,
            ResourceHelpers.resourceFilePath("testConfig.yaml"),
            ConfigOverride.config("server.applicationConnectors[0].port", String.valueOf(getFreePort())),
            ConfigOverride.config("server.adminConnectors[0].port", String.valueOf(getFreePort())));

    int adminPort = EXT.getAdminPort();
    int appPort = EXT.getLocalPort();
    Client client = EXT.client();

    String ping = client.target("http://localhost:" + appPort + "/ping")
            .request().get(String.class);

    String scrape = client.target("http://localhost:"+ adminPort +"/prometheus")
            .request().get(String.class);
    

    @Test
    public void serverAcceptsAppConnection() throws IOException {
        Socket ableToConnect = new Socket("localhost", appPort);
        assertTrue(ableToConnect.isConnected());
        ableToConnect.close();
    }

    @Test
    public void testPingResource() {
        Assertions.assertThat(ping)
                .isNotNull()
                .matches("pong");
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
    public void testClassLoaderMetrics() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .contains("jvm_classes_loaded", "jvm_classes_unloaded");
    }

    @Test
    public void testJvmMemoryMetrics() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .contains("jvm_buffer_", "jvm_memory_");
    }

    @Test
    public void testJvmGcMetrics() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .contains("jvm_gc_");
    }

    @Test
    public void testJvmThreadMetrics() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .contains("jvm_threads_");
    }

    @Test
    public void testUptimeMetrics() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .contains("process_uptime", "process_start_time");
    }

    @Test
    public void testProcessorMetrics() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .contains("system_cpu_", "system_load_average");
    }

    @Test
    public void testLogbackMetrics() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .contains("logback_events");
    }

    @Test
    public void testHttpLatencyMetrics() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .contains("http_server_requests_seconds");
    }

    @Test
    public void testServletFilter() {
        Assertions.assertThat(scrape)
                .isNotNull()
                .matches("(?s).*http_server_requests_seconds_bucket.*uri=\"/ping\".*");
    }


    @Path("/")
    public static class TestApp extends Application<Configuration> {

        @Path("/ping")
        public static class PingResource {
            @GET
            public String ping() {
                return "pong";
            }
        }

        @Override
        public void initialize(Bootstrap<Configuration> bootstrap){
            bootstrap.addBundle(new MicrometerBundle());
        }

        @Override
        public void run(Configuration configuration, Environment environment) throws Exception {
            environment.jersey().register(new PingResource());

            FilterRegistration.Dynamic prometheusFilter = environment.servlets().addFilter("MicrometerHttpFilter", new MicrometerHttpFilter());
            prometheusFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        }
    }
}
