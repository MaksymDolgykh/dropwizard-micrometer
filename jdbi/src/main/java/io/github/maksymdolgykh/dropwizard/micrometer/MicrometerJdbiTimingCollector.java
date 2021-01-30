package io.github.maksymdolgykh.dropwizard.micrometer;

import org.jdbi.v3.core.statement.StatementContext;
import org.jdbi.v3.core.statement.TimingCollector;

import io.prometheus.client.Histogram;


public class MicrometerJdbiTimingCollector implements TimingCollector {

    private static final double[] jdbiLatencyBuckets = new double[]
            {.001, .002, .003, .004, .005, .007, .01, .015, .025, .05, .075,
                    .1, .15, .25, .35, .45, .5, .6, .7, .8, .9, 1,
                    1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 1.8, 1.9,
                    2, 2.5, 3, 3.5, 4, 4.5, 5,
                    5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5,
                    10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                    20, 30, 40, 50, 60, 80, 100, 120};

    public static final Histogram jdbiRequests = Histogram.build()
            .name("jdbi_requests_seconds")
            .help("jdbi requests duration distribution")
            .labelNames("query")
            .buckets(jdbiLatencyBuckets)
            .register(MicrometerBundle.prometheusRegistry.getPrometheusRegistry());

    @Override
    public void collect(long elapsedTime, StatementContext ctx) {
        String sqlMethodName;

        try {
            sqlMethodName = ctx.getExtensionMethod().getMethod().getName();
        } catch (Exception e) {
            sqlMethodName = "undefined";
        }
        observePrometheusMetric(sqlMethodName, (elapsedTime / 1E9));
    }

    private void observePrometheusMetric (String sqlMethodName, double elapsedTime) {
        jdbiRequests.labels(sqlMethodName).observe(elapsedTime);
    }
}
