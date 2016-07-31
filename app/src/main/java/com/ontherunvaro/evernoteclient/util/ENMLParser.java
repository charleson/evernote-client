package com.ontherunvaro.evernoteclient.util;

import com.evernote.client.android.EvernoteUtil;

/**
 * Created by ontherunvaro on 30/07/16.
 */
public class ENMLParser {

    private static final String CONTENT_DELIMITER = "en-note";

    public static String parseContent(String enml) {
        return enml.replace(EvernoteUtil.NOTE_PREFIX, "").replace(EvernoteUtil.NOTE_SUFFIX, "");
    }

}
