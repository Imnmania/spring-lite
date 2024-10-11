package me.niloybiswas.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String convertPackageToPath(String packageName) {
        return packageName.replace(".", "/");
    }

    public static List<String> recursiveFiles(String filePath) {
        // files must be in a directory
        File file = new File(filePath);
        if (!file.isDirectory()) {
            return new ArrayList<>();
        }
        // return filenames when they are within the package/directory
        List<String> fileList = new ArrayList<>();
        File[] subFiles = file.listFiles();
        for (File newFile : subFiles) {
            if (newFile.isFile()) {
                fileList.add(newFile.getAbsolutePath());
            } else {
                fileList.addAll(recursiveFiles(newFile.getAbsolutePath()));
            }
        }
        return fileList;
    }
}
