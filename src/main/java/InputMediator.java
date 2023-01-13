import com.google.common.eventbus.Subscribe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

public class InputMediator extends Subscriber{

    private FileLoader fileLoader;
    private FileType currentFileType;
    private Object[] loadedData;
    protected ArrayList<Object> listOfLoadedData;
    private boolean parserExists;

    public FileType getCurrentFileType() {
        return currentFileType;
    }

    public void setCurrentFileType(FileType currentFileType) {
        this.currentFileType = currentFileType;
    }

    public ArrayList<Object> getListOfLoadedData() {
        return listOfLoadedData;
    }

    public void setListOfLoadedData(ArrayList<Object> listOfLoadedData) {
        this.listOfLoadedData = listOfLoadedData;
    }

    public InputMediator() {
        build();
    }

    private void build() {
        parserExists = false;
        fileLoader = new FileLoader();
        listOfLoadedData = new ArrayList<>(1);
    }

    private void parserLoadError(){
        System.out.println("Error! No file parser for specified file Type loaded");
        System.exit(-10);
    }

    private void parserExistCheckReset(){
        parserExists = false;
    }

    @Subscribe
    public void receive(LoadFileEvent loadFileEvent) {

        try {

            for (Object fileParser : fileLoader.getFileParsers()) {

                Method getParserType = fileParser.getClass().getDeclaredMethod("type");
                String parserType = (String) getParserType.invoke(fileParser);

                if (parserType.equals(getCurrentFileType().toString())) {
                    parserExists = true;
                    Method parseFile = fileParser.getClass().getDeclaredMethod("parseFile", String.class);
                    Object[] parsedData = (Object[]) parseFile.invoke(fileParser, Configuration.INSTANCE.pathToGinData + "gin." + getCurrentFileType());
                    loadedData = parsedData;
                    Collections.addAll(listOfLoadedData, parsedData);
                }
            }

            if (!parserExists){
                parserLoadError();
            }
            parserExistCheckReset();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
