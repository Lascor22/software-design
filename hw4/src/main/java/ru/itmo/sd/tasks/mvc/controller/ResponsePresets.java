package ru.itmo.sd.tasks.mvc.controller;

import org.json.JSONObject;

public class ResponsePresets {

    public static JSONObject error(String cause, String message, String comment) {
        JSONObject root = new JSONObject();
        root.put("status", "error");

        if (comment != null) {
            root.put("comment", comment);
        }
        if (message != null) {
            root.put("reason", message);
        }
        if (cause != null) {
            root.put("cause", cause);
        }

        return root;
    }

    public static JSONObject error(String message) {
        return error(null, null, message);
    }

    public static JSONObject error(Throwable cause) {
        return error(cause.getClass().getName(), cause.getMessage(), null);
    }

    public static JSONObject done() {
        JSONObject root = new JSONObject();
        root.put("status", "done");
        return root;
    }
}
