package com.app.grafana;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
public class ThreadInfoController {

    private final String podName;

    public ThreadInfoController(
            @Value("${CONTAINER_APP_REPLICA_NAME:${POD_NAME:${HOSTNAME:}}}") String configPodName) {
        this.podName = MetricsConfig.resolvePodName(configPodName);
    }

    @Hidden
    @GetMapping(value = "/actuator/threads", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> threads() {
        return Arrays.stream(
            ManagementFactory.getThreadMXBean().dumpAllThreads(false, false))
            .map(t -> {
                Map<String, Object> map = new HashMap<>();
                map.put("pod", podName);
                map.put("name", t.getThreadName());
                map.put("state", t.getThreadState().name());
                map.put("daemon", t.isDaemon());
                return map;
            })
            .toList();
    }
}
