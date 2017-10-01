/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Represents the state of a facility
 *
 * @author Ezequiel Andujar Montes
 */
public class Facility {

    private int capturingTransactions;
    private int counterCount;
    private int maxCapacity;
    private int captureCount;
    private int maxUsage;
    private boolean available;
    private final Deque<TimeRecord> holdingTimeRecords;
    private final Deque<TimeRecord> unavailTimeRecords;
    private Xact owningXact;
    private Model model;

    public Facility(Model model, int maxCapacity) {
        this(model);
        this.maxCapacity = maxCapacity;
    }

    public Facility(Model model) {
        capturingTransactions = 0;
        counterCount = 0;
        maxCapacity = 1;
        captureCount = 0;
        maxUsage = 0;
        available = true;
        holdingTimeRecords = new ArrayDeque<>();
        unavailTimeRecords = new ArrayDeque<>();
        this.model = model;
    }

    /**
     * Returns the number of transaction that has been seized the facility
     *
     * @return
     */
    public int getCaptureCount() {
        return captureCount;
    }

    /**
     * Return the number of transactions that have crossed the facility
     *
     * @return
     */
    public int getCounterCount() {
        return counterCount;
    }

    /**
     * Returns the current number of transactions that seized the facility
     *
     * @return
     */
    public int getCapturingTransactions() {
        return capturingTransactions;
    }

    /**
     * Returns the available capacity of the facility
     *
     * @return
     */
    public int getAvailableCapacity() {
        return maxCapacity - captureCount;
    }

    /**
     * Returns Maximum storage in use at Storage
     *
     * @return
     */
    public int getMaxUsage() {
        return maxUsage;
    }

    /**
     * Sets the maximum capacity of the facilty
     *
     * @param maxCapacity
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * Try to capture the server and sets the current owning transaction.
     * Returns true if success, false otherwise
     *
     * @param tr The owning transaction
     * @return
     */
    public boolean capture(Xact tr) {

        if (capturingTransactions == maxCapacity || !available || (!available && !tr.ownershipGranted())) {
            return false;
        }
        owningXact = tr;
        counterCount++;
        maxUsage++;
        capturingTransactions++;
        captureCount++;
        registerStartHoldTime();
        return true;
    }

    /**
     * Try to capture the server by count transactions and sets the current
     * owning transaction. Returns true if success, false otherwise
     *
     * @param count Number of transaction that attempts to own the facility
     * @param tr The owning transaction
     * @return
     */
    public boolean capture(int count, Xact tr) {

        if (capturingTransactions + count >= maxCapacity || !available || (!available && !tr.ownershipGranted())) {
            return false;
        }
        owningXact = tr;
        counterCount += count;
        capturingTransactions += count;
        captureCount += count;
        registerStartHoldTime();
        return true;
    }

    /**
     * Returns the transaction that currently owns the facility
     *
     * @return
     */
    public Xact getOwningXact() {
        return owningXact;
    }

    /**
     * Removes the owning transaction from contention for the facility
     */
    public void removeOwningXact() {
        this.owningXact = null;
    }

    /**
     * Return true if the storage has space enought left and is available, false
     * otherwise
     *
     * @return
     */
    public boolean isAvailable() {
        return capturingTransactions < maxCapacity && available;
    }

    /**
     * Sets the availability of the facility
     *
     * @param available
     */
    public void setAvailable(boolean available) {

        this.available = available;

        if (this.available && !available) {
            registerUnavailStartTime();
        } else if (!this.available && available) {
            registerUnavailEndsTime();
        }
    }

    /**
     * Return true if the storage is empty, false otherwise
     *
     * @return
     */
    public boolean storageEmpty() {
        return capturingTransactions == 0;
    }

    /**
     * Return true if the storage is full, false otherwise
     *
     * @return
     */
    public boolean storageFull() {
        return capturingTransactions == maxCapacity;
    }

    /**
     * Realeases the facility from the owning transaction
     *
     * @param tr The transaction that releases the facility
     */
    public void release(Xact tr) {
        capturingTransactions = 0;
        owningXact = null;
        registerEndHoldTime();
    }

    /**
     * Releases count transactions from the facility
     *
     * @param count number of transactions that releases the facility
     * @param tr The transaction that releases the facility
     */
    public void release(int count, Xact tr) {
        capturingTransactions -= count;
        owningXact = null;
        registerEndHoldTime();
    }

    /**
     * Registers a holding time start capture
     *
     * @param time
     */
    private void registerStartHoldTime() {

        float time = model.getRelativeClock();
        holdingTimeRecords.push(new TimeRecord(time, time));

    }

    /**
     * Register a holding time ends capture
     *
     * @param time
     */
    private void registerEndHoldTime() {

        if (!holdingTimeRecords.isEmpty()) {
            holdingTimeRecords.peekFirst().setEndTime(model.getRelativeClock());
        }
    }

    private void registerUnavailStartTime() {

        float time = model.getRelativeClock();
        this.unavailTimeRecords.push(new TimeRecord(time, time));
    }

    private void registerUnavailEndsTime() {
        if (!unavailTimeRecords.isEmpty()) {
            unavailTimeRecords.peekFirst().setEndTime(model.getRelativeClock());
        }
    }

    /**
     * Returns The average time the facility is owned by a capturing transaction
     *
     * @return
     */
    public float avgHoldingTime() {

        float sum = holdingTimeRecords.stream()//
                .map(rt -> rt.totalTime())//
                .reduce(0f, (x, y) -> x + y);

        int total = holdingTimeRecords.size();

        return total != 0 ? sum / total : 0;
    }

    /**
     * Returns The average time the facility was in unavailable state
     *
     * @return
     */
    public float avgUnavailTime() {
        float sum = unavailTimeRecords.stream()//
                .map(tr -> tr.totalTime())//
                .reduce(0f, (x, y) -> x + y);

        int total = unavailTimeRecords.size();

        return total != 0 ? sum / total : 0;
    }

    /**
     * Returns the total utilization time during the simulation
     *
     * @return
     */
    public float getUtilizationTime() {
        return holdingTimeRecords.stream()//
                .map(tr -> tr.totalTime())//
                .reduce(0f, (x, y) -> x + y);
    }

}
