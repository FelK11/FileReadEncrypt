import java.util.Comparator;

public class QuickSorter {

    private static final QuickSorter instance = new QuickSorter();
    private String version = "1.0";
    private String sorterType = "Quick";
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
    public static QuickSorter getInstance() {
        return instance;
    }

    //private constructor
    private QuickSorter() {
        port = new Port();
    }

    /**
     * Sort the first size entries in a.
     */
    public Object[] sort(Object[] a, int size, Comparator c) {
        quicksort(a, 0, size - 1, c);
        return a;
    }


    private void swap(Object[] a, int left, int right) {
        Object t = a[left];
        a[left] = a[right];
        a[right] = t;
    }

    /**
     * Sort the entries in a between left and right inclusive.
     */
    public void quicksort(Object[] a, int left, int right, Comparator c) {
        int size = right - left + 1;
        switch (size) {
            case 0:
            case 1:
                break;
            case 2:
                if (c.compare(a[left], a[right]) > 0) swap(a, left, right);
                break;
            case 3:
                if (c.compare(a[left], a[right - 1]) > 0) swap(a, left, right - 1);
                if (c.compare(a[left], a[right]) > 0) swap(a, left, right);
                if (c.compare(a[left + 1], a[right]) > 0) swap(a, left + 1, right);
                break;
            default:
                int median = median(a, left, right, c);
                int partition = partition(a, left, right, median, c);
                quicksort(a, left, partition - 1, c);
                quicksort(a, partition + 1, right, c);
        }
    }

    private int median(Object[] a, int left, int right, Comparator c) {
        int center = (left + right) / 2;
        if (c.compare(a[left], a[center]) > 0) swap(a, left, center);
        if (c.compare(a[left], a[right]) > 0) swap(a, left, right);
        if (c.compare(a[center], a[right]) > 0) swap(a, center, right);
        swap(a, center, right - 1);
        return right - 1;
    }

    private int partition(Object[] a, int left, int right,
                                 int pivotIndex, Comparator c) {
        int leftIndex = left;
        int rightIndex = right - 1;
        while (true) {
            while (c.compare(a[++leftIndex], a[pivotIndex]) < 0);
            while (c.compare(a[--rightIndex], a[pivotIndex]) > 0);
            if (leftIndex >= rightIndex) {
                break; // pointers cross so partition done
            } else {
                swap(a, leftIndex, rightIndex);
            }
        }
        swap(a, leftIndex, right - 1);         // restore pivot
        return leftIndex;                 // return pivot location
    }


    public class Port{

        public String version() {
            return getVersion();
        }

        public String type() {
            return getSorterType();
        }

        public Object[] sortFile(Object[] arrayToSort, Comparator comparator) {

            return sort(arrayToSort, arrayToSort.length, comparator);

        }
    }


}
