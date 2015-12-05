/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockscalculate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author JordiL
 */
public class Utils {

    public static ArrayList<ArrayList> readCSV(String filePath) {
        /**
         * read a CSV file input : filePath : the path of input file output :
         * ArrayList with the read values
         */
        ArrayList output = new ArrayList();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(filePath));
            scanner.useDelimiter(";");

            int lineCounter = -1;
            while (scanner.hasNextLine()) {
                lineCounter++;
                String line = scanner.nextLine();
                if (lineCounter > 0) {
                    ArrayList temp = new ArrayList();
                    Scanner lineScanner = new Scanner(line);
                    lineScanner.useDelimiter(";");

                    while (lineScanner.hasNext()) {
                        String token = lineScanner.next();

                        if (token.isEmpty()) {
                            token = "0";
                        }
                        temp.add(token);
                    }
                    lineScanner.close();
                    output.add(temp);
                }
            }




        } catch (FileNotFoundException ex) {
            String message = "File " + filePath + " Not Found";
            Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);
        } finally {
            if (scanner != null) {
                scanner.close();
            }

        }
        return output;
    }

    public static void writeCSV(String outputFile, ArrayList<String> out) {
        {
            /**
             * write CSV file input : outputFile : String : the path of input
             * file : out : ArrayList<String> : the lines to be written in a csv
             * file output : void
             */
            Collection<String> outCollection = out;
            File file = new File(outputFile);
            try {
                System.out.println(outCollection.toString());
                FileUtils.writeLines(file, outCollection);
            } catch (IOException ex) {
                String message = "File " + outputFile + " Not Found or Error Writing";
                Utils.mostrarMensajePorConsola(StockProperties.showMessage, StockProperties.logPath, StockProperties.showClassInfo, StockProperties.logLevel, message);
            }
        }
    }

    public static void mostrarMensajePorConsola(boolean showMessage, String outLogFile, boolean showClassInfo, String logLevel, String message) {
        /*
         * it writes or shows in console the logs messages
         * showMessage : boolean : if true the log message is written
         * outLogFile : String : log file
         * showClassInfo : boolean : shows information like class, function, line,where the message is written
         * logLevel : String : not used
         * message : String : message written
         */

        String nameClass = java.lang.Thread.currentThread().getStackTrace()[2].getClassName();
        String nameFunction = java.lang.Thread.currentThread().getStackTrace()[2].getMethodName();
        int lineNumber = java.lang.Thread.currentThread().getStackTrace()[2].getLineNumber();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);

        if (showMessage) {
            String retornoLinea = "\r\n";
            String messageOut;
            if (showClassInfo) {
                messageOut = "[" + time + "] [" + logLevel + "] [" + nameClass + "/" + nameFunction + ":" + lineNumber + "] " + message + retornoLinea;
            } else {
                messageOut = "[" + time + "] [" + logLevel + "] " + message + retornoLinea;
            }

            if (outLogFile != null) {
                try {
                    File yourFile = new File(outLogFile);
                    if (!(yourFile.exists())) {
                        System.out.println("File " + outLogFile + " not found");
                        yourFile.createNewFile();
                    }
                    FileOutputStream oFile = new FileOutputStream(yourFile, true);

                    PrintStream p = new PrintStream(new BufferedOutputStream(oFile));

                    p.append(messageOut);
                    p.flush();
                } catch (FileNotFoundException ex) {
                    System.out.println("Fichero:" + outLogFile + " no encontrado");
                    System.out.println(messageOut);
                } catch (IOException ex) {
                    System.out.println("Fichero LOG-error:" + outLogFile);
                    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.print(messageOut);
            }
        }
    }

    public static void generateInputFile(String inputFile, ArrayList<String> symbols) {
        /**
         * generate random data input file as csv
         *
         */
        ArrayList<String> inputArray = new ArrayList();
        HashMap meanprice = new HashMap();
        inputArray.add("stockSymbol;type;tickerPrice;quantity;lastDividend;fixedDividend;parValue;tickerMeanPrice;operation");
        int ii = (int) Math.round((1000 * Math.random()));
        for (int i = 0; i < ii; i++) {
            long val = 5 - Math.round((10 * Math.random()));
            String operation;
            if (val > 0) {
                operation = "buy";
            } else {
                operation = "sell";
            }

            val = 5 - Math.round((10 * Math.random()));
            String type;
            if (val > 0) {
                type = "Common";
            } else {
                type = "Preferred";
            }

            do {
                val = Math.round((symbols.size() * Math.random()));
            } while (val >= symbols.size());

            String symbol = symbols.get((int) val);

            String quantity = String.valueOf((int) Math.round((200 * Math.random())));

            double rootValue = (double) Math.round((100 * Math.random()));
            double lastDividend = rootValue * (1 + (1 - Math.round((2 * Math.random()))) / 2);
            double tickerPrice = rootValue * (1 + (1 - Math.round((2 * Math.random()))) / 2);
            double tickerMeanPrice;
            if (meanprice.get(symbol) == null) {
                tickerMeanPrice = rootValue * (1 + (1 - Math.round((2 * Math.random()))) / 2);
                meanprice.put(symbol, tickerMeanPrice);
            } else {
                tickerMeanPrice = (double) meanprice.get(symbol);

            }
            double parValue = (double) Math.round((100 * Math.random()));

            double fixedDividend = (double) Math.round((100 * Math.random()));

            String output = symbol + ";" + type + ";" + tickerPrice + ";" + quantity + ";" + lastDividend + ";" + fixedDividend + ";" + parValue + ";" + tickerMeanPrice + ";" + operation;
            inputArray.add(output);
        }

        Utils.writeCSV(inputFile, inputArray);
    }
}
