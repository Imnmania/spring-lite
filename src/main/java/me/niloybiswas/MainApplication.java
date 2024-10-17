package me.niloybiswas;

import me.niloybiswas.spring_lite.SpringLiteApplication;
import me.niloybiswas.spring_lite.annotations.PackageScan;
import me.niloybiswas.spring_lite.core.Environment;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PackageScan(scanPackages = {"me.niloybiswas"})
public class MainApplication {
    public static void main(String[] args) throws Exception {
        SpringLiteApplication.run(MainApplication.class);
    }
}