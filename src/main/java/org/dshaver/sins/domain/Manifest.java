package org.dshaver.sins.domain;

import com.google.common.collect.Multimap;
import lombok.Data;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Data
public class Manifest<T, U> {
    Set<String> ids;

    Map<String, U> idMap;

    Multimap<T, U> typeIndex;

    public Collection<U> getByType(T type) {
        return typeIndex.get(type);
    }

    public Optional<U> getById(String id) {
        return Optional.ofNullable(idMap.get(id));
    }
}
