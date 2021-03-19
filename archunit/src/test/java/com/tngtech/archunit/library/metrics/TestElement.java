package com.tngtech.archunit.library.metrics;

import java.util.HashSet;
import java.util.Set;

class TestElement implements HasDependencies<TestElement> {
    final Set<TestElement> dependencies = new HashSet<>();

    @Override
    public Set<TestElement> getDependencies() {
        return dependencies;
    }

    void addDependency(TestElement element) {
        dependencies.add(element);
    }
}
