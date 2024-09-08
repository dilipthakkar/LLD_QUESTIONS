package com.system.design.parkinglot.slot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Slot {
    Integer id;
    SlotType type;

    @Override
    public boolean equals(Object arg0) {
        return (arg0 instanceof Slot && ((Slot) arg0).id == this.id);
    }
}
