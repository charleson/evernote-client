package com.ontherunvaro.evernoteclient.util;

import java.util.Map;

/**
 * Cache for note content.
 * <p/>
 * Created by ontherunvaro on 1/08/16.
 */
public class NoteContentCache {

    private static final int MAX_ENTRIES = 10;
    private static final Map<String, String> cache = new SizeLimitedHashMap<>(MAX_ENTRIES, MAX_ENTRIES);

    /**
     * Gets the content for a given note, if it exists in the cache.
     *
     * @param guid the GUID of the note
     * @return the content of the note if it exists in the cache, <code>null</code> otherwise
     */
    public static String get(String guid) {
        return cache.get(guid);
    }

    /**
     * Puts a GUID-content pair in the cache.
     *
     * @param guid    the GUID of the note
     * @param content the content of the note
     */
    public static void put(String guid, String content) {
        cache.put(guid, content);
    }


}
