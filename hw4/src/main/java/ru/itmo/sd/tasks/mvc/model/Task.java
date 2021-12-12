package ru.itmo.sd.tasks.mvc.model;

import java.sql.Timestamp;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Task {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    private final long id;
    private final Date expire;
    private final String description;
    private final TaskStatus status;

    public Task(long id, String desc, int status) {
        this(id, desc, null, status);
    }

    public Task(long id, String desc, Date expire, int status) {
        this.description = desc;
        this.id = id;
        if (expire != null) {
            Date now = new Date(System.currentTimeMillis());
            if (now.after(expire) && status != TaskStatus.DONE.ordinal()) {
                status = TaskStatus.FAILED.ordinal();
            }
        }
        this.expire = expire;
        this.status = TaskStatus.values()[status];
    }

    public boolean hasExpireDate() {
        return expire != null;
    }

    public static Task valueFrom(ResultSet result) throws SQLException {
        long id = result.getLong(1);
        Timestamp timestamp = result.getTimestamp(3);
        String description = result.getString(4);
        int status = result.getInt(5);
        if (timestamp != null) {
            Date date = new Date(timestamp.getTime());
            return new Task(id, description, date, status);
        }
        return new Task(id, description, status);
    }

    public String getExpireDate() {
        return DATE_FORMAT.format(expire);
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Task `")
                .append(description)
                .append("` ")
                .append("[")
                .append(status)
                .append("]");
        if (hasExpireDate()) {
            sb.append(" (till ")
                    .append(DATE_FORMAT.format(expire))
                    .append(")");
        }
        return sb.toString();
    }

    public long getId() {
        return id;
    }

    public Date getExpire() {
        return expire;
    }

    public String getDescription() {
        return description;
    }

    public enum TaskStatus {

        IN_PROCESS("in process", ""),
        DONE("done", "task-done"),
        FAILED("failed", "task-failed");

        private final String PRINT, STYLE;

        TaskStatus(String print, String style) {
            this.PRINT = print;
            this.STYLE = style;
        }

        @Override
        public String toString() {
            return PRINT;
        }

        public String getStyle() {
            return STYLE;
        }
    }

}
