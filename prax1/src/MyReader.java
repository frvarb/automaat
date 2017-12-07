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
        for (int linenumber = 0; linenumber < lines.size(); linenumber++) {
            writer.write(lines.get(linenumber) + "\n");
        }
        writer.close();
        return firstLine;
    }


}
