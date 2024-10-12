package me.niloybiswas.spring_lite;

import me.niloybiswas.spring_lite.annotations.PackageScan;

public class SpringLiteApplication {
    public static void run(Class<?> mainClass) {
        PackageScan packageScan = mainClass.getAnnotation(PackageScan.class);
        for (String packageName : packageScan.scanPackages()) {
            System.out.println("packageName = " + packageName);
        }
    }
}
