package com.system.design.parkinglot;

import com.system.design.parkinglot.slot.Slot;
import com.system.design.parkinglot.terminal.EntryTerminal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SlotTerminalDistanceModal implements Comparable<SlotTerminalDistanceModal> {
    Slot slot;
    EntryTerminal terminal;
    Integer distance;

    @Override
    public int compareTo(SlotTerminalDistanceModal arg0) {
        if (arg0 instanceof SlotTerminalDistanceModal
                && ((SlotTerminalDistanceModal) arg0).slot.getId() == this.slot.getId()
                && ((SlotTerminalDistanceModal) arg0).terminal.getId() == this.terminal.getId())
            return 0;
        if (arg0.getDistance() <= this.getDistance())
            return 1;
        else
            return -1;
    }
}
