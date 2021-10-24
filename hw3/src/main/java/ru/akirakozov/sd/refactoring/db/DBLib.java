package ru.akirakozov.sd.refactoring.db;

public class DBLib {

    public static String createTable() {
        return "CREATE TABLE IF NOT EXISTS `products` (" +
                "`id`    INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "`name`  TEXT NOT NULL UNIQUE," +
                "`price` INT  NOT NULL" +
                ")";
    }

    public static String insertProduct(String name, long price) {
        return "INSERT " +
                "INTO `products` (`name`, `price`) " +
                "VALUES ('" + name +
                "', '" + price + "')";
    }

    public static String selectOrderedColumn(String column, String order, boolean limited) {
        return "SELECT * " +
                "FROM `products` " +
                "ORDER BY `" + column + "` " +
                order + " " +
                (limited ? "LIMIT 1" : "");
    }

    public static String selectAggregation(String function, String column) {
        return "SELECT " + function +
                "(" + column + ") AS 'result' " +
                "FROM `products`";
    }

}
