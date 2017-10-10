import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class RealWeatherAPITest {

    private WeatherAPI weatherAPI;

    @Before
    public void setUp() throws IOException {
        this.weatherAPI = new WeatherAPI();
        this.weatherAPI.setJsonObject("Tallinn");
    }

    @Test
    public void testTempRightNow() {
        String tempRightNow = this.weatherAPI.getTempRightNow();
        assertEquals(10, Double.parseDouble(tempRightNow), 10);
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
    public void testDifferentPlacesTemp() throws IOException {
        // new ID 3875024!
        assertFalse(this.weatherAPI.getTempRightNow()
                .equals(this.weatherAPI.getTempFromAnotherPlace()));
    }

    @Test
    public void testDayMinSmallerThanMax() {
        assertTrue(Double.parseDouble(this.weatherAPI.getMinTempToday()) <
                Double.parseDouble(this.weatherAPI.getMaxTempToday()));
    }

    @Test
    public void test3daysMeasurementAmount() {
        this.weatherAPI.orderJsonObjectByDates();
        // Check if we have enough measurements and predictions.
        assertTrue(this.weatherAPI.getToday().length() <= 8 && this.weatherAPI.getToday().length() > 0);
        assertTrue(this.weatherAPI.getTodayPlus1Day().length() == 8);
        assertTrue(this.weatherAPI.getTodayPlus2Days().length() == 8);
        assertTrue(this.weatherAPI.getTodayPlus3Days().length() == 8);
    }

    @Test
    public void testCoordinatesLat() throws IOException {
        assertEquals(Double.parseDouble(this.weatherAPI.getLat()), 60, 5);
    }

    @Test
    public void testCoordinatesLon() throws IOException {
        assertEquals(Double.parseDouble(this.weatherAPI.getLon()), 25, 5);
    }

    @Test
    public void testLowestTempToday() {
        assertEquals(10, this.weatherAPI.lowestTemp(LocalDate.now()), 10);
    }

    @Test
    public void testLowestTempIn1Day() {
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1);
        assertEquals(10, this.weatherAPI.lowestTemp(tomorrow), 10);
    }

    @Test
    public void testLowestTempIn2Days() {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 2);
        assertEquals(10, this.weatherAPI.lowestTemp(nowPlus2), 10);
    }

    @Test
    public void testLowestTempIn3Days() {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus3 = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 3);
        assertEquals(10, this.weatherAPI.lowestTemp(nowPlus3), 10);
    }

    @Test
    public void testHighestTempToday() {
        assertEquals(10, this.weatherAPI.highestTemp(LocalDate.now()), 10);
    }

    @Test
    public void testHighestTempIn1Day() {
        LocalDate now = LocalDate.now();
        LocalDate tomorrow = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1);
        assertEquals(10, this.weatherAPI.highestTemp(tomorrow), 10);
    }

    @Test
    public void testHighestTempIn2Days() {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 2);
        assertEquals(10, this.weatherAPI.highestTemp(nowPlus2), 10);
    }

    @Test
    public void testHighestTempIn3Days() {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus3 = LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 3);
        assertEquals(10, this.weatherAPI.highestTemp(nowPlus3), 10);
    }

    @Test
    public void testCityDataFromConsoleCorrect() throws IOException {
        System.setIn(new ByteArrayInputStream("london".getBytes()));
        this.weatherAPI.askForCity();
        assertEquals(this.weatherAPI.getCityName().toLowerCase(), "london");
    }

    @Test
    public void testCityDataFromConsoleFalse() throws IOException {
        System.setIn(new ByteArrayInputStream("london".getBytes()));
        this.weatherAPI.askForCity();
        assertFalse(this.weatherAPI.getCityName().toLowerCase().equals("tallinn"));
    }

    @Test
    public void testFileWriteAndRead() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Berlin");
        this.weatherAPI.createFile("input.txt", stringBuilder);
        this.weatherAPI.readAndWriteAPIToFile();
        JSONObject outputJsonObject = this.weatherAPI.readJsonObjectFromFile("output.txt");
        assertEquals(outputJsonObject.getJSONObject("city").get("name").toString(), "Berlin");
    }
}

