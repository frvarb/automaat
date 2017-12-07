import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;

public class WeatherAPI {

    public void setOfflineJson(String filename) throws IOException, JSONException {
        this.jsonObject = myJsonParser.getJsonFromFile(filename);
    }

    private MyJsonParser myJsonParser = new MyJsonParser();

    private MyPrinter myPrinter = new MyPrinter();

    public MyPrinter getMyPrinter() {
        return myPrinter;
    }

    public void setMyPrinter(MyPrinter myPrinter) {
        this.myPrinter = myPrinter;
    }

    private MyReader myReader = new MyReader();

    private JSONObject jsonObject;

    public JSONArray jsonArray() throws JSONException {
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

    public Double lowestTemp(LocalDate localDate) throws JSONException {
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

    public Double highestTemp(LocalDate localDate) throws JSONException {
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
    public String getDateToday() throws JSONException {
        return getToday().getJSONObject(0).get("dt_txt").toString().substring(0, 10);
    }

    public String getDateTodayPlus1() throws JSONException {
        return getTodayPlus1Day().getJSONObject(0).get("dt_txt").toString().substring(0, 10);
    }

    public String getDateTodayPlus2() throws JSONException {
        return getTodayPlus2Days().getJSONObject(0).get("dt_txt").toString().substring(0, 10);
    }

    public String getDateTodayPlus3() throws JSONException {
        return getTodayPlus3Days().getJSONObject(0).get("dt_txt").toString().substring(0, 10);
    }


    public String getLat() throws JSONException {
        return getJsonObject().getJSONObject("city").getJSONObject("coord").get("lat").toString();
    }

    public String getCityName() throws JSONException {
        return getJsonObject().getJSONObject("city").get("name").toString();
    }

    public String getLon() throws JSONException {
        return getJsonObject().getJSONObject("city").getJSONObject("coord").get("lon").toString();
    }

    public String getTempRightNow() throws JSONException {
        return jsonArray().getJSONObject(0).getJSONObject("main").get("temp").toString();
    }

    public String getMinTempToday() throws JSONException {
        return jsonArray().getJSONObject(0).getJSONObject("main").get("temp_min").toString();
    }

    public String getMaxTempToday() throws JSONException {
        return jsonArray().getJSONObject(0).getJSONObject("main").get("temp_max").toString();
    }

    public String getMinTempTodayPlus1() throws JSONException {
        return jsonArray().getJSONObject(1).getJSONObject("main").get("temp_min").toString();
    }

    public String getMaxTempTodayPlus1() throws JSONException {
        return jsonArray().getJSONObject(1).getJSONObject("main").get("temp_max").toString();
    }

    public String getMinTempTodayPlus2() throws JSONException {
        return jsonArray().getJSONObject(2).getJSONObject("main").get("temp_min").toString();
    }

    public String getMaxTempTodayPlus2() throws JSONException {
        return jsonArray().getJSONObject(2).getJSONObject("main").get("temp_max").toString();
    }

    public String getMinTempTodayPlus3() throws JSONException {
        return jsonArray().getJSONObject(3).getJSONObject("main").get("temp_min").toString();
    }

    public String getMaxTempTodayPlus3() throws JSONException {
        return jsonArray().getJSONObject(3).getJSONObject("main").get("temp_max").toString();
    }

    public String getTempFromAnotherPlace() throws IOException, JSONException {
        // new ID 3875024!
        URL weather1 = new URL("http://api.openweathermap.org/data/2.5/forecast?id=3875024&APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection1 = weather1.openConnection();
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
        JSONObject jsonObject1 = new JSONObject(bufferedReader1.readLine());
        JSONArray jsonList1 = jsonObject1.getJSONArray("list");
        return jsonList1.getJSONObject(0).getJSONObject("main").get("temp").toString();
    }

    public void setJsonObject(String city) throws IOException, JSONException {
        this.jsonObject = this.myJsonParser.getJsonFromInternet(city);
    }

    public void orderJsonObjectByDates() throws JSONException {
        LocalDate now0 = LocalDate.now();
        //Dates
        LocalDate now1 = LocalDate.now().plusDays(1);
        LocalDate now2 = LocalDate.now().plusDays(2);
        LocalDate now3 = LocalDate.now().plusDays(3);
        //Order the list to check if we have enough objects.
        for (int json = 0; json < jsonArray().length(); json++) {
            JSONObject object = jsonArray().getJSONObject(json);
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

    public void askForCity() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Enter The City name: ");
            String city = bufferedReader.readLine();
            setJsonObject(city);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            bufferedReader.close();
        }
    }

    public void createFile(String filename, WeatherAPI weatherAPI) throws FileNotFoundException, UnsupportedEncodingException, JSONException {
        this.myPrinter.printString(filename, weatherAPI);
    }

    public void readFromFile(String filename) throws IOException, JSONException {
        String city = this.myReader.readLine(filename);
        this.setJsonObject(city);
    }

    public void readAndWriteAPIToFile(String filename) throws IOException, JSONException {
        readFromFile(filename);
        createFile(this.getCityName() + ".txt", this);
    }

    public JSONObject readJsonObjectFromFile(String filename) throws IOException, JSONException {
        return new JSONObject(this.myReader.readLine(filename));
    }

    public void setMyReader(MyReader myReader) {
        this.myReader = myReader;
    }

    public MyJsonParser getMyJsonParser() {
        return myJsonParser;
    }

    public void setMyJsonParser(MyJsonParser myJsonParser) {
        this.myJsonParser = myJsonParser;
    }
}
