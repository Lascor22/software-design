package ru.itmo.sd.tasks.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class RequestUtils {
    private static final Map<Character, String> ESCAPE_MAP = new HashMap<>();

    static {
        ESCAPE_MAP.put('&', "&amp;");
        ESCAPE_MAP.put('<', "&lt;");
        ESCAPE_MAP.put('>', "&gt;");
        ESCAPE_MAP.put('"', "&quot;");
        ESCAPE_MAP.put('\'', "&#039;");
    }

    public static String escapeChars(String input) {
        if (input == null || input.length() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (ESCAPE_MAP.containsKey(c)) {
                sb.append(ESCAPE_MAP.get(c));
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static void checkFields(JSONObject input, List<String> fields) {
        fields.forEach(p -> {
            boolean isRequired = p.charAt(0) != '?';
            String name = isRequired ? p : p.substring(1);

            if (isRequired && !input.has(name)) {
                throw new RuntimeException(name);
            }
        });
    }

}
