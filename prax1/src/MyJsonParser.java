import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MyJsonParser {

    public JSONObject getJsonFromInternet(String city) throws IOException, JSONException {
        URL weather = new URL("http://api.openweathermap.org/data/2.5/forecast?q=" + city + "&APPID=33aed9c64a61381f4c6254f604da5f53&units=metric");
        URLConnection urlConnection = weather.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        return new JSONObject(bufferedReader.readLine());
    }

    public JSONObject getJsonFromFile(String filename) throws IOException, JSONException {
        MyReader reader = new MyReader();
        return new JSONObject(reader.readLine(filename));
    }
}
