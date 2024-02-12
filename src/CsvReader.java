import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CsvReader {
    private ArrayList<String> records = new ArrayList<>();

    public void readCSV(String pathCSV) {
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(pathCSV))) {
               String line;
               while ((line = br.readLine()) != null) {
                   String[] values = line.split(",");
                   records.add(values[0]);
               }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getRecords() {
        return records;
    }
}