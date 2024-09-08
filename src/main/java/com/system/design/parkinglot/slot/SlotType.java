package com.system.design.parkinglot.slot;

public enum SlotType {
    SMALL(3),
    LARGE(1),
    MEDIUM(2),
    ELECTRIC(4);

    int priority;

    SlotType(Integer priority) {
        this.priority = priority;
    }
}
