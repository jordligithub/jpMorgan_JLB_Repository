/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockscalculate;

import java.util.Date;

/**
 *
 * @author JordiL
 */
public class Ticker {

    public double tradePrice;
    public int quantity;
    public Date timeRecord;
    public String operation; //sell or buy

    public Ticker(int quantity, double tradePrice, Date timeRecord, String operation) {
        this.tradePrice = tradePrice;
        this.quantity = quantity;
        this.timeRecord = timeRecord;
        this.operation = operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public double getTradePrice() {
        return tradePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setTimeRecord(Date timeRecord) {
        this.timeRecord = timeRecord;
    }

    public Date getTimeRecord() {
        return timeRecord;
    }

    public long getTimeRecordinMillis() {
        return timeRecord.getTime();
    }

    public boolean isInInterval(Date begDate) {
        return this.timeRecord.after(begDate);
    }

    public void setTradePrice(double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
