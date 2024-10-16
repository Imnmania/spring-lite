package me.niloybiswas.spring_lite.core;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {
    private static final String RESOURCE_PATH = "src/main/java/resources/";

    public static Map<String, Object> readPropertiesFile() throws IOException {
        Map<String, Object> propertiesMap = new HashMap<>();
        List<String> propertiesFileNames = listPropertiesFileNames();
        for (String fileName : propertiesFileNames) {
            File file = new File(RESOURCE_PATH + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strParts = line.split("=");
                if (strParts.length == 2) {
                    String key = strParts[0];
                    String value = strParts[1];
                    propertiesMap.put(key, value);
                }
            }
        }
        System.out.println("properties => " + propertiesMap);
        List<String> profiles = listProfileNames(propertiesFileNames);
        for (String profile : profiles) {
            System.out.println("Profile = " + profile);
        }
        return propertiesMap;
    }

    private static List<String> filterPropertiesFileNames(String[] activeProfiles) {
        return new ArrayList<>();
    }

    private static Map<String, Object> getValueMapFromProperties(String[] propertiesFileNames) throws IOException {
        Map<String, Object> propertiesMap = new HashMap<>();
        for (String fileName : propertiesFileNames) {
            File file = new File(RESOURCE_PATH + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strParts = line.split("=");
                if (strParts.length == 2) {
                    String key = strParts[0];
                    String value = strParts[1];
                    propertiesMap.put(key, value);
                }
            }
        }
        return propertiesMap;
    }

    private static List<String> listPropertiesFileNames() {
        String path = "src/main/java/resources";
        File file = new File(path);
        List<String> fileNames = new ArrayList<>();
        File[] files = file.listFiles();
        assert files != null;
        for (File newFile : files) {
            fileNames.add(newFile.getName());
        }
        return fileNames;
    }

    private static List<String> listProfileNames(List<String> propertiesFileNames) {
        final List<String> profileNames = new ArrayList<>();
        for (String profileName : listPropertiesFileNames()) {
            if (!profileName.contains("-")) continue;
            String[] dashSplits = profileName.split("-");
            if (dashSplits.length == 2) {
                String tempName = dashSplits[1];
                String finalName = tempName.split("\\.")[0];
                profileNames.add(finalName);
            }
        }
        return profileNames;
    }
}
