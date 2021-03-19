package com.tngtech.archunit.library.metrics;

import com.tngtech.archunit.core.domain.JavaClassTransitiveDependenciesTest;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.assertj.core.data.Offset;
import org.junit.Test;

import static com.google.common.collect.Iterables.getOnlyElement;
import static com.tngtech.archunit.testutil.Assertions.assertThatDependencies;
import static org.assertj.core.api.Assertions.assertThat;

public class LakosMetricsTest {

    @Test
    public void lakos_metrics_of_a_single_component() {
        MetricsComponent<TestElement> component = new TestMetricsComponent("component");
        MetricsComponents<TestElement> components = MetricsComponents.of(component);

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
    public void lakos_metrics_of_two_dependent_components() {
        TestMetricsComponent componentA = new TestMetricsComponent("component A");
        TestMetricsComponent componentB = new TestMetricsComponent("component B");
        componentA.addDependency(componentB);
        MetricsComponents<TestElement> components = MetricsComponents.of(componentA, componentB);

        LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(components);

        assertThat(metrics.getCumulativeComponentDependency())
                .as("CCD").isEqualTo(3);
        assertThat(metrics.getAverageComponentDependency())
                .as("ACD").isEqualTo(1.5);
        assertThat(metrics.getRelativeAverageComponentDependency())
                .as("RACD").isEqualTo(0.75);
        assertThat(metrics.getNormalizedCumulativeComponentDependency())
                .as("NCCD").isEqualTo(1);
    }

    @Test
    public void lakos_metrics_of_multiple_dependent_components() {
        TestMetricsComponent componentA = new TestMetricsComponent("component A");
        TestMetricsComponent componentB = new TestMetricsComponent("component B");
        TestMetricsComponent componentC = new TestMetricsComponent("component C");
        componentA.addDependency(componentB);
        componentB.addDependency(componentC);
        MetricsComponents<TestElement> components = MetricsComponents.of(componentA, componentB, componentC);

        LakosMetrics metrics = ArchitectureMetrics.lakosMetrics(components);

        assertThat(metrics.getCumulativeComponentDependency())
                .as("CCD").isEqualTo(6);
        assertThat(metrics.getAverageComponentDependency())
                .as("ACD").isEqualTo(2);
        assertThat(metrics.getRelativeAverageComponentDependency())
                .as("RACD").isCloseTo(2/3.0, Offset.offset(0.01));
        assertThat(metrics.getNormalizedCumulativeComponentDependency())
                .as("NCCD").isCloseTo(6/5.0, Offset.offset(0.01));
    }

    static class TestMetricsComponent extends MetricsComponent<TestElement> {
        TestMetricsComponent(String identifier) {
            super(identifier, new TestElement());
        }

        void addDependency(TestMetricsComponent target) {
            getOnlyElement(getElements()).addDependency(getOnlyElement(target.getElements()));
        }

        public void addDependencies(TestMetricsComponent... components) {
            for (TestMetricsComponent component : components) {
                addDependency(component);
            }
        }
    }

}
