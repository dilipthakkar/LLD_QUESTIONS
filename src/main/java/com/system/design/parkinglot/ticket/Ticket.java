package com.system.design.parkinglot.ticket;

import java.util.Date;

import com.system.design.parkinglot.slot.Slot;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticket {
    Integer id;
    String vehicleNumber;
    Date entryTime;
    Slot slot;
    Date exitTime;
}
