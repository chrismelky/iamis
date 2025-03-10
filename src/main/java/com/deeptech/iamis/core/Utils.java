package com.deeptech.iamis.core;

import java.util.*;

public class Utils {

    public static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format(
                        "%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"),
                " ");
    }

    /**
     * @return
     */
    public static UUID generateUuid() {
        return UUID.randomUUID();
    }
}
