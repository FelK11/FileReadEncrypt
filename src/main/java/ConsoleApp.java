import com.google.common.eventbus.EventBus;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleApp {

    private final EventBus eventBus;
    private InputMediator inputMediator;
    private ValidCommandManager validCommandManager;
    ArrayList<Object> loadList;

    public ConsoleApp() {
        eventBus = new EventBus("Console App");
        build();
    }

    private void build() {
        inputMediator = new InputMediator();
        validCommandManager = new ValidCommandManager();
        loadList = new ArrayList<Object>();
        addSubscriber(inputMediator);
    }

    public void addSubscriber(Subscriber subscriber) {
        eventBus.register(subscriber);
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string: ");
        String str = scanner.nextLine();

        System.out.println(validCommandManager.validCommandsWithoutMacro.get(1));

        if (!validCommandManager.validCommandsWithoutMacro.contains(str)) {
            wrongCommand();
        }

        //LoadEvent
        if (str.equals(validCommandManager.validCommandsWithoutMacro.get(0)) || str.equals(validCommandManager.validCommandsWithoutMacro.get(1)) || str.equals(validCommandManager.validCommandsWithoutMacro.get(2))) {

            if (str.equals(validCommandManager.validCommandsWithoutMacro.get(0))) {
                inputMediator.setCurrentFileType(FileType.JSON);
            } else if (str.equals(validCommandManager.validCommandsWithoutMacro.get(1))){
                inputMediator.setCurrentFileType(FileType.CSV);
            } else if (str.equals(validCommandManager.validCommandsWithoutMacro.get(2))){
                inputMediator.setCurrentFileType(FileType.XML);
            }

            eventBus.post(new LoadFileEvent());
            loadList = inputMediator.getListOfLoadedData();

            for (Object gin : loadList) {
                System.out.println("Gin: " + ReflectionToStringBuilder.toString(gin));
            }

        }

        //SortEvent
        if (str.equals(validCommandManager.validCommandsWithoutMacro.get(3)) || str.equals(validCommandManager.validCommandsWithoutMacro.get(4)) || str.equals(validCommandManager.validCommandsWithoutMacro.get(5))) {

            if (str.equals(validCommandManager.validCommandsWithoutMacro.get(3))) {
                inputMediator.setCurrentSortType(SortType.LAMBDA);
            } else if (str.equals(validCommandManager.validCommandsWithoutMacro.get(4))){
                inputMediator.setCurrentSortType(SortType.MERGE);
            } else if (str.equals(validCommandManager.validCommandsWithoutMacro.get(5))){
                inputMediator.setCurrentSortType(SortType.QUICK);
            }

            eventBus.post(new SortDataEvent());
            /*loadList = inputMediator.getListOfLoadedData();

            for (Object gin : loadList) {
                System.out.println("Gin: " + ReflectionToStringBuilder.toString(gin));
            }*/

        }

    }

    private void wrongCommand() {
        System.out.println("Error invalid command");
        System.exit(-42);
    }
}

