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
import com.tngtech.archunit.base.ForwardingCollection;

import java.util.Collection;

public class MetricsComponents<T> extends ForwardingCollection<MetricsComponent<T>> {
    private final ImmutableSet<MetricsComponent<T>> components;

    private MetricsComponents(ImmutableSet<MetricsComponent<T>> components) {
        this.components = components;
    }

    @Override
    protected Collection<MetricsComponent<T>> delegate() {
        return components;
    }

    @SafeVarargs
    public static <T> MetricsComponents<T> of(MetricsComponent<T>... components) {
        return new MetricsComponents<T>(ImmutableSet.copyOf(components));
    }
}
