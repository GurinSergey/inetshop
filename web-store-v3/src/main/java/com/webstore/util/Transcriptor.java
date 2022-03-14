package com.webstore.util;

import com.ibm.icu.text.Transliterator;

public class Transcriptor {
    private static final String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";
    private static final String LATIN_TO_CYRILLIC = "Latin-Russian/BGN";

    private static String fillSpaces (String str) {
        return str.trim().replace(" ", "_");
    }

    private static String removeSpaces (String str) {
        return str.trim().replace("_", " ");
    }

    private static String removeApostrophe (String str) {
        return str.trim().replace("ʹ", "");
    }

    public static String cyr2lat(String str) {
        Transliterator toLatinTrans = Transliterator.getInstance(CYRILLIC_TO_LATIN);
        return  removeApostrophe(toLatinTrans.transliterate(fillSpaces(str)));
    }

    public static String lat2cyr(String str) {
        Transliterator toCyrTrans = Transliterator.getInstance(LATIN_TO_CYRILLIC);
        return toCyrTrans.transliterate(removeSpaces(str));
    }
}
