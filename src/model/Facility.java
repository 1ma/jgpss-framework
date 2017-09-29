/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

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
    private final ArrayList<Float> holdingTimeRecords;
    private final ArrayList<Float> unavailTimeRecords;
    private Xact owningXact;

    public Facility() {
        capturingTransactions = 0;
        counterCount = 0;
        maxCapacity = 1;
        captureCount = 0;
        maxUsage = 0;
        available = true;
        holdingTimeRecords = new CustomArrayList<>();
        unavailTimeRecords = new CustomArrayList<>();
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
        registerStartHoldTime(tr.getMoveTime());
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
        registerStartHoldTime(tr.getMoveTime());
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
     * @param time
     */
    public void setAvailable(boolean available, float time) {

        this.available = available;

        if (this.available && !available) {
            this.registerUnavailStartTime(time);
        } else if (!this.available && available) {
            this.registerUnavailEndsTime(time);
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
        registerEndHoldTime(tr.getMoveTime());
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
        registerEndHoldTime(tr.getMoveTime());
    }

    /**
     * Registers a holding time start capture
     *
     * @param time
     */
    private void registerStartHoldTime(float time) {
        holdingTimeRecords.add(time);
    }

    /**
     * Register a holding time ends capture
     *
     * @param time
     */
    private void registerEndHoldTime(float time) {
        Float startHoldTime = holdingTimeRecords.get(holdingTimeRecords.size() - 1);
        holdingTimeRecords.set(holdingTimeRecords.indexOf(startHoldTime), time - startHoldTime);
    }

    private void registerUnavailStartTime(float time) {
        this.unavailTimeRecords.add(time);
    }

    private void registerUnavailEndsTime(float time) {

        Float startUnavailTime = unavailTimeRecords.get(unavailTimeRecords.size() - 1);
        unavailTimeRecords.set(unavailTimeRecords.indexOf(startUnavailTime), time - startUnavailTime);
    }

    /**
     * Returns The average time the facility is owned by a capturing transaction
     *
     * @return
     */
    public float avgHoldingTime() {
        return holdingTimeRecords.stream().reduce(0f, (x, y) -> x + y) / holdingTimeRecords.size();
    }

    /**
     * Returns The average time the facility was in unavailable state
     *
     * @return
     */
    public float avgUnavailTime() {
        return unavailTimeRecords.stream().reduce(0f, (x, y) -> x + y) / unavailTimeRecords.size();
    }

    /**
     * Returns the total utilization time during the simulation
     */
    public float getUtilizationTime() {
        return holdingTimeRecords.stream().reduce(0f, (x, y) -> x + y);
    }

}
