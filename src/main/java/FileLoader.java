import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class FileLoader {

        private final ArrayList<Object> parserPorts;

        public FileLoader() {
                parserPorts = new ArrayList<>(FileType.values().length);
                build();
        }

        public ArrayList<Object> getFileParsers() {
                return parserPorts;
        }

        public void build() {

                //verify Jar
                try {
                        ProcessBuilder processBuilder = new ProcessBuilder("C:\\Users\\Felix\\.jdks\\openjdk-17.0.2\\bin\\jarsigner", "-verify", Configuration.INSTANCE.pathToFileParserJavaArchive + "fileparser-1.0.jar");
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


                        // parsers
                        for (int i = 0; i < FileType.values().length; i++) {
                                URL[] urls = {new File(Configuration.INSTANCE.pathToFileParserJavaArchive + "fileparser-1.0.jar").toURI().toURL()};
                                URLClassLoader urlClassLoader = new URLClassLoader(urls, FileLoader.class.getClassLoader());

                                Class<?> parserClass = Class.forName(FileType.values()[i] + "_parser", true, urlClassLoader);
                                Object parserInstance = parserClass.getMethod("getInstance").invoke(null);

                                Object parserPort = parserClass.getDeclaredField("port").get(parserInstance);
                                parserPorts.add(parserPort);
                        }

                } catch (Exception e) {
                        System.out.println(e.getMessage());
                }


        }



}
