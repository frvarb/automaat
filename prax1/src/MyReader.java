import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MyReader {

    public String readLine(String filename) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line = bufferedReader.readLine();
        bufferedReader.close();
        return line;
    }

    public String readAndDelete(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        String firstLine = "";
        if (!lines.isEmpty()) {
            firstLine = lines.remove(0);
        }
        bufferedReader.close();
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        writer.print("");
        for (int i = 0; i < lines.size(); i++) {
            writer.write(lines.get(i) + "\n");
        }
        writer.close();
        return firstLine;
    }


}
