package ru.itmo.sd.tasks.mvc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ListOfTasks {
    private final String header;
    private final long id;
    private final List<Task> tasks;

    public ListOfTasks(long id, String title) {
        this(id, title, new ArrayList<>());
    }

    public ListOfTasks(long id, String title, List<Task> listOfTasks) {
        this.tasks = new ArrayList<>(listOfTasks);
        this.header = title;
        this.id = id;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public static ListOfTasks valueFrom(ResultSet result) throws SQLException {
        long id = result.getLong(1);
        String title = result.getString(2);
        return new ListOfTasks(id, title);
    }

    @Override
    public String toString() {
        return "List `" + header + "` of " + tasks.toString();
    }

    public long getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(tasks);
    }
}
