import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;

public class WeatherAPI {

    private JSONObject jsonObject;

    public JSONArray jsonArray() {
        return this.jsonObject.getJSONArray("list");
    }

    public JSONObject getJsonObject(){
        return jsonObject;
    }

    private JSONArray today = new JSONArray();

    private JSONArray todayPlus1Day = new JSONArray();

    private JSONArray todayPlus2Days = new JSONArray();

    private JSONArray todayPlus3Days = new JSONArray();

    public JSONArray getToday() {
        return this.today;
    }

    public JSONArray getTodayPlus1Day() {
        return this.todayPlus1Day;
    }

    public JSONArray getTodayPlus2Days() {
        return this.todayPlus2Days;
    }

    public JSONArray getTodayPlus3Days() {
        return this.todayPlus3Days;
    }

    public Double lowestTemp(LocalDate localDate) {
        Double lowestTemp = new Double("50");
        for (int i = 0; i < jsonArray().length(); i++) {
            JSONObject object = jsonArray().getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(localDate.toString())) {
                if (Double.parseDouble(object.getJSONObject("main").get("temp_min").toString()) < lowestTemp) {
                    lowestTemp = Double.parseDouble(object.getJSONObject("main").get("temp_min").toString());
                }
            }
        }
        return lowestTemp;
    }

    public Double highestTemp(LocalDate localDate) {
        Double highestTemp = new Double("0");
        for (int i = 0; i < jsonArray().length(); i++) {
            JSONObject object = jsonArray().getJSONObject(i);
            if (object.get("dt_txt").toString().substring(0, 10).equals(localDate.toString())) {
                if (Double.parseDouble(object.getJSONObject("main").get("temp_max").toString()) > highestTemp) {
                    highestTemp = Double.parseDouble(object.getJSONObject("main").get("temp_max").toString());
                }
            }
        }
        return highestTemp;
    }


    public String getLat() {
        return getJsonObject().getJSONObject("city").getJSONObject("coord").get("lat").toString();
    }

    public String getLon() {
        return getJsonObject().getJSONObject("city").getJSONObject("coord").get("lon").toString();
    }

    public String getTempRightNow() {
        return jsonArray().getJSONObject(0).getJSONObject("main").get("temp").toString();
    }

    public String getMinTempToday() {
        return jsonArray().getJSONObject(0).getJSONObject("main").get("temp_min").toString();
    }

    public String getMaxTempToday() {
        return jsonArray().getJSONObject(0).getJSONObject("main").get("temp_max").toString();
    }

    public String getTempFromAnotherPlace() throws IOException {
        // new ID 3875024!
        URL weather1 = new URL("http://api.openweathermap.org/data/2.5/forecast?id=3875024&APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection1 = weather1.openConnection();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
        JSONObject jsonObject1 = new JSONObject(bufferedReader1.readLine());
        JSONArray jsonList1 = jsonObject1.getJSONArray("list");
        return jsonList1.getJSONObject(0).getJSONObject("main").get("temp").toString();
    }

    public void setJsonObject(String city) throws IOException {
        URL weather = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection = weather.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        this.jsonObject = new JSONObject(bufferedReader.readLine());
    }

    public void orderJsonObjectByDates(){
        LocalDate now0 = LocalDate.now();
        //Dates
        LocalDate now1 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 1);
        LocalDate now2 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 2);
        LocalDate now3 = LocalDate.of(now0.getYear(), now0.getMonth(), now0.getDayOfMonth() + 3);
        //Order the list to check if we have enough objects.
        for (int i = 0; i < jsonArray().length(); i++) {
            JSONObject object = jsonArray().getJSONObject(i);
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
    }


}
