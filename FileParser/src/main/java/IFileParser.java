import java.io.IOException;

public interface IFileParser {

    Gin[] parseFile(String path) throws IOException;

}
