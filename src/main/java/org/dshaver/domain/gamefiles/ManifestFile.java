package org.dshaver.domain.gamefiles;

import lombok.Data;

import java.util.Set;

@Data
public class ManifestFile {
    Set<String> ids;
}
