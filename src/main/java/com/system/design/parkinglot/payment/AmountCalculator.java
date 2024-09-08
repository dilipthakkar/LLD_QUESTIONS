package com.system.design.parkinglot.payment;

import java.util.Date;

import com.system.design.parkinglot.ticket.Ticket;

public class AmountCalculator {

    public Integer getAmount(Ticket ticket) {
        Date exitTime = ticket.getExitTime();
        Date entryTime = ticket.getEntryTime();
        long timeDiffInMinutes = (exitTime.getTime() - entryTime.getTime());
        return (int) timeDiffInMinutes * 10;
    }

}
