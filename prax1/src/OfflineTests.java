import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class OfflineTests {

    private WeatherAPI weatherAPI;

    private String city;

    @Before
    public void setUp() throws IOException, JSONException {
        this.weatherAPI = new WeatherAPI();
        this.weatherAPI.setOfflineJson("output.txt");
        this.city = "Berlin";
    }

    @Test
    public void testTempRightNow() throws JSONException {
        System.out.println(this.weatherAPI.getCityName());
        String tempRightNow = this.weatherAPI.getTempRightNow();
        assertEquals(0, Double.parseDouble(tempRightNow), 50);
    }

    @Test(expected = IOException.class)
    public void testCityIdNone() throws IOException {
        // No ID to the city in url.
        URL weather = new URL("http://api.openweathermap.org/data/2.5/forecast?id=&" +
                "APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection = weather.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    }

    @Test(expected = IOException.class)
    public void testCityIdWrong() throws IOException {
        // Wrong ID to the city in url.
        URL weather = new URL("http://api.openweathermap.org/data/2.5/forecast?id=asdewrrgrg&" +
                "APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection = weather.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    }

    @Test
    public void testDayMinSmallerThanMax() throws JSONException {
        assertTrue(Double.parseDouble(this.weatherAPI.getMinTempToday()) <
                Double.parseDouble(this.weatherAPI.getMaxTempToday()));
    }

    @Test
    public void test3daysMeasurementAmountToday() throws JSONException {
        this.weatherAPI.orderJsonObjectByDates();
        // Check if we have enough measurements and predictions.
        assertTrue(this.weatherAPI.getToday().length() <= 8 && this.weatherAPI.getToday().length() >= 0);
    }

    @Test
    public void testCoordinatesLat() throws IOException, JSONException {
        assertEquals(Double.parseDouble(this.weatherAPI.getLat()), 60, 100);
    }

    @Test
    public void testCoordinatesLon() throws IOException, JSONException {
        assertEquals(Double.parseDouble(this.weatherAPI.getLon()), 25, 100);
    }

    @Test
    public void testLowestTempToday() throws JSONException {
        assertEquals(0, this.weatherAPI.lowestTemp(LocalDate.now()), 50);
    }

    @Test
    public void testLowestTempIn1Day() throws JSONException {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        assertEquals(0, this.weatherAPI.lowestTemp(tomorrow), 50);
    }

    @Test
    public void testLowestTempIn2Days() throws JSONException {
        LocalDate nowPlus2 = LocalDate.now().plusDays(2);
        assertEquals(0, this.weatherAPI.lowestTemp(nowPlus2), 50);
    }

    @Test
    public void testLowestTempIn3Days() throws JSONException {
        LocalDate nowPlus3 = LocalDate.now().plusDays(3);
        assertEquals(0, this.weatherAPI.lowestTemp(nowPlus3), 50);
    }

    @Test
    public void testHighestTempToday() throws JSONException {
        assertEquals(0, this.weatherAPI.highestTemp(LocalDate.now()), 50);
    }

    @Test
    public void testHighestTempIn1Day() throws JSONException {
        LocalDate nowPlus1 = LocalDate.now().plusDays(1);
        assertEquals(0, this.weatherAPI.highestTemp(nowPlus1), 50);
    }

    @Test
    public void testHighestTempIn2Days() throws JSONException {
        LocalDate nowPlus2 = LocalDate.now().plusDays(2);
        assertEquals(0, this.weatherAPI.highestTemp(nowPlus2), 50);
    }

    @Test
    public void testHighestTempIn3Days() throws JSONException, ParseException {
        LocalDate nowPlus3 = LocalDate.now().plusDays(3);
        assertEquals(0, this.weatherAPI.highestTemp(nowPlus3), 50);
    }


    //
    @Test
    public void testFileWriteAndRead() throws IOException, JSONException {
        this.weatherAPI.getMyPrinter().justCreate("input.txt", "Berlin");
        this.weatherAPI.setOfflineJson("output.txt");
        JSONObject outputJsonObject = this.weatherAPI.readJsonObjectFromFile("output.txt");
        assertEquals(outputJsonObject.getJSONObject("city").get("name").toString(), "Berlin");
    }

    @Test
    public void testInputFileRead() throws IOException {
        MyReader myReader = new MyReader();
        assertTrue(myReader.readLine("input.txt").equals("Berlin"));
    }

    @Test
    public void printAndGetString() throws FileNotFoundException, UnsupportedEncodingException {
        this.weatherAPI.getMyPrinter().justCreate("uus", "uus");
        assertTrue(this.weatherAPI.getMyPrinter().printedString.equals("uus"));
    }
}
