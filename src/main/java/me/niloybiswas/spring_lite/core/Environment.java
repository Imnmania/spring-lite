package me.niloybiswas.spring_lite.core;

import me.niloybiswas.spring_lite.annotations.Component;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Environment {

    public Environment() {
        readPropertiesFile();
    }

    private static final String RESOURCE_PATH = "src/main/java/resources/";
    private static final String ACTIVE_PROFILE_KEY = "spring.profiles.active";
    private static final Map<String, Object> PROPERTIES = new HashMap<>();

    public String getProperty(String key) {
        if (key.startsWith("${") && key.endsWith("}")) {
            key = key.replaceAll("\\$", "");
            key = key.replaceAll("\\{", "");
            key = key.replaceAll("}", "");
            return (String) PROPERTIES.get(key);
        }
        return null;
    }

    private void readPropertiesFile() {
        try {
            Map<String, Object> propertiesMap = new HashMap<>();

            // find default profile
            String defaultProfileFileName = getDefaultProfilePropertiesFileName();
            /*System.out.println("[Default Profile File] => " + defaultProfileFileName);*/

            // write default profile properties to propertiesMap
            writePropertiesValuesToMap(Collections.singletonList(defaultProfileFileName), propertiesMap);
            /*System.out.println("[propertiesMap] => " + propertiesMap);*/

            // find active profiles
            List<String> activeProfileNames = getActiveProfileNames(propertiesMap);
            List<String> activeProfileFileNames = getActiveProfilePropertiesFileNames(activeProfileNames);
            System.out.println(Utils.getPartialGreenText("[Active Profiles]: ") + Arrays.toString(activeProfileNames.toArray()));
            /*System.out.println("[active profile files] => " + Arrays.toString(activeProfileFileNames.toArray()));*/

            // write active profiles properties to propertiesMap
            writePropertiesValuesToMap(activeProfileFileNames, propertiesMap);
            /*System.out.println("[updated propertiesMap] = " + propertiesMap);*/

            PROPERTIES.putAll(propertiesMap);
        } catch (Exception e) {
            Utils.printRedText("[ERROR]: Failed to read properties file!");
            e.printStackTrace();
        }
    }

    private void writePropertiesValuesToMap(List<String> propertiesFileNames, Map<String, Object> propertiesMap) throws IOException {
        for (String fileName : propertiesFileNames) {
            File file = new File(RESOURCE_PATH + fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strParts = line.split("=");
                if (strParts.length == 2) {
                    String key = strParts[0].trim();
                    String value = strParts[1].trim();
                    propertiesMap.put(key, value);
                }
            }
        }
    }

    private List<String> getAllPropertiesFileNames() {
        File file = new File(RESOURCE_PATH);
        List<String> fileNames = new ArrayList<>();
        File[] files = file.listFiles();
        assert files != null;
        for (File newFile : files) {
            fileNames.add(newFile.getName());
        }
        return fileNames;
    }

    private String getDefaultProfilePropertiesFileName() {
        List<String> propertiesFileNames = getAllPropertiesFileNames();
        return propertiesFileNames
                .stream()
                .filter(name -> !name.contains("-"))
                .collect(Collectors.toList())
                .get(0);
    }

    private List<String> getActiveProfilePropertiesFileNames(List<String> activeProfileNames) {
        List<String> propertiesFileNames = getAllPropertiesFileNames();
        List<String> activeProfilePropertiesFileNames = new ArrayList<>();

        for (String propertiesFileName : propertiesFileNames) {
            for (String activeProfileName : activeProfileNames) {
                if (propertiesFileName.contains(activeProfileName)) {
                    activeProfilePropertiesFileNames.add(propertiesFileName);
                }
            }
        }
        return activeProfilePropertiesFileNames;
    }

    private List<String> getActiveProfileNames(Map<String, Object> propertiesMap) {
        String activeProfileName = (String) propertiesMap.get(ACTIVE_PROFILE_KEY);
        if (activeProfileName != null && activeProfileName.contains(",")) {
            String[] split = activeProfileName.split(",");
            return Arrays.stream(split).map(String::trim).collect(Collectors.toList());
        }
        return Collections.singletonList(activeProfileName);
    }

    private List<String> getExtraProfileNames(List<String> propertiesFileNames) {
        final List<String> profileNames = new ArrayList<>();
        for (String profileName : getAllPropertiesFileNames()) {
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
