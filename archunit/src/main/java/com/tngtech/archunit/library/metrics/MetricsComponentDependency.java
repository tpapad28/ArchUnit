package com.tngtech.archunit.library.metrics;

public class MetricsComponentDependency {
    private final MetricsComponent<?> origin;
    private final MetricsComponent<?> target;

    public MetricsComponentDependency(MetricsComponent<?> origin, MetricsComponent<?> target) {
        this.origin = origin;
        this.target = target;
    }
}
