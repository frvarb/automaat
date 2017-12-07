import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class MyPrinter {

    public String printedString;

    public void printString(String filename, WeatherAPI weatherAPI) throws FileNotFoundException, UnsupportedEncodingException, JSONException {
        if(weatherAPI.getTodayPlus1Day().length() == 0) {
            weatherAPI.orderJsonObjectByDates();
        }
        PrintWriter printWriter = new PrintWriter(filename, "UTF-8");
        String toWrite = "City name: " + weatherAPI.getCityName() + "\n"
                + "City coordinates: " + weatherAPI.getLon() + " : " + weatherAPI.getLat() + "\n"
                + "Date : " + weatherAPI.getDateToday() + "\n"
                + " Max temp: " + weatherAPI.getMaxTempToday() + "\n"
                + " Min temp: " + weatherAPI.getMinTempToday() + "\n"
                + "Date : " + weatherAPI.getDateTodayPlus1() + "\n"
                + " Max temp: " + weatherAPI.getMaxTempTodayPlus1() + "\n"
                + " Min temp: " + weatherAPI.getMinTempTodayPlus1() + "\n"
                + "Date : " + weatherAPI.getDateTodayPlus2() + "\n"
                + " Max temp: " + weatherAPI.getMaxTempTodayPlus2() + "\n"
                + " Min temp: " + weatherAPI.getMinTempTodayPlus2() + "\n"
                + "Date : " + weatherAPI.getDateTodayPlus3() + "\n"
                + " Max temp: " + weatherAPI.getMaxTempTodayPlus3() + "\n"
                + " Min temp: " + weatherAPI.getMinTempTodayPlus3() + "\n"
                + "Temp right now: " +weatherAPI.getTempRightNow();
        printWriter.append(toWrite);
        this.printedString = toWrite;
        printWriter.close();
    }

    public void justCreate(String filename, String message) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter printWriter = new PrintWriter(filename, "UTF-8");
        printWriter.append(message);
        this.printedString = message;
        printWriter.close();
    }
}
