import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CSV_parser {

    private static final CSV_parser instance = new CSV_parser();
    private String version = "1.0";
    private String parserType = "CSV";


    //port
    public Port port;

    //private constructor
    private CSV_parser() {
        port = new Port();
    }

    // static method getInstance
    public static CSV_parser getInstance() {
        return instance;
    }


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

    private Object[] parseCSVFile(String path) throws IOException {

        CsvMapper csvMapper = new CsvMapper();

        File file = new File(path);

        CsvSchema csvSchema = csvMapper
                .typedSchemaFor(Object.class)
                .withHeader()
                .withColumnSeparator(';')
                .withComments();


        MappingIterator ginIter = csvMapper
                .readerWithTypedSchemaFor(Object.class)
                .with(csvSchema)
                .readValues(file);


        List<Object> ginList = ginIter.readAll();
        Object[] gins = ginList.toArray(new Object[ginList.size()]);


        return gins;
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
            return parseCSVFile(path);
        }
    }

}
