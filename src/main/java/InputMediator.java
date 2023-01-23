import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.Subscribe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class InputMediator extends Subscriber {

    private final ObjectMapper objectMapper;
    private final Scanner scanner;
    private ValidCommandManager validCommandManager;
    private FileLoader fileLoader;
    private SortLoader sortLoader;
    private CryptoLoader cryptoLoader;
    private CustomFileWriter customFileWriter;
    private FileType currentFileType;
    private SortType currentSortType;
    private String currentFileName;
    private String[] currentSortFieldNames;
    private Object[] currentLoadedData;
    private Comparator dataComparator;
    private boolean componentExists;
    private boolean loadDecryptedFile;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public ValidCommandManager getValidCommandManager() {
        return validCommandManager;
    }

    public FileLoader getFileLoader() {
        return fileLoader;
    }

    public SortLoader getSortLoader() {
        return sortLoader;
    }

    public CryptoLoader getCryptoLoader() {
        return cryptoLoader;
    }

    public CustomFileWriter getCustomFileWriter() {
        return customFileWriter;
    }

    public FileType getCurrentFileType() {
        return currentFileType;
    }

    public void setCurrentFileType(FileType currentFileType) {
        this.currentFileType = currentFileType;
    }

    public SortType getCurrentSortType() {
        return currentSortType;
    }

    public void setCurrentSortType(SortType currentSortType) {
        this.currentSortType = currentSortType;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public String[] getCurrentSortFieldNames() {
        return currentSortFieldNames;
    }

    public Object[] getCurrentLoadedData() {
        return currentLoadedData;
    }

    public InputMediator() {
        objectMapper = new ObjectMapper();
        scanner = new Scanner(System.in);
        build();
    }

    private void build() {
        loadDecryptedFile = false;
        componentExists = false;
        validCommandManager = new ValidCommandManager();
        fileLoader = new FileLoader();
        sortLoader = new SortLoader();
        cryptoLoader = new CryptoLoader();
        customFileWriter = new CustomFileWriter();

    }

    private void parserLoadError() {
        System.out.println("Error! No file parser for specified file Type loaded");
        System.exit(-10);
    }

    private void componentExistCheckReset() {
        componentExists = false;
    }

    private void generateDataComparatorForFirstKey(int fieldNumberToSortAfter) {

        String fieldName = (String) ((LinkedHashMap) currentLoadedData[0]).keySet().toArray()[fieldNumberToSortAfter];
        // Generate Comparator
        dataComparator = Comparator.comparing((LinkedHashMap<String, String> entry) -> entry.get(fieldName));

    }

    private void generateFieldNamesForComparator() {

        currentSortFieldNames = new String[((LinkedHashMap) currentLoadedData[0]).keySet().toArray().length];
        for (int i = 0; i < ((LinkedHashMap) currentLoadedData[0]).keySet().toArray().length; i++) {
            currentSortFieldNames[i] = (String) ((LinkedHashMap) currentLoadedData[0]).keySet().toArray()[i];

        }

    }

    @Subscribe
    public void receive(LoadFileEvent loadFileEvent) {


        try {

            for (Object fileParser : fileLoader.getFileParsers()) {

                Method getParserType = fileParser.getClass().getDeclaredMethod("type");
                String parserType = (String) getParserType.invoke(fileParser);

                if (parserType.equals(getCurrentFileType().toString())) {
                    componentExists = true;
                    Method parseFile = fileParser.getClass().getDeclaredMethod("parseFile", String.class);

                    if (!loadDecryptedFile) {
                        setCurrentFileName(getCurrentFileName().concat("."));
                        Object[] parsedData = (Object[]) parseFile.invoke(fileParser, Configuration.INSTANCE.pathToGinData + getCurrentFileName() + getCurrentFileType());
                        currentLoadedData = parsedData;
                    } else {
                        Object[] parsedData = (Object[]) parseFile.invoke(fileParser, Configuration.INSTANCE.pathToGinData + getCurrentFileName() + getCurrentFileType());
                        currentLoadedData = parsedData;
                        loadDecryptedFile = false;
                    }
                }
            }

            if (!componentExists) {
                parserLoadError();
            }
            componentExistCheckReset();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Subscribe
    public void receive(SortDataEvent sortDataEvent) {

        generateFieldNamesForComparator();
        boolean validFieldNumber = false;

        while (!validFieldNumber) {

            int fieldNumber = 0;
            System.out.println("Choose field number to sort data after:");
            for (int i = 0; i < currentSortFieldNames.length; i++) {

                System.out.print((i + 1) + " - " + currentSortFieldNames[i] + "; ");

            }
            System.out.println();

            String fieldNumberString = getScanner().nextLine();
            try {
                fieldNumber = Integer.parseInt(fieldNumberString);
            } catch (NumberFormatException ne){
                System.out.println("Error input valid Number!");
            }

            if (fieldNumber != 0 && fieldNumber <= currentSortFieldNames.length){
                generateDataComparatorForFirstKey(fieldNumber - 1);
                validFieldNumber = true;
            } else {
                System.out.println("Error! Enter field number accordingly to sort field!");
            }

        }


        try {

            for (Object sorter : sortLoader.getSorters()) {

                Method getSorterType = sorter.getClass().getDeclaredMethod("type");
                String sorterType = ((String) getSorterType.invoke(sorter)).toUpperCase();

                if (sorterType.equals(getCurrentSortType().toString())) {
                    componentExists = true;
                    Method sortData = sorter.getClass().getDeclaredMethod("sortFile", Object[].class, Comparator.class);
                    Object[] sortedData = (Object[]) sortData.invoke(sorter, currentLoadedData, dataComparator);
                    currentLoadedData = sortedData;

                }
            }
            if (!componentExists) {
                parserLoadError();
            }
            componentExistCheckReset();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    @Subscribe
    public void receive(EncryptDataEvent encryptDataEvent) {

        setCurrentFileName(getCurrentFileName().concat("."));

        //write File
        File fileToEncrypt = getCustomFileWriter().writeFile(getCurrentLoadedData(), getCurrentFileType(), getCurrentFileName());

        try {
            Method encryptData = getCryptoLoader().getCryptos().get(0).getClass().getDeclaredMethod("encrypt", String.class, File.class, File.class);
            encryptData.invoke(getCryptoLoader().getCryptos().get(0), Configuration.INSTANCE.cryptoKey, fileToEncrypt, fileToEncrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void receive(DecryptDataEvent decryptDataEvent) {

        //get File
        File fileToDecrypt = new File(Configuration.INSTANCE.pathToGinData + getCurrentFileName() + "." + getCurrentFileType().toString().toLowerCase());
        File decryptedFile = new File(Configuration.INSTANCE.pathToGinData + getCurrentFileName() + Configuration.INSTANCE.decryptedDataFileSuffix + getCurrentFileType().toString().toLowerCase());

        try {
            Method encryptData = getCryptoLoader().getCryptos().get(0).getClass().getDeclaredMethod("decrypt", String.class, File.class, File.class);
            encryptData.invoke(getCryptoLoader().getCryptos().get(0), Configuration.INSTANCE.cryptoKey, fileToDecrypt, decryptedFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setCurrentFileName(getCurrentFileName().concat(Configuration.INSTANCE.decryptedDataFileSuffix));

        loadDecryptedFile = true;
    }


    @Subscribe
    public void receive(StartMacroEvent startMacroEvent) {

        BufferedWriter macroWriter = null;

        boolean firstCommand = false;
        boolean finishMacro = false;
        ArrayList<String> commandList = new ArrayList<>();

        while (!finishMacro) {

            System.out.println("Enter next macro command: ");
            String nextCommand = scanner.nextLine();


            if (getValidCommandManager().validCommandCheck(nextCommand)) {

                if (!firstCommand && (nextCommand.startsWith(validCommandManager.getSortCommandIdentifier()) || nextCommand.startsWith(validCommandManager.getEncryptCommandIdentifier()))) {
                    System.out.println("Error must load data before encrypting or sorting! Try again");
                } else if (nextCommand.startsWith(validCommandManager.getStartMacroIdentifier()) || nextCommand.startsWith(validCommandManager.getExecuteMacroIdentifier())) {
                    System.out.println("Error! Can't use start or execute Macro Command before finishing the macro! Try Again");
                } else if (nextCommand.startsWith(validCommandManager.getFinishMacroIdentifier())) {

                    finishMacro = true;
                    currentFileName = validCommandManager.extractFileNameFromString(nextCommand);

                    File currentMacro = new File(Configuration.INSTANCE.pathToGinData + getCurrentFileName() + Configuration.INSTANCE.macroFileSuffix);
                    try {
                        macroWriter = new BufferedWriter(new FileWriter(currentMacro, true));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //write commands to file
                    for (String macroCommand : commandList) {
                        try {
                            macroWriter.append(macroCommand);
                            macroWriter.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        macroWriter.flush();
                        macroWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //add macro to macro list
                } else {
                    firstCommand = true;
                    commandList.add(nextCommand);
                }
            } else {
                System.out.println("Error! Invalid command! Try again!");
            }
        }
    }

}
