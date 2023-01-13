import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class XML_parser{

    private static final XML_parser instance = new XML_parser();
    private String version = "1.0";
    private String parserType = "XML";
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
    public static XML_parser getInstance() {
        return instance;
    }

    //private constructor
    private XML_parser() {
        port = new Port();
    }


    public Gin[] parseXMLFile(String path) {

        ObjectMapper xmlMapper = new XmlMapper();

        File file = new File(path);

        try {
            Gin[] gin = xmlMapper.readValue(file, Gin[].class);

/*            System.out.println("gin brand = " + gin[0].getManufacturer());
            System.out.println("gin price = " + gin[0].getPrice());*/
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
        public Gin[] parseFile(String path) throws IOException {
            return parseXMLFile(path);
        }
    }

}
