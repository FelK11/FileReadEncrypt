import java.util.Comparator;

public class MergeSorter{

    private static final MergeSorter instance = new MergeSorter();
    private String version = "1.0";
    private String sorterType = "Merge";
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
    public static MergeSorter getInstance() {
        return instance;
    }

    //private constructor
    private MergeSorter() {
        port = new Port();
    }


    public <T> T[] sort(T []a,int order, Comparator<? super T> comparator){
        return mergeSort(a,0, a.length-1, order,comparator);
    }


    public <T> T[] mergeSort(T []a,int lowerIndex,int upperIndex,int order, Comparator<? super T>comparator){
        if(lowerIndex == upperIndex){
            return a;
        }else{
            int mid = (lowerIndex+upperIndex)/2;
            mergeSort(a,lowerIndex,mid,order,comparator);
            mergeSort(a,mid+1, upperIndex,order,comparator);

            if(order == SortOrder.INSTANCE.ASC_ORDER){
                mergeAsc(a,lowerIndex,mid+1,upperIndex,comparator);
            }else if(order == SortOrder.INSTANCE.DESC_ORDER){
                mergeDesc(a,lowerIndex,mid+1,upperIndex,comparator);
            }else{
                throw new UnsupportedOperationException("The order you specified is not supported.");
            }
        }
        return a;
    }

    @SuppressWarnings("unchecked")
    public <T> void mergeAsc(T []a,int lowerIndexCursor,int higerIndex,int upperIndex,Comparator<? super T>comparator){
        Object []tempArray = getTempArray(a.length);
        int tempIndex=0;
        int lowerIndex = lowerIndexCursor;
        int midIndex = higerIndex-1;
        int totalItems = upperIndex-lowerIndex+1;
        while(lowerIndex <= midIndex && higerIndex <= upperIndex){
            if(comparator.compare(a[lowerIndex],a[higerIndex]) < 0){
                tempArray[tempIndex++] = a[lowerIndex++];
            }else{
                tempArray[tempIndex++] = a[higerIndex++];
            }
        }

        while(lowerIndex <= midIndex){
            tempArray[tempIndex++] = a[lowerIndex++];
        }

        while(higerIndex <= upperIndex){
            tempArray[tempIndex++] = a[higerIndex++];
        }

        for(int i=0;i<totalItems;i++){
            a[lowerIndexCursor+i] = (T) tempArray[i];
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void mergeDesc(T []a,int lowerIndexCursor,int higerIndex,int upperIndex,Comparator<? super T>comparator){
        Object []tempArray = getTempArray(a.length);
        int tempIndex=0;
        int lowerIndex = lowerIndexCursor;
        int midIndex = higerIndex-1;
        int totalItems = upperIndex-lowerIndex+1;
        while(lowerIndex <= midIndex && higerIndex <= upperIndex){
            if(comparator.compare(a[lowerIndex],a[higerIndex]) > 0){
                tempArray[tempIndex++] = a[lowerIndex++];
            }else{
                tempArray[tempIndex++] = a[higerIndex++];
            }
        }

        while(lowerIndex <= midIndex){
            tempArray[tempIndex++] = a[lowerIndex++];
        }

        while(higerIndex <= upperIndex){
            tempArray[tempIndex++] = a[higerIndex++];
        }

        for(int i=0;i<totalItems;i++){
            a[lowerIndexCursor+i] = (T) tempArray[i];
        }
    }

    private Object[] getTempArray(int length){
        Object []tempArray = new Object[length];
        return tempArray;
    }

    public class Port{

        public String version() {
            return getVersion();
        }

        public String type() {
            return getSorterType();
        }

        public Object[] sortFile(Object[] arrayToSort, Comparator comparator) {

             return sort(arrayToSort, SortOrder.ASC_ORDER, comparator);

        }
    }

}
