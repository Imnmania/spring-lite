package me.niloybiswas;

import me.niloybiswas.spring_lite.SpringLiteApplication;
import me.niloybiswas.spring_lite.annotations.PackageScan;

@PackageScan(scanPackages = {"me.niloybiswas"})
public class MainApplication {
    public static void main(String[] args) throws Exception {
        SpringLiteApplication.run(MainApplication.class);
    }
}