package me.niloybiswas.spring_lite.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {
    public static List<Class<?>> getRecursiveClasses(String filePath, String packageName) throws ClassNotFoundException {
        // files must be in a directory
        File file = new File(filePath);
        if (!file.isDirectory()) {
            return new ArrayList<>();
        }
        // return filenames when they are within the package/directory
        List<Class<?>> classList = new ArrayList<>();
        File[] subFiles = file.listFiles();
        for (File newFile : subFiles) {
            if (newFile.isFile() && newFile.getName().endsWith(".class")) {
                String className = packageName + "." + newFile.getName().replace(".class", "");
                classList.add(Class.forName(className));
            } else {
                classList.addAll(getRecursiveClasses(newFile.getAbsolutePath(), packageName + "." + newFile.getName()));
            }
        }
        return classList;
    }
}
