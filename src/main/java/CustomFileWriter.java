import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CustomFileWriter {

    public ObjectMapper getJsonMapper() {
        return jsonMapper;
    }

    public ObjectMapper getXmlMapper() {
        return xmlMapper;
    }

    public CsvMapper getCsvMapper() {
        return csvMapper;
    }

    private final ObjectMapper jsonMapper;
    private final ObjectMapper xmlMapper;
    private final CsvMapper csvMapper;

    public CustomFileWriter() {
        jsonMapper = new ObjectMapper();
        xmlMapper = new XmlMapper();
        csvMapper = new CsvMapper();
    }

    private File writeDataToJson(Object[] data, FileType fileType, String fileName) {
        File tempFile = new File(Configuration.INSTANCE.pathToGinData + fileName + fileType.toString().toLowerCase());
        try {
            getJsonMapper().writerWithDefaultPrettyPrinter().writeValue(tempFile, data);
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File writeDataToCsv(Object[] data, FileType fileType, String fileName) {

        File tempFile = new File(Configuration.INSTANCE.pathToGinData + fileName + fileType.toString().toLowerCase());
        CsvSchema csvSchema = CsvSchema.emptySchema().withColumnSeparator(';').withHeader();
        CsvSchema.Builder csvBuilder = new CsvSchema.Builder(csvSchema);
        ArrayList<String> columnNameList = new ArrayList<>();

        for (Object columnName : ((LinkedHashMap) data[0]).keySet().toArray()) {
            columnNameList.add((String) columnName);
        }

        for (int i = 0; i < ((LinkedHashMap) data[0]).keySet().toArray().length; i++) {
            csvBuilder.addColumn((String) ((LinkedHashMap) data[0]).keySet().toArray()[i]);
        }

        csvSchema = csvBuilder.build();

        try {
            getCsvMapper().writer(csvSchema).writeValue(tempFile, data);
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File writeDataToXml(Object[] data, FileType fileType, String fileName) {
        File tempFile = new File(Configuration.INSTANCE.pathToGinData + fileName + fileType.toString().toLowerCase());
        try {
            getXmlMapper().writerWithDefaultPrettyPrinter().writeValue(tempFile, data);
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File writeFile(Object[] data, FileType fileType, String fileName) {
        if (fileType.equals(FileType.JSON)) {
            return writeDataToJson(data, fileType, fileName);
        } else if (fileType.equals(FileType.CSV)) {
            return writeDataToCsv(data, fileType, fileName);
        } else if (fileType.equals(FileType.XML)) {
            return writeDataToXml(data, fileType, fileName);
        } else {
            System.out.println("Error unsupported filetype");
            System.exit(-19);
        }
        return null;
    }

}
