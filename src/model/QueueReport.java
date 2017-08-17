/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 * Register the queue statistics
 *
 * @author eZe
 */
public class QueueReport {

    private int maxCount;
    private int totalEntries;
    private int zeroEntries;
    private int currentCount;
    private final ArrayList<Integer> avgCount;
    private final ArrayList<Double> avgTime;

    private float average(ArrayList array, boolean withoutZeroEntries) {

        if (array.isEmpty()) {
            return 0;
        }
        float cont = 0;
        for (int i = 0; i < array.size(); i++) {
            if (!withoutZeroEntries || withoutZeroEntries && (Double) array.get(i) != 0.0) {
                cont += (Double) array.get(i);
            }
        }
        return cont / array.size();
    }

    public QueueReport() {
        this.maxCount = 0;
        this.totalEntries = 0;
        this.zeroEntries = 0;
        this.avgCount = new ArrayList<Integer>();
        this.avgTime = new ArrayList<Double>();
    }

    /**
     * Returns the length of the Queue
     *
     * @return
     */
    public int getMaxCount() {
        return this.maxCount;
    }

    /**
     * Returns the percentage of entries that are zero entries
     *
     * @return
     */
    public double getPercentZeros() {

        return (this.zeroEntries / this.totalEntries) * 100;
    }

    /**
     * Returns the entries with 0 waiting time
     *
     * @return
     */
    public int getZeroEntries() {
        return this.zeroEntries;
    }

    /**
     * Returns the number of transactions that passed through the queue
     *
     * @return
     */
    public int getTotalEntries() {
        return this.totalEntries;
    }

    /**
     * the average waiting time of the transactions in the queue
     *
     * @return
     */
    public Float getAvgTime() {
        return average(this.avgTime, false);
    }

    /**
     * the average waiting time of the transactions in the queue
     *
     * @param zero if true returns the average waiting time without zero entries
     * @return
     */
    public Float getAvgTime(boolean zero) {
        return average(this.avgTime, zero);
    }

    /**
     * Returns the entries average time stay
     *
     * @param zero if true returns the stay average time without zero entries
     * @return
     */
    public Float getAvgCount(boolean zero) {

        return this.average(this.avgCount, zero);
    }

    /**
     * Returns the current count of the queue (current length)
     *
     * @return
     */
    public int getCurrentCount() {
        return this.currentCount;
    }

    /**
     * Increments the total entries by entries units
     *
     * @param entries
     */
    public void incTotalEntries(int entries) {
        this.totalEntries += entries;
    }

    /**
     * Increments the current count by entries units
     *
     * @param entries
     */
    public void incCurrentCount(int entries) {
        this.currentCount += entries;
        this.avgCount.add(this.currentCount);
    }

    /**
     * Increments the entries with 0 waiting time
     */
    public void incZeroEntries() {
        this.zeroEntries++;
    }

    /**
     * Decrements the current count by entries units
     *
     * @param entries
     */
    public void decCurrentCount(int entries) {
        this.currentCount -= entries;
        this.avgCount.add(this.currentCount);
    }

    /**
     * Increments the maximum length of the queue by increment units
     *
     * @param increment
     */
    public void incMaxCount(int increment) {
        this.maxCount += increment;
        this.avgCount.add(this.maxCount);
    }

    /**
     * Registers a new transaction time
     *
     * @param time
     */
    public void regAvgTime(double time) {
        this.avgTime.add(time);
    }
}
