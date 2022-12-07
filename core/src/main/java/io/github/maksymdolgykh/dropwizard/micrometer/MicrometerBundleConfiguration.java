package io.github.maksymdolgykh.dropwizard.micrometer;

import io.dropwizard.Configuration;

public interface MicrometerBundleConfiguration<T extends Configuration> {
    PrometheusConfiguration getPrometheusConfiguration();
}
