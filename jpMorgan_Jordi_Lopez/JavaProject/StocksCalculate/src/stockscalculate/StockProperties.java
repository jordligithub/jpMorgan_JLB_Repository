/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stockscalculate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author JordiL
 */
public class StockProperties {

    public static String inputPath = "";
    public static String outputPath = "";
    public static String logPath = "";
    public static boolean showMessage = true;
    public static boolean showClassInfo = true;
    public static String logLevel = "1";
    public static long lastMinutesInterval = 0;
    public static String symbols = "";

    public static void readPropsFile(String propFile) {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(propFile);

            prop.load(input);

            symbols = prop.getProperty("symbols");
            inputPath = prop.getProperty("inputFile");
            outputPath = prop.getProperty("outputFile");
            logPath = prop.getProperty("logFile");
            showMessage = Boolean.parseBoolean(prop.getProperty("showLog"));
            showClassInfo =Boolean.parseBoolean(prop.getProperty("showExtendedInfo"));
             
            lastMinutesInterval = Long.parseLong(prop.getProperty("lastMinutesInterval"));
        } catch (IOException ex) {
            System.out.println("Error opening or processing properties Files");
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Error opening or processing properties Files");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error opening or processing properties Files");
            }
        }

    }
}
