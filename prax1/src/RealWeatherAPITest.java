import org.json.*;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RealWeatherAPITest {

    private JSONArray jsonList;
    private LocalDate now0 = LocalDate.now();

    @Before
    public void setUp() throws IOException {
        URL weather = new URL("http://api.openweathermap.org/data/2.5/forecast?id=588409&APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection = weather.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        JSONObject jsonObject = new JSONObject(bufferedReader.readLine());
        jsonList = jsonObject.getJSONArray("list");
    }

    @Test
    public void testTempRightNow() {
        String tempRightNow = jsonList.getJSONObject(0).getJSONObject("main").get("temp").toString();
        System.out.println(tempRightNow);
        assertEquals(10, Double.parseDouble(tempRightNow), 10);
    }

    @Test(expected = IOException.class)
    public void testCityIdNone() throws IOException {
        // No ID to the city in url.
        URL weather = new URL("http://api.openweathermap.org/data/2.5/forecast?id=&APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection = weather.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    }

    @Test(expected = IOException.class)
    public void testCityIdWrong() throws IOException {
        // Wrong ID to the city in url.
        URL weather = new URL("http://api.openweathermap.org/data/2.5/forecast?id=asdewrrgrg&APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection = weather.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
    }

    @Test
    public void testDifferentPlacesTemp() throws IOException {
        // new ID 3875024!
        URL weather1 = new URL("http://api.openweathermap.org/data/2.5/forecast?id=3875024&APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection1 = weather1.openConnection();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
        JSONObject jsonObject1 = new JSONObject(bufferedReader1.readLine());
        JSONArray jsonList1 = jsonObject1.getJSONArray("list");
        assertFalse(jsonList.getJSONObject(0).getJSONObject("main").get("temp").toString()
                .equals(jsonList1.getJSONObject(0).getJSONObject("main").get("temp").toString()));
    }

    @Test
    public void testDayMinSmallerThanMax() {
        assertTrue(Double.parseDouble(jsonList.getJSONObject(0).getJSONObject("main").get("temp_min").
                toString()) < Double.parseDouble(jsonList.getJSONObject(0).getJSONObject("main").get("temp_max").
                toString()));
    }

    @Test
    public void test3daysMeasurementAmount() {
        //Dates
        LocalDate now1 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 1);
        LocalDate now2 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 2);
        LocalDate now3 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 3);
        //Arrays for all the predictions.
        JSONArray today = new JSONArray();
        JSONArray todayPlus1Day = new JSONArray();
        JSONArray todayPlus2Days = new JSONArray();
        JSONArray todayPlus3Days = new JSONArray();
        //Order the list to check if we have enough objects.
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject object = jsonList.getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(now0.toString())) {
                today.put(object);
            } else if (object.get("dt_txt").toString().substring(0, 10).equals(now1.toString())) {
                todayPlus1Day.put(object);
            } else if (object.get("dt_txt").toString().substring(0, 10).equals(now2.toString())) {
                todayPlus2Days.put(object);
            } else if (object.get("dt_txt").toString().substring(0, 10).equals(now3.toString())) {
                todayPlus3Days.put(object);
            }
        }
        // Check if we have enough measurements and predictions.
        assertTrue(today.length() <= 8 && today.length() > 0);
        assertTrue(todayPlus1Day.length() == 8);
        assertTrue(todayPlus2Days.length() == 8);
        assertTrue(todayPlus3Days.length() == 8);
    }

    @Test
    public void testCoordinatesLat() throws IOException {
        URL weather1 = new URL("http://api.openweathermap.org/data/2.5/forecast?id=588409&APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection1 = weather1.openConnection();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
        JSONObject jsonObject1 = new JSONObject(bufferedReader1.readLine());
        assertTrue(jsonObject1.getJSONObject("city").getJSONObject("coord").get("lat").toString().equals("59.437"));
    }

    @Test
    public void testCoordinatesLon() throws IOException {
        URL weather1 = new URL("http://api.openweathermap.org/data/2.5/forecast?id=588409&APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection1 = weather1.openConnection();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
        JSONObject jsonObject1 = new JSONObject(bufferedReader1.readLine());
        assertTrue(jsonObject1.getJSONObject("city").getJSONObject("coord").get("lon").toString().equals("24.7535"));
    }

    @Test
    public void testLowestTempToday() {
        Double lowestTemp = new Double("50");
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject object = jsonList.getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(now0.toString())) {
                if (Double.parseDouble(object.getJSONObject("main").get("temp_min").toString()) < lowestTemp) {
                    lowestTemp = Double.parseDouble(object.getJSONObject("main").get("temp_min").toString());
                }
            }
        }
        System.out.println(lowestTemp);
        assertEquals(10, lowestTemp, 10);
    }

    @Test
    public void testLowestTempIn1Day() {
        Double lowestTemp = new Double("50");
        LocalDate now1 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 1);
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject object = jsonList.getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(now1.toString())) {
                if (Double.parseDouble(object.getJSONObject("main").get("temp_min").toString()) < lowestTemp) {
                    lowestTemp = Double.parseDouble(object.getJSONObject("main").get("temp_min").toString());
                }
            }
        }
        System.out.println(lowestTemp);
        assertEquals(10, lowestTemp, 10);
    }

    @Test
    public void testLowestTempIn2Days() {
        Double lowestTemp = new Double("50");
        LocalDate now2 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 2);
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject object = jsonList.getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(now2.toString())) {
                if (Double.parseDouble(object.getJSONObject("main").get("temp_min").toString()) < lowestTemp) {
                    lowestTemp = Double.parseDouble(object.getJSONObject("main").get("temp_min").toString());
                }
            }
        }
        System.out.println(lowestTemp);
        assertEquals(10, lowestTemp, 10);
    }

    @Test
    public void testLowestTempIn3Days() {
        Double lowestTemp = new Double("50");
        LocalDate now3 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 3);
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject object = jsonList.getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(now3.toString())) {
                if (Double.parseDouble(object.getJSONObject("main").get("temp_min").toString()) < lowestTemp) {
                    lowestTemp = Double.parseDouble(object.getJSONObject("main").get("temp_min").toString());
                }
            }
        }
        System.out.println(lowestTemp);
        assertEquals(10, lowestTemp, 10);
    }

    @Test
    public void testHighestTempToday() {
        Double highestTemp = new Double("0");
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject object = jsonList.getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(now0.toString())) {
                if (Double.parseDouble(object.getJSONObject("main").get("temp_max").toString()) > highestTemp) {
                    highestTemp = Double.parseDouble(object.getJSONObject("main").get("temp_max").toString());
                }
            }
        }
        System.out.println(highestTemp);
        assertEquals(10, highestTemp, 10);
    }

    @Test
    public void testHighestTempIn1Day() {
        Double highestTemp = new Double("0");
        LocalDate now1 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 1);
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject object = jsonList.getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(now1.toString())) {
                if (Double.parseDouble(object.getJSONObject("main").get("temp_max").toString()) > highestTemp) {
                    highestTemp = Double.parseDouble(object.getJSONObject("main").get("temp_max").toString());
                }
            }
        }
        System.out.println(highestTemp);
        assertEquals(10, highestTemp, 10);
    }

    @Test
    public void testHighestTempIn2Days() {
        Double highestTemp = new Double("0");
        LocalDate now2 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 2);
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject object = jsonList.getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(now2.toString())) {
                if (Double.parseDouble(object.getJSONObject("main").get("temp_max").toString()) > highestTemp) {
                    highestTemp = Double.parseDouble(object.getJSONObject("main").get("temp_max").toString());
                }
            }
        }
        System.out.println(highestTemp);
        assertEquals(10, highestTemp, 10);
    }

    @Test
    public void testHighestTempIn3Days() {
        Double highestTemp = new Double("0");
        LocalDate now3 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 3);
        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject object = jsonList.getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(now3.toString())) {
                if (Double.parseDouble(object.getJSONObject("main").get("temp_max").toString()) > highestTemp) {
                    highestTemp = Double.parseDouble(object.getJSONObject("main").get("temp_max").toString());
                }
            }
        }
        System.out.println(highestTemp);
        assertEquals(10, highestTemp, 10);
    }
}

