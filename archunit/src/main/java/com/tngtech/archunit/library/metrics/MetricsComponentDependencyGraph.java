/*
 * Copyright 2014-2021 TNG Technology Consulting GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tngtech.archunit.library.metrics;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;

public class MetricsComponentDependencyGraph<T extends HasDependencies<T>> {
    private final Iterable<MetricsComponent<T>> components;

    public MetricsComponentDependencyGraph(Iterable<MetricsComponent<T>> components) {
        this.components = components;
    }

    public static <T extends HasDependencies<T>> MetricsComponentDependencyGraph<T> of(MetricsComponent<T>... components) {
        return new MetricsComponentDependencyGraph<T>(ImmutableSet.copyOf(components));
    }

    public static <T extends HasDependencies<T>> MetricsComponentDependencyGraph<T> of(Iterable<MetricsComponent<T>> components) {
        return new MetricsComponentDependencyGraph<T>(components);
    }

    public Set<MetricsComponent<T>> dependenciesOf(MetricsComponent<T> component) {
        Map<T, MetricsComponent<T>> reverseLookup = Maps.newHashMap();
        for (MetricsComponent<T> c : components) {
            for (T element : c.getElements()) {
                reverseLookup.put(element, c);
            }
        }

        ImmutableSet.Builder<MetricsComponent<T>> builder = ImmutableSet.builder();
        for (T element : component.getElements()) {
            for (T dependency : element.getDependencies()) {
                MetricsComponent<T> lookedUpComponent = reverseLookup.get(dependency);
                if (!lookedUpComponent.equals(component)) {
                    builder.add(lookedUpComponent);
                }
            }
        }

        return builder.build();
    }
}
