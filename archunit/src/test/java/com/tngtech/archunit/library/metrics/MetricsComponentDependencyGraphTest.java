package com.tngtech.archunit.library.metrics;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class MetricsComponentDependencyGraphTest {

    @Test
    public void graph_should_be_built_from_components(){
        TestElement component1Element1 = new TestElement();
        TestElement component1Element2 = new TestElement();
        TestElement component2Element1 = new TestElement();

        component1Element1.addDependency(component1Element2);
        component1Element1.addDependency(component2Element1);

        MetricsComponent<TestElement> component1 = MetricsComponent.of("component1", component1Element1, component1Element2);
        MetricsComponent<TestElement> component2 = MetricsComponent.of("component2", component2Element1);

        MetricsComponentDependencyGraph<TestElement> graph = MetricsComponentDependencyGraph.of(component1, component2);

        assertThat(graph.dependenciesOf(component1)).containsOnly(component2);
    }



    static class TestElement implements HasDependencies<TestElement> {
        final Set<TestElement> dependencies = new HashSet<>();

        @Override
        public Set<TestElement> getDependencies() {
            return dependencies;
        }

        void addDependency(TestElement element){
            dependencies.add(element);
        }
    }
}