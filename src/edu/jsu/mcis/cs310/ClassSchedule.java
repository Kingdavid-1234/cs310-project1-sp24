package edu.jsu.mcis.cs310;



import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.System.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.lang.ClassLoader;




public class ClassSchedule {
    
    private final String CSV_FILENAME = "jsu_sp24_v1.csv";
    private final String JSON_FILENAME = "jsu_sp24_v1.json";
    
    private final String CRN_COL_HEADER = "crn";
    private final String SUBJECT_COL_HEADER = "subject";
    private final String NUM_COL_HEADER = "num";
    private final String DESCRIPTION_COL_HEADER = "description";
    private final String SECTION_COL_HEADER = "section";
    private final String TYPE_COL_HEADER = "type";
    private final String CREDITS_COL_HEADER = "credits";
    private final String START_COL_HEADER = "start";
    private final String END_COL_HEADER = "end";
    private final String DAYS_COL_HEADER = "days";
    private final String WHERE_COL_HEADER = "where";
    private final String SCHEDULE_COL_HEADER = "schedule";
    private final String INSTRUCTOR_COL_HEADER = "instructor";
    private final String SUBJECTID_COL_HEADER = "subjectid";
    
   public String convertJsonToCsvString(JsonObject json) {
    if (json == null) {
        return "";
    }

    StringWriter writer = new StringWriter();
    try (CSVWriter csvWriter = new CSVWriter(writer, '\t', '"', '\\', "\n")) {
        JsonArray jsonArray = (JsonArray) json.get("data");  // Assumes "data" is the JSON array key

        if (jsonArray != null && !jsonArray.isEmpty()) {
            List<String[]> data = new ArrayList<>();

            // Assume first item defines headers
            JsonObject firstObject = (JsonObject) jsonArray.get(0);
            Set<String> keys = firstObject.keySet();
            String[] header = keys.toArray(new String[0]);
            data.add(header);

            // Process each JSON object into a CSV row
            for (Object obj : jsonArray) {
                JsonObject rowObject = (JsonObject) obj;
                String[] row = new String[header.length];
                for (int i = 0; i < header.length; i++) {
                    row[i] = rowObject.get(header[i]).toString();
                }
                data.add(row);
            }

            csvWriter.writeAll(data);
        }
    } catch (IOException ex) {
        ex.printStackTrace();  // Consider using logging
    }
    return writer.toString();
}


    
    public String convertCsvToJsonString(List<String[]> csv) {
    // Return an empty JSON array string if input CSV data is null or empty
    if (csv == null || csv.isEmpty()) {
        return "[]";
    }

    JsonArray jsonArray = new JsonArray(); // Prepare a JSON array to hold all JSON objects
    String[] header = csv.get(0); // Assume the first row in the CSV contains header information

    // Iterate over each row of the CSV data, skipping the header row
    for (int i = 1; i < csv.size(); i++) {
        JsonObject jsonObject = new JsonObject(); // Create a new JSON object for each row
        String[] row = csv.get(i); // Get current row data

        // Fill the JSON object with key-value pairs where the keys are headers
        for (int j = 0; j < header.length; j++) {
            jsonObject.put(header[j], row[j]);
        }
        jsonArray.add(jsonObject); // Add the filled JSON object to the JSON array
    }

    return jsonArray.toJson(); // Convert the JSON array to string format and return
}

    
    public List<String[]> getCsv() {
        
        List<String[]> csv = getCsv(getInputFileData(CSV_FILENAME));
        return csv;
        
    }
    
    public List<String[]> getCsv(String input) {
        
        List<String[]> csv = null;
        
        try {
            
            CSVReader reader = new CSVReaderBuilder(new StringReader(input)).withCSVParser(new CSVParserBuilder().withSeparator('\t').build()).build();
            csv = reader.readAll();
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return csv;
        
    }
    
    public String getCsvString(List<String[]> csv) {
        
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer, '\t', '"', '\\', "\n");
        
        csvWriter.writeAll(csv);
        
        return writer.toString();
        
    }


    
   private String getInputFileData(String filename) {
    StringBuilder buffer = new StringBuilder();
    ClassLoader loader = ClassLoader.getSystemClassLoader();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("resources" + File.separator + filename)))) {
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append('\n');
        }
    } catch (IOException e) {
        return null;  // Return null to indicate that an error occurred
    }
    return buffer.toString();
    }       

    JsonObject getJson() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


   
}