/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockscalculate;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author JordiL abstract class that defines the variables and the methods
 */
public abstract class Stock {

    protected String symbol;
    protected String type;
    protected double lastDividend;
    protected double fixedDividend;
    protected double parValue;
    protected double dividenYield;
    protected double peRatio;
    protected double geometricMean;
    protected double tickerMeanPrice; //I suppose this attribute is the mean of all the stock trading of a specific Ticker or the last value
    protected double stockPrice;
    protected ArrayList<Ticker> tickers = new ArrayList<Ticker>();  //this List is the prices and quantities of all the Stocks bought

    public Stock(String symbol, String type, double parValue, double lastDividend, double fixedDividend, double tickerMeanPrice, ArrayList<Ticker> tickers) {
        this.symbol = symbol;
        this.type = type;
        this.lastDividend = lastDividend;
        this.fixedDividend = fixedDividend;
        this.tickerMeanPrice = tickerMeanPrice;
        this.parValue = parValue;
        //this.tickers = tickers;
        this.initializeTicker(tickers);
    }

    public void initializeTicker(ArrayList<Ticker> tickers) {
        /**
         * the reason of this method is data security, to prevent modifications
         * of the tickers' object outside of the class
         */
        for (int i = 0; i < tickers.size(); i++) {
            this.addTicker(tickers.get(i).getQuantity(), tickers.get(i).getTradePrice(), tickers.get(i).getOperation());
        }



    }

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return type;
    }

    public double getStockPrice() {
        this.stockPrice = this.calculateStockPrice();
        return stockPrice;
    }

    public double getLastDividend() {
        return lastDividend;
    }

    public double getFixedDividend() {
        return fixedDividend;
    }

    public double getParValue() {
        return parValue;
    }

    public double getDividenYield() {
        this.dividenYield = this.calculateDividendYield();
        return dividenYield;
    }

    public double getTickerMeanPrice() {
        return tickerMeanPrice;
    }

    public double getPeRatio() {
        this.calculatePeRatio();
        return peRatio;
    }

    public double getGeometricMean() {
        return geometricMean;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLastDividend(double lastDividend) {
        this.lastDividend = lastDividend;
    }

    public void setFixedDividend(double fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public void setParValue(double parValue) {
        this.parValue = parValue;
    }

    public void setDividenYield(double dividenYield) {
        this.dividenYield = dividenYield;
    }

    public void setPeRatio(double peRatio) {
        this.peRatio = peRatio;
    }

    public void setGeometricMean(double geometricMean) {
        this.geometricMean = geometricMean;
    }

    public void setTickerMeanPrice(double tickerMeanPrice) {
        this.tickerMeanPrice = tickerMeanPrice;
    }

    protected abstract double calculateDividendYield();

    protected void calculatePeRatio() {
        /**
         * calculate PeRatio of the stock
         *
         */
        if (this.lastDividend != 0) {
            this.peRatio = this.tickerMeanPrice / this.lastDividend;
        } else {
            this.peRatio = -1;
            String message = "Error:dividing by zero,symbol:" + this.symbol;
            Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);


        }

    }

    public void addTicker(int quantity, double price, String operation) {
        /**
         * add a ticker to the arraylist
         */
        this.tickers.add(new Ticker(quantity, price, new Date(), operation));
    }

    public double getGCBE() {
        return this.calculateGBCE();
    }

    protected double calculateGBCE() {
        /**
         * calculate GCBE
         */
        int multiplyValues = 1;
        int i;
        for (i = 0; i < this.tickers.size(); i++) {
            multiplyValues *= this.tickers.get(i).getTradePrice();
        }
        if (i % 2 == 0 && multiplyValues < 0) {


            String message = "Error:sqrt with negatives values,symbol:" + this.symbol;
            Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);


            return -1;
        } else if (multiplyValues < 0) {
            String message = "Error:negative price has no sense,symbol:" + this.symbol;
            Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);

            return -1;
        } else {
            return Math.pow(multiplyValues, i);
        }


    }

    public double calculateStockPriceNoOperation() {
        //it calculates the Stock Price if only one operation is allowed
        double sumOfAmount = 0;
        double sumOfQuantity = 0;
        for (int i = 0; i < this.tickers.size(); i++) {
            sumOfAmount += this.tickers.get(i).tradePrice * this.tickers.get(i).getQuantity();
            sumOfQuantity += this.tickers.get(i).getQuantity();
        }

        if (sumOfQuantity <= 0) {
            String message = "Error:dividing by zero,symbol:" + this.symbol;

            Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);

            return -1;
        } else {
            return sumOfAmount / sumOfQuantity;
        }


    }

    public double calculateStockPrice() {
        /**
         * if operation is buy trade price is positive if it's sell price is
         * negative operation = sell then operationSign = -1 otherwise
         * operationSign = 1
         */
        double sumOfAmount = 0;
        double sumOfQuantity = 0;
        for (int i = 0; i < this.tickers.size(); i++) {
            int operationSign = 1;
            if (this.tickers.get(i).operation.equalsIgnoreCase("sell")) {
                operationSign = -1;
            }

            sumOfAmount += operationSign * this.tickers.get(i).tradePrice * this.tickers.get(i).getQuantity();
            sumOfQuantity += this.tickers.get(i).getQuantity();
        }


        if (sumOfQuantity <= 0) {
            String message = "Error:dividing by zero,symbol:" + this.symbol;
            Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);
            return -1;
        } else {
            return sumOfAmount / sumOfQuantity;
        }


    }

    public double calculateStockPriceIntervalTime(long lastInterval) {
        /**
         * if operation is buy trade price is positive if it's sell price is
         * negative operation = sell then operationSign = -1 otherwise
         * operationSign = 1
         */
        double sumOfAmount = 0;
        double sumOfQuantity = 0;
        Date begDate = new Date((new Date()).getTime() - 15 * 60 * 1000);
        for (int i = 0; i < this.tickers.size(); i++) {
            if (this.tickers.get(i).isInInterval(begDate)) {
                int operationSign = 1;
                if (this.tickers.get(i).operation.equalsIgnoreCase("sell")) {
                    operationSign = -1;
                }

                sumOfAmount += operationSign * this.tickers.get(i).tradePrice * this.tickers.get(i).getQuantity();
                sumOfQuantity += this.tickers.get(i).getQuantity();
            }
        }


        if (sumOfQuantity <= 0) {
            String message = "Error:dividing by zero,symbol:" + this.symbol;
            Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);
            return -1;
        } else {
            return sumOfAmount / sumOfQuantity;
        }


    }
}
