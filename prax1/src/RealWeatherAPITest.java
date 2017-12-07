import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class RealWeatherAPITest {

    private WeatherAPI weatherAPI;

    public static String city;

    @BeforeClass
    public static void setUpClass() throws IOException {
        //executed only once, before the first test
        city = new MyReader().readAndDelete("input.txt");
        System.out.println(city);
    }

    @Before
    public void setUp() throws IOException, JSONException {
        this.weatherAPI = new WeatherAPI();
        this.weatherAPI.setJsonObject(city);
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
    public void testDifferentPlacesTemp() throws IOException, JSONException {
        // new ID 3875024!
        assertFalse(this.weatherAPI.getTempRightNow()
                .equals(this.weatherAPI.getTempFromAnotherPlace()));
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
    public void test3daysMeasurementAmountPlus1() throws JSONException {
        this.weatherAPI.orderJsonObjectByDates();
        // Check if we have enough measurements and predictions.
        assertTrue(this.weatherAPI.getTodayPlus1Day().length() == 8);
    }

    @Test
    public void test3daysMeasurementAmountPlus2() throws JSONException {
        this.weatherAPI.orderJsonObjectByDates();
        // Check if we have enough measurements and predictions.
        assertTrue(this.weatherAPI.getTodayPlus2Days().length() == 8);
    }

    @Test
    public void test3daysMeasurementAmountPlus3() throws JSONException {
        this.weatherAPI.orderJsonObjectByDates();
        // Check if we have enough measurements and predictions.
        assertTrue(this.weatherAPI.getTodayPlus3Days().length() == 8);
    }

    @Test
    public void testCoordinatesLat() throws IOException, JSONException {
        assertEquals(60, Double.parseDouble(this.weatherAPI.getLat()), 200);
    }

    @Test
    public void testCoordinatesLon() throws IOException, JSONException {
        assertEquals(25, Double.parseDouble(this.weatherAPI.getLon()), 200);
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

    @Test
    public void testCityDataFromConsoleCorrect() throws IOException, JSONException {
        System.setIn(new ByteArrayInputStream("london".getBytes()));
        this.weatherAPI.askForCity();
        assertEquals(this.weatherAPI.getCityName().toLowerCase(), "london");
    }

    @Test
    public void testCityDataFromConsoleFalse() throws IOException, JSONException {
        System.setIn(new ByteArrayInputStream("london".getBytes()));
        this.weatherAPI.askForCity();
        assertFalse(this.weatherAPI.getCityName().toLowerCase().equals("tallinn"));
    }

    @Test
    public void testFileWriteAndRead() throws IOException, JSONException {
        this.weatherAPI.getMyPrinter().justCreate("buur.txt", "Berlin");
        this.weatherAPI.readAndWriteAPIToFile("buur.txt");
        JSONObject outputJsonObject = this.weatherAPI.readJsonObjectFromFile("output.txt");
        assertEquals(outputJsonObject.getJSONObject("city").get("name").toString(), "Berlin");
    }

    @Test
    public void testPrintAndGetString() throws FileNotFoundException, UnsupportedEncodingException {
        this.weatherAPI.getMyPrinter().justCreate("uus", "uus");
        assertTrue(this.weatherAPI.getMyPrinter().printedString.equals("uus"));
    }

    @Test
    public void testCreatedFile() throws FileNotFoundException, UnsupportedEncodingException, JSONException {
        this.weatherAPI.getMyPrinter().printString(city, this.weatherAPI);
        File f = new File(city);
        assertTrue(f.exists());
    }
}

