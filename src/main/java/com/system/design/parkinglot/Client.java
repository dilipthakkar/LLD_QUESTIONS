package com.system.design.parkinglot;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import com.system.design.parkinglot.payment.AmountCalculator;
import com.system.design.parkinglot.slot.Slot;
import com.system.design.parkinglot.slot.SlotType;
import com.system.design.parkinglot.terminal.EntryTerminal;
import com.system.design.parkinglot.ticket.Ticket;

public class Client {
    public static void main(String[] args) {
        Slot slot1 = new Slot(1, SlotType.SMALL);
        Slot slot2 = new Slot(2, SlotType.SMALL);
        Slot slot3 = new Slot(3, SlotType.SMALL);

        Slot slot4 = new Slot(4, SlotType.MEDIUM);
        Slot slot5 = new Slot(5, SlotType.MEDIUM);

        Slot slot6 = new Slot(6, SlotType.LARGE);

        EntryTerminal entryTerminal1 = new EntryTerminal(1);
        EntryTerminal entryTerminal2 = new EntryTerminal(2);

        SlotTerminalDistanceModal modal1 = new SlotTerminalDistanceModal(slot1, entryTerminal1, 2);
        SlotTerminalDistanceModal modal2 = new SlotTerminalDistanceModal(slot2, entryTerminal1, 3);
        SlotTerminalDistanceModal modal3 = new SlotTerminalDistanceModal(slot3, entryTerminal1, 4);
        SlotTerminalDistanceModal modal4 = new SlotTerminalDistanceModal(slot4, entryTerminal1, 5);
        SlotTerminalDistanceModal modal5 = new SlotTerminalDistanceModal(slot5, entryTerminal1, 6);
        SlotTerminalDistanceModal modal6 = new SlotTerminalDistanceModal(slot6, entryTerminal1, 7);

        SlotTerminalDistanceModal modal7 = new SlotTerminalDistanceModal(slot1, entryTerminal2, 7);
        SlotTerminalDistanceModal modal8 = new SlotTerminalDistanceModal(slot2, entryTerminal2, 5);
        SlotTerminalDistanceModal modal9 = new SlotTerminalDistanceModal(slot3, entryTerminal2, 5);
        SlotTerminalDistanceModal modal10 = new SlotTerminalDistanceModal(slot4, entryTerminal2, 4);
        SlotTerminalDistanceModal modal11 = new SlotTerminalDistanceModal(slot5, entryTerminal2, 3);
        SlotTerminalDistanceModal modal12 = new SlotTerminalDistanceModal(slot6, entryTerminal2, 2);

        ParkingLot parkingLot = new ParkingLot.ParkingLotConfigurer().add(modal1).add(modal2).add(modal3).add(modal4)
                .add(modal5).add(modal6).add(modal7).add(modal8).add(modal9).add(modal10).add(modal11).add(modal12)
                .addAmountCalculate(new AmountCalculator()).build();

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        IntStream.range(0, 20).forEach((i) -> {
            executorService.submit(() -> {
                parkingLot.bookSlot(SlotType.MEDIUM, entryTerminal1, "POOL");
                System.out.println(parkingLot.bookedSlots.size());
            });
        });

        ExecutorService executorService2 = Executors.newFixedThreadPool(20);
        IntStream.range(0, 20).forEach((i) -> {
            executorService2.submit(() -> {
                parkingLot.bookSlot(SlotType.MEDIUM, entryTerminal1, "POOL_2");
                System.out.println(parkingLot.bookedSlots.size());
            });
        });

        executorService.shutdown();
        executorService2.shutdown();

        parkingLot.printAllTickets();

        // Optional<Ticket> ticket = parkingLot.bookSlot(SlotType.MEDIUM,
        // entryTerminal1, "VEH_123");
        // Optional<Ticket> ticket2 = parkingLot.bookSlot(SlotType.MEDIUM,
        // entryTerminal1, "VEH_124");
        // Optional<Ticket> ticket3 = parkingLot.bookSlot(SlotType.SMALL,
        // entryTerminal2, "VEH_125");
        // Optional<Ticket> ticket4 = parkingLot.bookSlot(SlotType.SMALL,
        // entryTerminal2, "VEH_126");
        // Optional<Ticket> ticket5 = parkingLot.bookSlot(SlotType.LARGE,
        // entryTerminal1, "VEH_127");
        // Optional<Ticket> ticket6 = parkingLot.bookSlot(SlotType.MEDIUM,
        // entryTerminal2, "VEH_128");

        // parkingLot.freeSlot(ticket3.get());
        // parkingLot.freeSlot(ticket2.get());
        // Optional<Ticket> ticket7 = parkingLot.bookSlot(SlotType.MEDIUM,
        // entryTerminal1, "VEH_130");

    }
}
