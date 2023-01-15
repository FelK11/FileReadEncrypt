import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConsoleApp {

    private final EventBus eventBus;
    private InputMediator inputMediator;
    private boolean stopApp;

    public ConsoleApp() {
        eventBus = new EventBus("Console App");
        build();
    }

    private void build() {
        inputMediator = new InputMediator();
        stopApp = false;
        addSubscriber(inputMediator);
    }

    public void addSubscriber(Subscriber subscriber) {
        eventBus.register(subscriber);
    }

    public void start() {

        while (!stopApp) {

            System.out.println("Enter a command: ");
            String command = inputMediator.getScanner().nextLine();

            if (inputMediator.getValidCommandManager().validCommandCheck(command)) {

                doCommand(command);

            } else {
                inputMediator.getValidCommandManager().wrongCommand();
            }

        }
    }


    private void doCommand(String command) {

        //StopApp
        if (command.equals(inputMediator.getValidCommandManager().getQuitCommandIdentifier())) {
            stopApp = true;
        }

        //LoadEvent
        if (command.startsWith(inputMediator.getValidCommandManager().getLoadCommandIdentifier())) {

            if (command.endsWith(FileType.values()[0].toString().toLowerCase())) {
                inputMediator.setCurrentFileType(FileType.JSON);
            } else if (command.endsWith(FileType.values()[1].toString().toLowerCase())) {
                inputMediator.setCurrentFileType(FileType.CSV);
            } else if (command.endsWith(FileType.values()[2].toString().toLowerCase())) {
                inputMediator.setCurrentFileType(FileType.XML);
            }

            inputMediator.setCurrentFileName(inputMediator.getValidCommandManager().extractFileNameFromString(command));

            eventBus.post(new LoadFileEvent());

            System.out.println("Loaded Data from " + inputMediator.getCurrentFileName() + inputMediator.getCurrentFileType().toString().toLowerCase() + ": ");
            for (Object gin : inputMediator.getCurrentLoadedData()) {
                try {
                    System.out.println("Gin: " + inputMediator.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(gin));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("---------------------------------------------------");
        }

        //SortEvent
        if (command.startsWith(inputMediator.getValidCommandManager().getSortCommandIdentifier())) {

            if (command.endsWith(SortType.LAMBDA.toString().toLowerCase())) {
                inputMediator.setCurrentSortType(SortType.LAMBDA);
            } else if (command.endsWith(SortType.MERGE.toString().toLowerCase())) {
                inputMediator.setCurrentSortType(SortType.MERGE);
            } else if (command.endsWith(SortType.QUICK.toString().toLowerCase())) {
                inputMediator.setCurrentSortType(SortType.QUICK);
            }

            eventBus.post(new SortDataEvent());

            System.out.println("Sorted Data with " + inputMediator.getCurrentSortType().toString().toLowerCase() + "sort: ");
            for (Object gin : inputMediator.getCurrentLoadedData()) {
                try {
                    System.out.println("Gin: " + inputMediator.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(gin));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("---------------------------------------------------");
        }


        //EncryptEvent
        if (command.startsWith(inputMediator.getValidCommandManager().getEncryptCommandIdentifier())) {

            if (command.endsWith(FileType.values()[0].toString().toLowerCase())) {
                inputMediator.setCurrentFileType(FileType.JSON);
            } else if (command.endsWith(FileType.values()[1].toString().toLowerCase())) {
                inputMediator.setCurrentFileType(FileType.CSV);
            } else if (command.endsWith(FileType.values()[2].toString().toLowerCase())) {
                inputMediator.setCurrentFileType(FileType.XML);
            }

            inputMediator.setCurrentFileName(inputMediator.getValidCommandManager().extractFileNameFromString(command));

            eventBus.post(new EncryptDataEvent());

            System.out.println("Encrypted data to:");
            System.out.println(inputMediator.getCurrentFileName() + inputMediator.getCurrentFileType());
            System.out.println("---------------------------------------------------");

        }

        //DecryptEvent
        if (command.startsWith(inputMediator.getValidCommandManager().getDecryptCommandIdentifier())) {

            if (command.endsWith(FileType.values()[0].toString().toLowerCase())) {
                inputMediator.setCurrentFileType(FileType.JSON);
            } else if (command.endsWith(FileType.values()[1].toString().toLowerCase())) {
                inputMediator.setCurrentFileType(FileType.CSV);
            } else if (command.endsWith(FileType.values()[2].toString().toLowerCase())) {
                inputMediator.setCurrentFileType(FileType.XML);
            }

            inputMediator.setCurrentFileName(inputMediator.getValidCommandManager().extractFileNameFromString(command));

            eventBus.post(new DecryptDataEvent());
            eventBus.post(new LoadFileEvent());

            System.out.println("Loaded decrypted Data from " + inputMediator.getCurrentFileName() + inputMediator.getCurrentFileType().toString().toLowerCase() + ": ");
            for (Object gin : inputMediator.getCurrentLoadedData()) {
                try {
                    System.out.println("Gin: " + inputMediator.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(gin));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("---------------------------------------------------");

        }

        //StartMacroEvent
        if (command.startsWith(inputMediator.getValidCommandManager().getStartMacroIdentifier())) {

            inputMediator.setCurrentFileName(inputMediator.getValidCommandManager().extractFileNameFromString(command));

            eventBus.post(new StartMacroEvent());
        }

        //ExecuteMacroEvent
        if (command.startsWith(inputMediator.getValidCommandManager().getExecuteMacroIdentifier())) {

            inputMediator.setCurrentFileName(inputMediator.getValidCommandManager().extractFileNameFromString(command));

            //Load Macro
            Scanner executeMacroScanner = null;
            try {
                executeMacroScanner = new Scanner(new File(Configuration.INSTANCE.pathToGinData + inputMediator.getCurrentFileName() + Configuration.INSTANCE.macroFileSuffix));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //ExecuteMacro
            while (executeMacroScanner.hasNextLine()) {
                String currentCommand = executeMacroScanner.nextLine();
                if (inputMediator.getValidCommandManager().validCommandCheck(currentCommand)) {
                    doCommand(currentCommand);
                } else {
                    System.out.println("Error! Invalid Macro");
                    break;
                }
            }

        }

    }

}

