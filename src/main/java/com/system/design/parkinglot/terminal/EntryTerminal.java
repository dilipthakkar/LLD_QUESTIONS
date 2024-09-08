package com.system.design.parkinglot.terminal;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EntryTerminal implements Terminal {
    Integer id;

    @Override
    public boolean equals(Object arg0) {
        return (arg0 instanceof EntryTerminal && ((EntryTerminal) arg0).id == this.id);
    }
}
