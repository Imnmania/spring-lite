package me.niloybiswas.spring_lite.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String convertPackageToPath(String packageName) {
        return packageName.replace(".", "/");
    }
}
