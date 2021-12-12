package ru.itmo.sd.tasks.mvc.controller;

import static ru.itmo.sd.tasks.mvc.model.Task.TaskStatus.*;
import static ru.itmo.sd.tasks.db.DBAccess.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import java.util.function.BiFunction;

import java.sql.SQLException;
import java.text.ParseException;

import org.json.JSONObject;

import ru.itmo.sd.tasks.db.DBAccess;
import ru.itmo.sd.tasks.mvc.model.Task;

public enum RequestOperation {

    ADD_LIST((db, r) -> {
        String title = r.getString("title").trim();
        title = RequestUtils.escapeChars(title);

        if (title.length() == 0) {
            return ResponsePresets.error("Title can't be empty");
        }

        try {
            db.addList(title);
        } catch (SQLException e) {
            return ResponsePresets.error(e);
        }

        return ResponsePresets.done();
    }, "title"),

    DELETE_LIST((db, r) -> {
        long listID = r.getLong("list");

        try {
            db.deleteList(listID);
        } catch (SQLException sqle) {
            return ResponsePresets.error(sqle);
        }

        return ResponsePresets.done();
    }, "list"),

    ADD_TASK((db, r) -> {
        long listID = r.getLong("list");

        String description = r.getString("desc").trim();
        description = RequestUtils.escapeChars(description);

        if (description.length() == 0) {
            return ResponsePresets.error("Description can't be empty");
        }

        Date expire = null;
        if (r.has("expireDate")) {
            String expireDate = r.getString("expireDate");
            if (r.has("expireTime")) {
                expireDate += " " + r.getString("expireTime");
            } else {
                expireDate += " 00:00";
            }
            expireDate = expireDate.trim();

            try {
                expire = SQL_FORMAT.parse(expireDate);

                if (!expire.after(new Date(System.currentTimeMillis()))) {
                    return ResponsePresets.error("This task is already failed");
                }
            } catch (ParseException pe) {
                return ResponsePresets.error(pe);
            }
        }

        try {
            db.addTask(listID, description, expire);
        } catch (SQLException e) {
            return ResponsePresets.error(e);
        }

        return ResponsePresets.done();
    }, "list", "desc", "?expireDate", "?expireTime"),

    DELETE_TASK((db, r) -> {
        long taskID = r.getLong("task");

        try {
            db.deleteTask(taskID);
        } catch (SQLException e) {
            return ResponsePresets.error(e);
        }

        return ResponsePresets.done();
    }, "task"),

    UPDATE_TASK((db, r) -> {
        long taskID = r.getLong("task");
        Task task = db.getTask(taskID);

        if (task == null) {
            return ResponsePresets.error("Task not found");
        }

        if (FAILED.equals(task.getStatus())) {
            return ResponsePresets.error("Task is failed (this status can't be undone)");
        }

        try {
            int status = (task.getStatus().ordinal() + 1) % 2;
            db.setTaskStatus(taskID, status);
        } catch (SQLException e) {
            return ResponsePresets.error(e);
        }

        return ResponsePresets.done();
    }, "task");

    private final BiFunction<DBAccess, JSONObject, JSONObject> handler;
    private final List<String> fields;

    RequestOperation(BiFunction<DBAccess, JSONObject, JSONObject> handler, String... fields) {
        this.fields = Collections.unmodifiableList(Arrays.asList(fields));
        this.handler = handler;
    }

    public JSONObject handleRequest(DBAccess db, JSONObject input) {
        try {
            RequestUtils.checkFields(input, fields);
        } catch (RuntimeException re) {
            String name = re.getMessage();
            return ResponsePresets.error("Missed required field `" + name + "`");
        }
        return handler.apply(db, input);
    }

}
