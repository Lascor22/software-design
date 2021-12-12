package ru.itmo.sd.tasks.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ru.itmo.sd.tasks.mvc.model.ListOfTasks;
import ru.itmo.sd.tasks.mvc.model.Task;

public class DBAccess {
    private final DBConnection db;

    public static final DateFormat SQL_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private PreparedStatement tasksByListStmt;
    private PreparedStatement allListsStmt;
    private PreparedStatement taskByIdStmt;
    private PreparedStatement addListStmt;
    private PreparedStatement deleteListStmt;
    private PreparedStatement addExpireTaskStmt;
    private PreparedStatement addTaskStmt;
    private PreparedStatement deleteTaskStmt;
    private PreparedStatement updateTaskStatusStmt;

    private void createStatements() throws SQLException {
        String SQL_SELECT_ALL_LISTS = "SELECT * FROM lists ORDER BY id";
        allListsStmt = db.prepareStatement(SQL_SELECT_ALL_LISTS);

        String SQL_SELECT_TASKS_BY_LIST = "SELECT * FROM tasks WHERE list= ? ORDER BY id";
        tasksByListStmt = db.prepareStatement(SQL_SELECT_TASKS_BY_LIST);

        String SQL_SELECT_TASK = "SELECT * FROM tasks WHERE id= ?";
        taskByIdStmt = db.prepareStatement(SQL_SELECT_TASK);

        String SQL_INSERT_LIST = "INSERT INTO lists (title) VALUES (?)";
        addListStmt = db.prepareStatement(SQL_INSERT_LIST);

        String SQL_DELETE_LIST = "DELETE FROM lists WHERE id = ?";
        deleteListStmt = db.prepareStatement(SQL_DELETE_LIST);

        String SQL_INSERT_EXPIRE_TASK = "INSERT INTO tasks (list, expire, description, status) VALUES (?, ?, ?, 0)";
        addExpireTaskStmt = db.prepareStatement(SQL_INSERT_EXPIRE_TASK);

        String SQL_INSERT_TASK = "INSERT INTO tasks (list, description, status) VALUES (?, ?, 0)";
        addTaskStmt = db.prepareStatement(SQL_INSERT_TASK);

        String SQL_DELETE_TASK = "DELETE FROM tasks WHERE id = ?";
        deleteTaskStmt = db.prepareStatement(SQL_DELETE_TASK);

        String SQL_CHANGE_TASK_STATUS = "UPDATE tasks SET status = ? WHERE id = ?";
        updateTaskStatusStmt = db.prepareStatement(SQL_CHANGE_TASK_STATUS);
    }

    public DBAccess() throws SQLException {
        db = DBConnection.getInstance();
        createStatements();
    }

    public List<ListOfTasks> getAllLists() {
        try {
            ResultSet result = allListsStmt.executeQuery();
            List<ListOfTasks> lists = new ArrayList<>();
            while (result.next()) {
                lists.add(ListOfTasks.valueFrom(result));
            }
            return lists;
        } catch (SQLException ignored) {
        }
        return new ArrayList<>();
    }

    public synchronized List<Task> getTasksOfList(long listID) {
        try {
            tasksByListStmt.clearParameters();
            tasksByListStmt.setLong(1, listID);
            ResultSet result = tasksByListStmt.executeQuery();
            List<Task> tasks = new ArrayList<>();
            while (result.next()) {
                tasks.add(Task.valueFrom(result));
            }
            return tasks;
        } catch (SQLException ignored) {
        }

        return new ArrayList<>();
    }

    public synchronized Task getTask(long taskID) {
        try {
            taskByIdStmt.clearParameters();
            taskByIdStmt.setLong(1, taskID);
            taskByIdStmt.executeQuery();
            ResultSet result = taskByIdStmt.executeQuery();
            if (result.next()) {
                return Task.valueFrom(result);
            }
        } catch (SQLException ignored) {
        }
        return null;
    }

    public synchronized void addList(String title) throws SQLException {
        addListStmt.clearParameters();
        addListStmt.setString(1, title);
        addListStmt.execute();
    }

    public synchronized void deleteList(long listID) throws SQLException {
        deleteListStmt.clearParameters();
        deleteListStmt.setLong(1, listID);
        deleteListStmt.execute();
    }

    public synchronized void addTask(long listID, String description, Date expire) throws SQLException {
        boolean hasExpire = expire != null;
        PreparedStatement stmt = hasExpire ? addExpireTaskStmt : addTaskStmt;
        stmt.clearParameters();
        stmt.setLong(1, listID);
        if (hasExpire) {
            stmt.setTimestamp(2, new java.sql.Timestamp(expire.getTime()));
            stmt.setString(3, description);
        } else {
            stmt.setString(2, description);
        }
        stmt.execute();
    }

    public synchronized void deleteTask(long taskID) throws SQLException {
        deleteTaskStmt.clearParameters();
        deleteTaskStmt.setLong(1, taskID);
        deleteTaskStmt.execute();
    }

    public synchronized void setTaskStatus(long taskID, int status) throws SQLException {
        updateTaskStatusStmt.clearParameters();
        updateTaskStatusStmt.setInt(1, status);
        updateTaskStatusStmt.setLong(2, taskID);
        updateTaskStatusStmt.execute();
    }
}
