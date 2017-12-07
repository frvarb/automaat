import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class MockTests {

    private WeatherAPI weatherAPI;

    private MyReader reader;

    private MyPrinter printer;

    private MyJsonParser myJsonParser;

    private MyJsonParser realParser;

    @Before
    public void setUp() {
        this.weatherAPI = new WeatherAPI();
        this.reader = mock(MyReader.class);
        this.printer = mock(MyPrinter.class);
        this.myJsonParser = mock(MyJsonParser.class);
        this.realParser = new MyJsonParser();
    }

    @Test
    public void mockFileReadInput() throws IOException, JSONException {
        this.weatherAPI.setMyReader(this.reader);
        when(this.reader.readLine("input.txt")).thenReturn("Berlin");
        this.weatherAPI.readFromFile("input.txt");
        verify(this.reader).readLine("input.txt");
    }

    @Test
    public void mockFileReadReturn() throws IOException, JSONException {
        this.weatherAPI.setMyPrinter(this.printer);
        doNothing().when(this.printer).printString("new", this.weatherAPI);
        this.weatherAPI.createFile("new", this.weatherAPI);
        verify(this.printer).printString("new", this.weatherAPI);
    }

    @Test
    public void mockInternetConnection() throws IOException, JSONException {
        this.weatherAPI.setMyJsonParser(this.myJsonParser);
        when(this.myJsonParser.getJsonFromInternet("Berlin")).thenReturn(this.realParser.getJsonFromFile("output.txt"));
        this.weatherAPI.setJsonObject("Berlin");
        verify(this.myJsonParser).getJsonFromInternet("Berlin");
    }

    @Test
    public void mockInternetConnectionCorrectCity() throws IOException, JSONException {
        this.weatherAPI.setMyJsonParser(this.myJsonParser);
        when(this.myJsonParser.getJsonFromInternet("Berlin")).thenReturn(this.realParser.getJsonFromFile("output.txt"));
        this.weatherAPI.setJsonObject("Berlin");
        assertTrue(this.weatherAPI.getCityName().equals("Berlin"));
    }

}
