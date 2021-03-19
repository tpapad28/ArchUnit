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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;

public class MetricsComponentDependencyGraph<T extends HasDependencies<T>> {
    private final SetMultimap<MetricsComponent<T>, MetricsComponent<T>> componentDependencies;

    private MetricsComponentDependencyGraph(Iterable<MetricsComponent<T>> components) {
        this.componentDependencies = createComponentDependencies(components);
    }

    private ImmutableSetMultimap<MetricsComponent<T>, MetricsComponent<T>> createComponentDependencies(Iterable<MetricsComponent<T>> components) {
        Map<T, MetricsComponent<T>> componentsByElements = indexComponentByElement(components);
        ImmutableSetMultimap.Builder<MetricsComponent<T>, MetricsComponent<T>> componentDependencies = ImmutableSetMultimap.builder();
        for (MetricsComponent<T> component : components) {
            componentDependencies.putAll(component, createDependenciesOf(component, componentsByElements));
        }
        return componentDependencies.build();
    }

    private Map<T, MetricsComponent<T>> indexComponentByElement(Iterable<MetricsComponent<T>> components) {
        Map<T, MetricsComponent<T>> componentsByElements = new HashMap<>();
        for (MetricsComponent<T> c : components) {
            for (T element : c.getElements()) {
                componentsByElements.put(element, c);
            }
        }
        return componentsByElements;
    }

    private ImmutableSet<MetricsComponent<T>> createDependenciesOf(MetricsComponent<T> component, Map<T, MetricsComponent<T>> componentsByElements) {
        ImmutableSet.Builder<MetricsComponent<T>> builder = ImmutableSet.builder();
        for (T element : component.getElements()) {
            for (T dependency : element.getDependencies()) {
                MetricsComponent<T> target = componentsByElements.get(dependency);
                if (target != null && !target.equals(component)) {
                    builder.add(target);
                }
            }
        }
        return builder.build();
    }

    @SafeVarargs
    public static <T extends HasDependencies<T>> MetricsComponentDependencyGraph<T> of(MetricsComponent<T>... components) {
        return new MetricsComponentDependencyGraph<T>(ImmutableSet.copyOf(components));
    }

    public static <T extends HasDependencies<T>> MetricsComponentDependencyGraph<T> of(Iterable<MetricsComponent<T>> components) {
        return new MetricsComponentDependencyGraph<T>(components);
    }

    public Set<MetricsComponent<T>> getDirectDependenciesOf(MetricsComponent<T> origin) {
        return componentDependencies.get(origin);
    }

    public Set<MetricsComponent<T>> getTransitiveDependencies(MetricsComponent<T> origin) {
        ImmutableSet.Builder<MetricsComponent<T>> transitiveDependencies = ImmutableSet.builder();
        Set<JavaClass> analyzedClasses = new HashSet<>();  // to avoid infinite recursion for cyclic dependencies
        addTransitiveDependenciesFrom(javaClass, transitiveDependencies, analyzedClasses);
        return transitiveDependencies.build();
    }

    private static void addTransitiveDependenciesFrom(JavaClass javaClass, ImmutableSet.Builder<Dependency> transitiveDependencies, Set<JavaClass> analyzedClasses) {
        analyzedClasses.add(javaClass);  // currently being analyzed
        Set<JavaClass> targetClassesToRecurse = new HashSet<>();
        for (Dependency dependency : javaClass.getDirectDependenciesFromSelf()) {
            transitiveDependencies.add(dependency);
            targetClassesToRecurse.add(dependency.getTargetClass().getBaseComponentType());
        }
        for (JavaClass targetClass : targetClassesToRecurse) {
            if (!analyzedClasses.contains(targetClass)) {
                addTransitiveDependenciesFrom(targetClass, transitiveDependencies, analyzedClasses);
            }
        }
    }

}
