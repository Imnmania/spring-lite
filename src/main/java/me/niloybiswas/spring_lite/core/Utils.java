package me.niloybiswas.spring_lite.core;

public class Utils {

    public static void printRedText(String text) {
        System.out.println(AnsiColor.RED + text + AnsiColor.RESET);
    }

    public static void printBlueText(String text) {
        System.out.println(AnsiColor.BLUE + text + AnsiColor.RESET);
    }

    public static void printGreenText(String text) {
        System.out.println(AnsiColor.GREEN + text + AnsiColor.RESET);
    }

    public static String getPartialRedText(String text) {
        return AnsiColor.RED + text + AnsiColor.RESET;
    }

    public static String getPartialBlueText(String text) {
        return AnsiColor.BLUE + text + AnsiColor.RESET;
    }

    public static String getPartialGreenText(String text) {
        return AnsiColor.GREEN + text + AnsiColor.RESET;
    }
}
