package com.wika.wikachat.utils;

public class FieldsUtil {

    /**
     * Checks if all the fields in the arguments are not empty
     * @param field
     * @return
     */
    public static boolean isFulfilled (String... field) {
        for (String f : field) {
            if (f == null || "".equals(f)) {
                return false;
            }
        }
        return true;
    }

}
