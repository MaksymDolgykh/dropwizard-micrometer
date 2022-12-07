package io.github.maksymdolgykh.dropwizard.micrometer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.dropwizard.Configuration;

public class PrometheusConfiguration extends Configuration {

    @JsonProperty("endpoint")
    @JsonSetter(nulls = Nulls.SKIP)
    private String endpoint = "/prometheus";

    public String getEndpoint() {
        return endpoint;
    }
}
