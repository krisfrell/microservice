package com.krisfrell.service;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PrometheusCustomMetricService implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private BuildProperties buildProperties;

    private final MeterRegistry meterRegistry;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        registerAppVersionMetric();
    }

    private void registerAppVersionMetric() {
        meterRegistry.gauge("app_version", List.of(new ImmutableTag("version", buildProperties.getVersion())), 1);
    }
}
