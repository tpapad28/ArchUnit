package com.tngtech.archunit.library.metrics;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class LakosMetricsTest {

    @Test
    public void Lakos_metrics_of_a_single_component() {
        MetricsComponent<ComponentContent> component = MetricsComponent.of("component");
        MetricsComponents<ComponentContent> components = MetricsComponents.of(component);

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

    @Test
    public void Lakos_metrics_of_multiple_components() {
        MetricsComponent<ComponentContent> componentA = MetricsComponent.of("component A", new ComponentContent(), new ComponentContent());
        MetricsComponent<ComponentContent> componentB = MetricsComponent.of("component B");
        MetricsComponents<ComponentContent> components = MetricsComponents.of(componentA, componentB);

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



    static class ComponentContent implements HasDependencies {
        final Set<MetricsComponentDependency> dependencies = new HashSet<>();

        @Override
        public Set<MetricsComponentDependency> getDependencies() {
            return dependencies;
        }
    }
}
