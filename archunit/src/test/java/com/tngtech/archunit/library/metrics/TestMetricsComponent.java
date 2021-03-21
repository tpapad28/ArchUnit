package com.tngtech.archunit.library.metrics;

import static com.google.common.collect.Iterables.getOnlyElement;

class TestMetricsComponent extends MetricsComponent<TestElement> {
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
