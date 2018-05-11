package com.comp3617.bitcoinCasino;

import android.util.Log;

/**
 * Logger class is the solution to the problem that log.d doesn't work with embedded unit tests.
 * In the logcat, the logs are duplicated to Log.d and system's standard out for the clarity and
 * universal uses from various sources.
 */
public class Logger {

    private static String TAG = "(+++LOGGER+++)";

    private static String DEBUG = "DEBUG";
    private static String ERROR = "ERROR";
    private static String WARNING = "WARNING";
    private static String INFORMATION = "INFORMATION";

    public static void debug(Object object, String message) {
        Log.d(DEBUG, message);
        System.out.println(TAG + " [" + DEBUG + "] "
                + object.getClass().getSimpleName()
                + ": " + message);
    }

    public static void error(Object object, String message) {
        Log.d(ERROR, message);
        System.out.println(TAG + " [" + ERROR + "] "
                + object.getClass().getSimpleName()
                + ": " + message);
    }

    public static void warn(Object object, String message) {
        Log.d(WARNING, message);
        System.out.println(TAG + " [" + WARNING + "] "
                + object.getClass().getSimpleName()
                + ": " + message);
    }

    public static void info(Object object, String message) {
        Log.d(INFORMATION, message);
        System.out.println(TAG + " [" + INFORMATION + "] "
                + object.getClass().getSimpleName()
                + ": " + message);
    }

}
