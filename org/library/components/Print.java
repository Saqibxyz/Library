package org.library.components;

public class Print {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String WHITE_BOLD = "\u001B[1;37m";
    public static void success(String message) {
        System.out.println(GREEN + "[SUCCESS] " + message + RESET);
    }
    public static void error(String message) {
        System.out.println(RED + "[ERROR] " + message + RESET);
    }
    public static void warning(String message) {
        System.out.println(YELLOW + "[WARNING] " + message + RESET);
    }
    public static void info(String message) {
        System.out.println(CYAN + "[INFO] " + message + RESET);
    }
    public static void header(String message) {
        System.out.println(MAGENTA + "\n===== " + message + " =====" + RESET);
    }
    public static void bold(String message) {
        System.out.println(WHITE_BOLD + message + RESET);
    }
    public static void println(String message) {
        System.out.println(BLUE + message + RESET);
    }
}
