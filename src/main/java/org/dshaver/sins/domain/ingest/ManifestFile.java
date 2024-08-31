package org.dshaver.sins.domain.ingest;

import lombok.Data;

import java.util.Set;

@Data
public class ManifestFile {
    Set<String> ids;
}
