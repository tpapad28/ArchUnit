package com.tngtech.archunit.library.metrics;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LakosMetricsTest {
    @Test
    public void Lakos_metrics_of_a_single_component() {
        MetricsComponents components = MetricsComponents.of(MetricsComponent.of("test"));

        LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(components);

        assertThat(metrics.getCumulativeComponentDependency())
                .as("CCD").isEqualTo(1);
        assertThat(metrics.getAverageComponentDependency())
                .as("ACD").isEqualTo(1.0);
        assertThat(metrics.getRelativeAverageComponentDependency())
                .as("RACD").isEqualTo(1.0);
        assertThat(metrics.getNormalizedCumulativeComponentDependency())
                .as("NCCD").isEqualTo(1.0);
    }
}
