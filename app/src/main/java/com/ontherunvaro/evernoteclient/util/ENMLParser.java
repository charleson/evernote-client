package com.ontherunvaro.evernoteclient.util;

/**
 * Created by ontherunvaro on 30/07/16.
 */
public class ENMLParser {

    private static final String CONTENT_DELIMITER = "en-note";

    public static String parseContent(String enml) {
        int start = enml.indexOf("<" + CONTENT_DELIMITER + ">");
        int end = enml.indexOf("</" + CONTENT_DELIMITER + ">");

        if (start >= 0 && end >= 0) {
            int realStart = start + ("<" + CONTENT_DELIMITER + ">").length();
            return enml.substring(realStart, end);
        }
        return null;
    }

}
