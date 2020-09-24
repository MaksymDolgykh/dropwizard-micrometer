package io.github.maksymdolgykh.dropwizard.micrometer;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.Histogram;

public class MicrometerBundle implements ConfiguredBundle<Configuration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MicrometerBundle.class);

    private static final String scrapePath = "/prometheus";

    private static final double[] httpLatencyBuckets = new double[]
    {.001, .002, .003, .004, .005, .007, .01, .015, .025, .05, .075,
            .1, .15, .25, .35, .45, .5, .6, .7, .8, .9, 1,
            1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9,
            2, 2.5, 3, 3.5, 4, 4.5, 5,
            5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5,
            10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 30, 40, 50, 60, 80, 100, 120};

    public static final PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public static final Histogram httpRequests = Histogram.build()
            .name("http_server_requests_seconds")
            .help("HTTP requests duration distribution")
            .labelNames("method", "status", "uri")
            .buckets(httpLatencyBuckets)
            .register(prometheusRegistry.getPrometheusRegistry());

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        String endpoint = scrapePath;

        // add system and jvm metrics
        new ClassLoaderMetrics().bindTo(prometheusRegistry);
        new JvmMemoryMetrics().bindTo(prometheusRegistry);
        new JvmGcMetrics().bindTo(prometheusRegistry);
        new JvmThreadMetrics().bindTo(prometheusRegistry);
        new UptimeMetrics().bindTo(prometheusRegistry);
        new ProcessorMetrics().bindTo(prometheusRegistry);
        new LogbackMetrics().bindTo(prometheusRegistry);
        
        MetricsServlet servlet = new MetricsServlet(prometheusRegistry.getPrometheusRegistry());

        LOGGER.info("Adding prometheus scraping endpoint as a servlet mapped to: {}", endpoint);
        environment.admin()
                .addServlet("prometheus", servlet)
                .addMapping(endpoint);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    
}
