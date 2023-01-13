import java.lang.reflect.Field;
import java.util.ArrayList;

public class ValidCommandManager {


    int numberOfValidCommandsWithoutMacro = 12;

    String command1 = "load data gin." + FileType.values()[0].toString().toLowerCase();
    String command2 = "load data gin." + FileType.values()[1].toString().toLowerCase();
    String command3 = "load data gin." + FileType.values()[2].toString().toLowerCase();

    String command4 = "sort data using algorithm " + SortAlgorithmType.values()[0].toString().toLowerCase();
    String command5 = "sort data using algorithm " + SortAlgorithmType.values()[0].toString().toLowerCase();
    String command6 = "sort data using algorithm " + SortAlgorithmType.values()[0].toString().toLowerCase();

    String command7 = "encrypt gin." + FileType.values()[0].toString().toLowerCase();
    String command8 = "encrypt gin." + FileType.values()[1].toString().toLowerCase();
    String command9 = "encrypt gin." + FileType.values()[2].toString().toLowerCase();

    String command10 = "decrypt gin." + FileType.values()[0].toString().toLowerCase();
    String command11 = "decrypt gin." + FileType.values()[1].toString().toLowerCase();
    String command12 = "decrypt gin." + FileType.values()[2].toString().toLowerCase();

    ArrayList<String> validCommandsWithoutMacro;

    String macroCommand1 = "start macro ";
    String macroCommand2 = "finish macro ";
    String macroCommand3 = "execute macro ";


    public ValidCommandManager() {

        validCommandsWithoutMacro = new ArrayList<>(numberOfValidCommandsWithoutMacro);
        build();

    }

    public void build(){
        for (int i = 0; i < numberOfValidCommandsWithoutMacro; i++) {

            try {
                Field field = ValidCommandManager.class.getDeclaredField(("command" + (i + 1)));
                validCommandsWithoutMacro.add((String) field.get(this));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
