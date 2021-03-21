package com.tngtech.archunit.library.metrics;

import com.tngtech.archunit.core.domain.JavaClassTransitiveDependenciesTest;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.Test;

import static com.tngtech.archunit.testutil.Assertions.assertThatDependencies;
import static org.assertj.core.api.Assertions.assertThat;

public class MetricsComponentDependencyGraphTest {

    @Test
    public void should_create_simple_dependency_between_two_components() {
        TestElement elementOfComponent1 = new TestElement();
        TestElement elementOfComponent2 = new TestElement();

        elementOfComponent1.addDependency(elementOfComponent2);

        MetricsComponent<TestElement> component1 = MetricsComponent.of("component1", elementOfComponent1);
        MetricsComponent<TestElement> component2 = MetricsComponent.of("component2", elementOfComponent2);

        MetricsComponentDependencyGraph<TestElement> graph = MetricsComponentDependencyGraph.of(component1, component2);

        assertThat(graph.getDirectDependenciesOf(component1)).containsOnly(component2);
    }

    @Test
    public void should_ignore_dependencies_inside_of_same_component() {
        TestElement component1Element1 = new TestElement();
        TestElement component1Element2 = new TestElement();
        TestElement component2Element1 = new TestElement();

        component1Element1.addDependency(component1Element2);
        component1Element1.addDependency(component2Element1);

        MetricsComponent<TestElement> component1 = MetricsComponent.of("component1", component1Element1, component1Element2);
        MetricsComponent<TestElement> component2 = MetricsComponent.of("component2", component2Element1);

        MetricsComponentDependencyGraph<TestElement> graph = MetricsComponentDependencyGraph.of(component1, component2);

        assertThat(graph.getDirectDependenciesOf(component1)).containsOnly(component2);
    }

    @Test
    public void should_ignore_dependencies_outside_of_components() {
        TestElement elementInsideOfComponent = new TestElement();
        TestElement elementOutsideOfComponent = new TestElement();

        elementInsideOfComponent.addDependency(elementOutsideOfComponent);

        MetricsComponent<TestElement> component = MetricsComponent.of("component", elementInsideOfComponent);
        MetricsComponentDependencyGraph<TestElement> graph = MetricsComponentDependencyGraph.of(component);

        assertThat(graph.getDirectDependenciesOf(component)).as("dependencies of component").isEmpty();
    }

    @Test
    public void findsTransitiveDependenciesInAcyclicGraph() {
        TestMetricsComponent a = new TestMetricsComponent("A");
        TestMetricsComponent b = new TestMetricsComponent("B");
        TestMetricsComponent c = new TestMetricsComponent("C");
        TestMetricsComponent d = new TestMetricsComponent("D");
        a.addDependencies(b, c);
        c.addDependency(d);

        MetricsComponentDependencyGraph<TestElement> graph = MetricsComponentDependencyGraph.of(a, b, c, d);

        assertThat(graph.getTransitiveDependencies(a)).containsOnly(b,c,d);
        assertThat(graph.getTransitiveDependencies(b)).isEmpty();
        assertThat(graph.getTransitiveDependencies(c)).containsOnly(d);
        assertThat(graph.getTransitiveDependencies(d)).isEmpty();
    }

    @Test
    public void findsTransitiveDependenciesInCyclicGraph() {
        TestMetricsComponent a = new TestMetricsComponent("A");
        TestMetricsComponent b = new TestMetricsComponent("B");
        TestMetricsComponent c = new TestMetricsComponent("C");
        TestMetricsComponent d = new TestMetricsComponent("D");
        TestMetricsComponent e = new TestMetricsComponent("E");
        a.addDependencies(b, c, d);
        c.addDependency(a);
        d.addDependency(e);
        e.addDependency(a);

        MetricsComponentDependencyGraph<TestElement> graph = MetricsComponentDependencyGraph.of(a, b, c, d, e);

        assertThat(graph.getTransitiveDependencies(a)).containsOnly(b, c, d, e, a);
        assertThat(graph.getTransitiveDependencies(b)).isEmpty();
        assertThat(graph.getTransitiveDependencies(c)).containsOnly(a, b, c, d, e);
        assertThat(graph.getTransitiveDependencies(d)).containsOnly(e, a, b, c, d);
        assertThat(graph.getTransitiveDependencies(e)).containsOnly(e, a, b, c, d);
    }
}
