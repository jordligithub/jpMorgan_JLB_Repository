/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockscalculate;

import java.util.ArrayList;

/**
 *
 * @author JordiL
 */
public class StockPreferred extends Stock {

    public StockPreferred(String symbol, String type, double parValue, double lastDividend, double fixedDividend, double tickerMeanPrice, ArrayList<Ticker> tickers) {
        super(symbol, type, parValue, lastDividend, fixedDividend, tickerMeanPrice, tickers);
    }

    @Override
    protected double calculateDividendYield() {

        if (this.tickerMeanPrice == 0) {
            String message = "Error:dividing by zero";
            Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);
            return -1;

        } else if (this.tickerMeanPrice < 0) {
            String message = "Error:negative mean has no sense";
            Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);
            return -1;
        } else {
            return this.dividenYield = (this.fixedDividend * this.parValue) / this.tickerMeanPrice;
        }

    }
}
