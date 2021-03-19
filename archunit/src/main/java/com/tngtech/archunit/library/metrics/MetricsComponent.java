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

import java.util.Collection;
import java.util.Set;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import com.tngtech.archunit.base.ForwardingCollection;

public class MetricsComponent<T> extends ForwardingCollection<T> {
    private final String identifier;
    private final Set<T> elements;

    @SafeVarargs
    MetricsComponent(String identifier, T... elements) {
        this.identifier = identifier;
        this.elements = ImmutableSet.copyOf(elements);
    }

    public Set<T> getElements() {
        return elements;
    }

    @Override
    protected Collection<T> delegate() {
        return elements;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("identifier", identifier)
                .toString();
    }

    public static <T> MetricsComponent<T> of(String identifier, T... elements) {
        return new MetricsComponent<T>(identifier, elements);
    }
}
