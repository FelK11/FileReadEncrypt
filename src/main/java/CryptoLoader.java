import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class CryptoLoader {

    private final ArrayList<Object> cryptoPorts;

    public CryptoLoader() {
        cryptoPorts = new ArrayList<>(FileType.values().length);
        build();
    }

    public ArrayList<Object> getCryptos() {
        return cryptoPorts;
    }

    public void build() {


        try {

            for (int i = 0; i < (SortType.values().length); i++) {

                //verify Jar

                ProcessBuilder processBuilder = new ProcessBuilder("C:\\Users\\Felix\\.jdks\\openjdk-17.0.2\\bin\\jarsigner", "-verify", Configuration.INSTANCE.pathToGenericComponentDirectory + Configuration.INSTANCE.cryptoComponentFolderName + Configuration.INSTANCE.pathToGenericComponentJavaArchive + Configuration.INSTANCE.cryptoJarName);
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


                // load crypto

                URL[] urls = {new File(Configuration.INSTANCE.pathToGenericComponentDirectory + Configuration.INSTANCE.cryptoComponentFolderName + Configuration.INSTANCE.pathToGenericComponentJavaArchive + Configuration.INSTANCE.cryptoJarName).toURI().toURL()};
                URLClassLoader urlClassLoader = new URLClassLoader(urls, SortLoader.class.getClassLoader());

                Class<?> cryptoClass = Class.forName("Crypto", true, urlClassLoader);
                Object cryptoInstance = cryptoClass.getMethod("getInstance").invoke(null);

                Object cryptoPort = cryptoClass.getDeclaredField("port").get(cryptoInstance);
                cryptoPorts.add(cryptoPort);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

}
