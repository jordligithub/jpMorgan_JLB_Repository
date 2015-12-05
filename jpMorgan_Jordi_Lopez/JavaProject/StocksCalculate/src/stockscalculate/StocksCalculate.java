/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockscalculate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author JordiL
 */
public class StocksCalculate {

    /**
     * There are 2 operations modes. Standar : the input data is read from the
     * inputFile specified in the properties file Random : there is a previous
     * generation of a csv file with random values
     */
    public static void main(String[] args) {

        // TODO code application logic here

        String mode = args[0];

        ArrayList<ArrayList> stockTickets = new ArrayList<ArrayList>();
        String propsFiles = args[1];

        StockProperties.readPropsFile(propsFiles);

        String message = "\r\n--------------------------------------------------------------------------------------\r\n"
                + "Starting Stocks Calcutate" + "\r\n"
                + "mode:" + mode + "\r\n"
                + "popertiesFile:" + propsFiles + "\r\n"
                + "inputFile:" + StockProperties.inputPath + "\r\n"
                + "outputFile:" + StockProperties.outputPath + "\r\n"
                + "logFile:" + StockProperties.logPath;

        Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);

        if (mode.equalsIgnoreCase("random")) {
            StockProperties.symbols.split(",");
            ArrayList<String> symbolsArray = new ArrayList(Arrays.asList(StockProperties.symbols.split(",")));

            Utils.generateInputFile(StockProperties.inputPath, symbolsArray);

        }

        stockTickets = Utils.readCSV(StockProperties.inputPath);

        ArrayList<Stock> stocks = initializeStocks(stockTickets);

        ArrayList<String> out = mountCSVOutput(stocks);
        Utils.writeCSV(StockProperties.outputPath, out);

        message = "\r\nFinishing Stocks Calcutate\r\n" + "--------------------------------------------------------------------------------------\r\n";
        Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);

    }

    public static ArrayList<Stock> initializeStocks(ArrayList<ArrayList> stockTicketsInput) {
        /**
         * Initialize the stocks and all the data needed
         */
        ArrayList<Stock> stocksArray = new ArrayList<Stock>();

        // Stock stockValues = null;

        for (int i = 0; i < stockTicketsInput.size(); i++) {

            ArrayList stockValues = stockTicketsInput.get(i);

            if (stockValues.size() != 9) { //falta añadir el ticker price
                String message = "error";
                Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);
            } else {
                String symbol = (String) stockValues.get(0);
                String type = (String) stockValues.get(1);
                double tickerPrice = Double.parseDouble((String) stockValues.get(2));
                int quantity = Integer.parseInt((String) stockValues.get(3));
                double lastDividend = Double.parseDouble((String) stockValues.get(4));

                String fixedDividendStr = (String) stockValues.get(5);
                fixedDividendStr = fixedDividendStr.replaceAll("%", "");
                double fixedDividend = Double.parseDouble(fixedDividendStr);
                double parValue = Double.parseDouble((String) stockValues.get(6));

                double tickerMeanPrice = Double.parseDouble((String) stockValues.get(7));
                String operation = (String) stockValues.get(8);



                int position = findStock(stocksArray, symbol);
                if (type.equalsIgnoreCase("Common")) {

                    if (position >= 0) {
                        stocksArray.get(position).addTicker(quantity, tickerPrice, operation);
                    } else {
                        ArrayList<Ticker> tickers = new ArrayList<Ticker>();
                        tickers.add(new Ticker(quantity, tickerPrice, new Date(), operation));
                        stocksArray.add(new StockCommon(symbol, type, parValue, lastDividend, fixedDividend, tickerMeanPrice, tickers));

                    }
                } else if (type.equalsIgnoreCase("Preferred")) {
                    if (position >= 0) {
                        stocksArray.get(position).addTicker(quantity, tickerPrice, operation);
                    } else {
                        ArrayList<Ticker> tickers = new ArrayList<Ticker>();
                        tickers.add(new Ticker(quantity, tickerPrice, new Date(), operation));
                        stocksArray.add(new StockPreferred(symbol, type, parValue, lastDividend, fixedDividend, tickerMeanPrice, tickers));

                    }

                } else {
                    String message = "error";
                    Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);

                }
            }

        }
        return stocksArray;

    }

    public static int findStock(ArrayList<Stock> stocksArray, String symbol) {
        /**
         * find in a Array if a specific stock exist determined by symbol Input:
         * stockArray : ArrayList<Stock> : Array with all the stocks read from
         * the input file symbol : String : symbol which position is needed to
         * know Output: position : int : >= 0 if the stock is found <0 if the
         * stock is not found
         */
        for (int i = 0; i < stocksArray.size(); i++) {
            if (stocksArray.get(i).getSymbol().equalsIgnoreCase(symbol)) {
                return i;
            }
        }
        return -1;
    }

    public static ArrayList<String> mountCSVOutput(ArrayList<Stock> stocks) {
        /**
         * it creates an ArrayList with the lines of the output csv Input:
         * stocks : ArrayList<Stock> : array woth all the stocks Output:
         * ArrayList<String> : array with the lines of the output data
         *
         *
         */
        String out = "Symbol;DividendYield;PeRatio;GCBE;StockPrice15min";
        long intervalBegin = new Date().getTime() - StockProperties.lastMinutesInterval;

        ArrayList<String> outArray = new ArrayList<>();
        outArray.add(out);

        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            String symbol = stock.getSymbol();
            try {
                double dividendYield = stock.getDividenYield();
                double peRatio = stock.getPeRatio();
                double gcbe = stock.getGCBE();
                double stockPrice = stock.calculateStockPriceIntervalTime(intervalBegin);

                out = symbol + ";" + dividendYield + ";" + peRatio + ";" + gcbe + ";" + stockPrice;
                outArray.add(out);
            } catch (Exception e) {
                String message = "Error Calculating Stock values for " + symbol;
                Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);

            }

        }

        return outArray;


    }
}
