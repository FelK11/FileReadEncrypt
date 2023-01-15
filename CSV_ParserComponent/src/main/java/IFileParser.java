import java.io.IOException;

public interface IFileParser {

    Object[] parseFile(String path) throws IOException;

}
