package ru.itmo.sd.tasks;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PropertiesLoader {
    public static void load(InputStream is) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    Arrays.asList(parts).forEach(String::trim);
                    System.setProperty(parts[0], parts[1]);
                }
            }
        }
    }

}
