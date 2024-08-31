package org.dshaver.sins.domain.ingest.unititem;

public enum ModifierBehavior {
    additive("+"), scalar("%");

    private final String operation;

    ModifierBehavior(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
