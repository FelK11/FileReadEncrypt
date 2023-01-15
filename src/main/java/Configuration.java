public enum Configuration {

    INSTANCE;

    public final String userDirectory = System.getProperty("user.dir");
    public final String fileSeparator = System.getProperty("file.separator");
    public final String pathToGenericComponentDirectory = userDirectory + fileSeparator;
    public final String pathToGenericComponentJavaArchive = fileSeparator + "jar" + fileSeparator;
    public final String pathToGinData = userDirectory + fileSeparator + "gin_data" + fileSeparator;
    public final String cryptoComponentFolderName = "CryptographyComponent";
    public final String sorterComponentFolderName = "SorterComponent";
    public final String parserComponentFolderName = "_ParserComponent";
    public final String parserJarName = "_Parser-1.0.jar";
    public final String sorterJarName = "SorterComponent-1.0.jar";
    public final String cryptoJarName = "CryptographyComponent-1.0.jar";
    public final String decryptedDataFileSuffix = "_decrypted.";
    public final String macroFileSuffix = ".rec";


    public final String cryptoKey = "d*h*b*w$2*0*2*3*";


}
