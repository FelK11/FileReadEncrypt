import java.util.Arrays;
import java.util.Comparator;

public class LambdaSorter {

    private static final LambdaSorter instance = new LambdaSorter();
    private String version = "1.0";
    private String sorterType = "Lambda";
    //port
    public Port port;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSorterType() {
        return sorterType;
    }

    public void setSorterType(String sorterType) {
        this.sorterType = sorterType;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    // static method getInstance
    public static LambdaSorter getInstance() {
        return instance;
    }

    //private constructor
    private LambdaSorter() {
        port = new Port();
    }


    public <T> T[] sort(T []a, Comparator<? super T> comparator){
        Arrays.sort(a, comparator);
        return a;
    }


    public class Port{

        public String version() {
            return getVersion();
        }

        public String type() {
            return getSorterType();
        }

        public Object[] sortFile(Object[] arrayToSort, Comparator comparator) {

            return sort(arrayToSort, comparator);

        }
    }

}
