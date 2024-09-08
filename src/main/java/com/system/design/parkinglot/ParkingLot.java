package com.system.design.parkinglot;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.system.design.parkinglot.payment.AmountCalculator;
import com.system.design.parkinglot.slot.Slot;
import com.system.design.parkinglot.slot.SlotType;
import com.system.design.parkinglot.terminal.EntryTerminal;
import com.system.design.parkinglot.terminal.Terminal;
import com.system.design.parkinglot.ticket.Printer;
import com.system.design.parkinglot.ticket.Ticket;

public class ParkingLot {

    private Map<Terminal, Map<SlotType, TreeSet<SlotTerminalDistanceModal>>> terminalToAvailSlots;
    private Map<Slot, List<SlotTerminalDistanceModal>> slotToTerminalsDistance;
    public Set<Slot> bookedSlots;
    private Integer ticketNumber;
    private List<Ticket> tickets;
    private AmountCalculator amountCalculator;

    public ParkingLot(ParkingLotConfigurer config) {
        this.terminalToAvailSlots = config.terminalToAvailSlots;
        this.slotToTerminalsDistance = config.slotToTerminalsDistance;
        this.bookedSlots = new HashSet<>();
        this.ticketNumber = 0;
        this.tickets = new ArrayList<>();
        this.amountCalculator = config.amountCalculator;
    }

    public static class ParkingLotConfigurer {

        private Map<Terminal, Map<SlotType, TreeSet<SlotTerminalDistanceModal>>> terminalToAvailSlots;
        private Map<Slot, List<SlotTerminalDistanceModal>> slotToTerminalsDistance;
        private AmountCalculator amountCalculator;

        public ParkingLotConfigurer() {
            terminalToAvailSlots = new HashMap<>();
            slotToTerminalsDistance = new HashMap<>();
        }

        public ParkingLotConfigurer addAmountCalculate(AmountCalculator amountCalculator) {
            this.amountCalculator = amountCalculator;
            return this;
        }

        public ParkingLotConfigurer add(SlotTerminalDistanceModal data) {
            if (!this.terminalToAvailSlots.containsKey(data.terminal)) {
                this.terminalToAvailSlots.put(data.terminal, new EnumMap<>(SlotType.class));
            }

            Map<SlotType, TreeSet<SlotTerminalDistanceModal>> typeToAvailSlots = this.terminalToAvailSlots
                    .get(data.terminal);

            if (!typeToAvailSlots.containsKey(data.slot.getType())) {
                typeToAvailSlots.put(data.slot.getType(), new TreeSet<>());
            }

            typeToAvailSlots.get(data.slot.getType()).add(data);

            slotToTerminalsDistance.compute(data.slot, (slot, distanceList) -> {
                if (distanceList == null)
                    distanceList = new ArrayList<>();
                distanceList.add(data);
                return distanceList;
            });

            return this;
        }

        public ParkingLot build() {
            return new ParkingLot(this);
        }
    }

    public synchronized Optional<Slot> getSlot(SlotType slotType, EntryTerminal entryTerminal) {
        TreeSet<SlotTerminalDistanceModal> slotTerminalDistanceModals = this.terminalToAvailSlots.get(entryTerminal)
                .get(slotType);

        if (slotTerminalDistanceModals.isEmpty())
            return Optional.empty();

        SlotTerminalDistanceModal slotTerminalDistanceModal = slotTerminalDistanceModals
                .pollFirst();

        for (Map<SlotType, TreeSet<SlotTerminalDistanceModal>> maps : this.terminalToAvailSlots.values()) {
            maps.get(slotType).remove(slotTerminalDistanceModal);
        }

        return Optional.of(slotTerminalDistanceModal.getSlot());
    }

    public Optional<Ticket> bookSlot(SlotType slotType, EntryTerminal entryTerminal, String vehicleNumber) {
        Optional<Slot> optionalSlot = getSlot(slotType, entryTerminal);

        if (!optionalSlot.isPresent())
            return Optional.empty();

        Slot slot = optionalSlot.get();

        bookedSlots.add(slot);

        Ticket ticket = new Ticket(this.ticketNumber++, vehicleNumber, new Date(), slot, null);
        tickets.add(ticket);

        return Optional.of(ticket);
    }

    public Integer freeSlot(Ticket ticket) {
        Slot slot = ticket.getSlot();

        bookedSlots.remove(slot);

        List<SlotTerminalDistanceModal> slotToTerminalsDistances = this.slotToTerminalsDistance.get(slot);
        for (SlotTerminalDistanceModal slotToDistance : slotToTerminalsDistances) {
            this.terminalToAvailSlots.get(slotToDistance.getTerminal()).get(slot.getType()).add(slotToDistance);
        }

        ticket.setExitTime(new Date());

        return this.amountCalculator.getAmount(ticket);
    }

    public void printAllTickets() {
        Printer printer = new Printer(this.amountCalculator);

        for (Ticket ticket : this.tickets) {
            printer.print(ticket);
        }
    }

}
