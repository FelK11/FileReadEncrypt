public class ValidCommandManager {


    private final String loadCommandIdentifier = "load data";
    private final String sortCommandIdentifier = "sort data using algorithm";
    private final String encryptCommandIdentifier = "encrypt";
    private final String decryptCommandIdentifier = "decrypt";
    private final String QuitCommandIdentifier = "quit";
    private final String startMacroIdentifier = "start macro";
    private final String finishMacroIdentifier = "finish macro";
    private final String executeMacroIdentifier = "execute macro";


    public String getLoadCommandIdentifier() {
        return loadCommandIdentifier;
    }

    public String getSortCommandIdentifier() {
        return sortCommandIdentifier;
    }

    public String getEncryptCommandIdentifier() {
        return encryptCommandIdentifier;
    }

    public String getDecryptCommandIdentifier() {
        return decryptCommandIdentifier;
    }

    public String getQuitCommandIdentifier() {
        return QuitCommandIdentifier;
    }

    public String getStartMacroIdentifier() {
        return startMacroIdentifier;
    }

    public String getFinishMacroIdentifier() {
        return finishMacroIdentifier;
    }

    public String getExecuteMacroIdentifier() {
        return executeMacroIdentifier;
    }


    public ValidCommandManager() {

    }

    public boolean validCommandCheck(String command) {

        if ((command.startsWith(encryptCommandIdentifier) || command.startsWith(decryptCommandIdentifier) || command.startsWith(loadCommandIdentifier)) &&
                ((command.endsWith("." + FileType.values()[0].toString().toLowerCase())) || command.endsWith("." + FileType.values()[1].toString().toLowerCase()) ||
                        command.endsWith("." + FileType.values()[2].toString().toLowerCase()))) {

            return true;

        } else if (command.startsWith(sortCommandIdentifier) && ((command.endsWith(SortType.values()[0].toString().toLowerCase())) ||
                command.endsWith(SortType.values()[1].toString().toLowerCase()) || command.endsWith(SortType.values()[2].toString().toLowerCase()))) {

            return true;

        } else if (command.equals(QuitCommandIdentifier)) {

            return true;

        } else return (command.startsWith(startMacroIdentifier) || command.startsWith(executeMacroIdentifier) || command.startsWith(finishMacroIdentifier)) &&
                (command.endsWith(Configuration.INSTANCE.macroFileSuffix));
    }

    public void wrongCommand() {
        System.out.println("Error invalid command! Try Again!");
    }

    public String extractFileNameFromString(String command) {

        int beginningIndexOfFilename, endingIndexOfFilename;
        String fileName;


        if (command.startsWith(getLoadCommandIdentifier())) {

            beginningIndexOfFilename = getLoadCommandIdentifier().length();
            endingIndexOfFilename = command.lastIndexOf(".");
            fileName = command.substring(beginningIndexOfFilename, endingIndexOfFilename);
            fileName = fileName.trim();
            return fileName;

        } else if (command.startsWith(getEncryptCommandIdentifier()) || command.startsWith(getDecryptCommandIdentifier())) {

            beginningIndexOfFilename = getEncryptCommandIdentifier().length();
            endingIndexOfFilename = command.lastIndexOf(".");
            fileName = command.substring(beginningIndexOfFilename, endingIndexOfFilename);
            fileName = fileName.trim();
            return fileName;

        } else if (command.startsWith(getStartMacroIdentifier())) {

            beginningIndexOfFilename = getStartMacroIdentifier().length();
            endingIndexOfFilename = command.lastIndexOf(".");
            fileName = command.substring(beginningIndexOfFilename, endingIndexOfFilename);
            fileName = fileName.trim();
            return fileName;

        } else if (command.startsWith(getFinishMacroIdentifier())) {

            beginningIndexOfFilename = getFinishMacroIdentifier().length();
            endingIndexOfFilename = command.lastIndexOf(".");
            fileName = command.substring(beginningIndexOfFilename, endingIndexOfFilename);
            fileName = fileName.trim();
            return fileName;

        } else if (command.startsWith(getExecuteMacroIdentifier())) {

            beginningIndexOfFilename = getExecuteMacroIdentifier().length();
            endingIndexOfFilename = command.lastIndexOf(".");
            fileName = command.substring(beginningIndexOfFilename, endingIndexOfFilename);
            fileName = fileName.trim();
            return fileName;

        } else {
            System.out.println("Error cant get filename");
            System.exit(-33);
        }
        return null;
    }

}
