package org.dshaver.domain;

import com.google.common.collect.Multimap;
import lombok.Data;
import org.dshaver.domain.gamefiles.unititem.UnitItem;
import org.dshaver.domain.gamefiles.unititem.UnitItemType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class Manifest {
    Set<String> ids;

    Map<String, UnitItem> unitItemsMap;

    Multimap<UnitItemType, UnitItem> typeIndex;

    public Collection<UnitItem> getByType(UnitItemType type) {
        return typeIndex.get(type);
    }
}
