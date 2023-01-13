public enum Configuration {

    INSTANCE;

    public final String userDirectory = System.getProperty("user.dir");
    public final String fileSeparator = System.getProperty("file.separator");
    public final String pathToFileParserJavaArchive = userDirectory + fileSeparator + "fileParser" + fileSeparator + "jar" + fileSeparator;
    public final String pathToGenericSorterComponent = userDirectory + fileSeparator;
    public final String pathToMergeSorterComponentJavaArchive = fileSeparator + "jar" + fileSeparator;
    public final String pathToGinData = userDirectory + fileSeparator + "fileParser" + fileSeparator + "gin_data" + fileSeparator;


    public final int maximumNumberOfEnginesPerWing = 2;


}
