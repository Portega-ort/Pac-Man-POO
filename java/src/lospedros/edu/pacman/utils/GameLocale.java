package lospedros.edu.pacman.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class GameLocale {

    private static ResourceBundle messages;
    private static Locale currentLocale;

    public static void setLocale(String language) {
        if ("es".equals(language)) {
            currentLocale = new Locale("es", "ES");
        } else {
            currentLocale = new Locale("en", "US");
        }
        messages = ResourceBundle.getBundle("messages", currentLocale);
    }

    public static String getString(String key) {
        if (messages == null) {
            setLocale("en"); // Default to English
        }
        return messages.getString(key);
    }
}