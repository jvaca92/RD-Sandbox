package my.sandbox.pf4j.spring.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class PluginUtils {

    public static InputStream getJarResourceAsStream(JarFile jarFile, String filename) throws IOException {
        final InputStream[] inputStream = new InputStream[1];
        inputStream[0] = jarFile.getInputStream(Collections.list(jarFile.entries())
                .stream()
                .filter(entry -> entry.getName().equalsIgnoreCase(filename))
                .findFirst()
                .orElse(null));
        return inputStream[0];
    }
}
