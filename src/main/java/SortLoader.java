import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class SortLoader {

    private final ArrayList<Object> sorterPorts;

    public SortLoader() {


        sorterPorts = new ArrayList<>(FileType.values().length);
        build();
    }

    public ArrayList<Object> getSorters() {
        return sorterPorts;
    }

    public void build() {


        try {

            for (int i = 0; i < (SortType.values().length); i++) {

                //verify Jar
                ProcessBuilder processBuilder = new ProcessBuilder("C:\\Users\\Felix\\.jdks\\openjdk-17.0.2\\bin\\jarsigner", "-verify", Configuration.INSTANCE.pathToGenericComponentDirectory + SortType.values()[i] + Configuration.INSTANCE.sorterComponentFolderName + Configuration.INSTANCE.pathToGenericComponentJavaArchive + SortType.values()[i] + Configuration.INSTANCE.sorterJarName);
                Process process = processBuilder.start();
                process.waitFor();

                InputStream inputStream = process.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                boolean isComponentAccepted = false;

                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    if (line.contains("verified")) {
                        isComponentAccepted = true;
                    }
                }

                if (isComponentAccepted) {
                    System.out.println("component accepted");
                } else {
                    System.out.println("component rejected");
                }


                // sorters

                URL[] urls = {new File(Configuration.INSTANCE.pathToGenericComponentDirectory + SortType.values()[i] + Configuration.INSTANCE.sorterComponentFolderName + Configuration.INSTANCE.pathToGenericComponentJavaArchive + SortType.values()[i] + Configuration.INSTANCE.sorterJarName).toURI().toURL()};
                URLClassLoader urlClassLoader = new URLClassLoader(urls, SortLoader.class.getClassLoader());

                Class<?> sorterClass = Class.forName(StringUtils.capitalize(SortType.values()[i].toString().toLowerCase()) + "Sorter", true, urlClassLoader);
                Object sorterInstance = sorterClass.getMethod("getInstance").invoke(null);

                Object sorterPort = sorterClass.getDeclaredField("port").get(sorterInstance);
                sorterPorts.add(sorterPort);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

}
