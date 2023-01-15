import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JSON_parser {

    private static final JSON_parser instance = new JSON_parser();
    private String version = "1.0";
    private String parserType = "JSON";
    //port
    public Port port;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getParserType() {
        return parserType;
    }

    public void setParserType(String parserType) {
        this.parserType = parserType;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    // static method getInstance
    public static JSON_parser getInstance() {
        return instance;
    }

    //private constructor
    private JSON_parser() {
        port = new Port();
    }

    public Object[] parseJSONFile(String path) {

        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File(path);

        try {
            Object[] gin = objectMapper.readValue(file, Object[].class);
            return gin;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public class Port implements IFileParser {

        public String version() {
            return getVersion();
        }

        public String type() {
            return getParserType();
        }

        @Override
        public Object[] parseFile(String path) throws IOException {
            return parseJSONFile(path);
        }
    }

}
