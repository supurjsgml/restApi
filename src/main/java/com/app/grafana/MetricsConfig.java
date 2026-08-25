package com.app.grafana;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(
            @Value("${spring.application.name:rest-api}") String appName,
            @Value("${CONTAINER_APP_REPLICA_NAME:${POD_NAME:${HOSTNAME:}}}") String configPodName) {
        return registry -> {
            String pod = resolvePodName(configPodName);
            registry.config().commonTags(
                "application", appName,
                "instance", pod,
                "pod", pod
            );
        };
    }

    public static String resolvePodName(String configuredValue) {
        if (configuredValue != null && !configuredValue.isBlank() && !"unknown".equalsIgnoreCase(configuredValue)) {
            return configuredValue;
        }

        String envReplica = System.getenv("CONTAINER_APP_REPLICA_NAME");
        if (envReplica != null && !envReplica.isBlank()) {
            return envReplica;
        }

        String envPod = System.getenv("POD_NAME");
        if (envPod != null && !envPod.isBlank()) {
            return envPod;
        }

        String envHost = System.getenv("HOSTNAME");
        if (envHost != null && !envHost.isBlank()) {
            return envHost;
        }

        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
