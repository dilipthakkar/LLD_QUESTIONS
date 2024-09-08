package com.system.design.parkinglot.ticket;

import com.system.design.parkinglot.payment.AmountCalculator;

public class Printer {
    AmountCalculator amountCalculator;

    public Printer(AmountCalculator amountCalculator) {
        this.amountCalculator = amountCalculator;
    }

    public void print(Ticket ticket) {
        System.out.println("------------------------------");
        System.out.println("Ticket ID: " + ticket.id);
        System.out.println("Vehicle Number: " + ticket.getVehicleNumber());
        System.out.println("Entry Time: " + ticket.getEntryTime());
        if (ticket.getExitTime() != null) {
            System.out.println("Exit Time: " + ticket.getExitTime());
        }
        System.out.println("Slot ID: " + ticket.slot.getId());
        System.out.println("Slot Type: " + ticket.slot.getType());
        if (ticket.getExitTime() != null) {
            System.out.println("Total Amount: " + amountCalculator.getAmount(ticket));
        } else {
            System.out.println("Total Amount: Yet to be estimated.....");
        }
        System.out.println("------------------------------");
    }
}
