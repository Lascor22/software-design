package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.DBAccess;
import ru.akirakozov.sd.refactoring.db.DBLib;
import ru.akirakozov.sd.refactoring.servlet.html.HTMLBuilder;
import ru.akirakozov.sd.refactoring.utils.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public enum RequestCommand {

    MAX(pr -> orderOperation(pr, "DESC", "max")),
    MIN(pr -> orderOperation(pr, "ASC", "min")),
    SUM(pr -> aggregateOperation(pr, "SUM", "price")),
    COUNT(pr -> aggregateOperation(pr, "COUNT", "*"));

    private static void orderOperation(Pair<DBAccess, HTMLBuilder> pr, String order, String minMax) {
        try {
            String query = DBLib.selectOrderedColumn("price", order, true);
            ResultSet result = pr.first.execute(query);

            pr.second.addSmallHeader("Product with " + minMax + " price: ");
            HTMLBuilder.resultToHTML(result, pr.second, "name", "price");
        } catch (SQLException | IllegalStateException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void aggregateOperation(Pair<DBAccess, HTMLBuilder> pr,
                                           String function, String column) {
        try {
            String query = DBLib.selectAggregation(function, column);
            ResultSet result = pr.first.execute(query);

            if (result.next()) {
                pr.second.addParagraph("Result of function " + function
                        + " = " + result.getString("result"));
            }
        } catch (SQLException | IllegalStateException e) {
            throw new IllegalStateException(e);
        }
    }

    public final Consumer<Pair<DBAccess, HTMLBuilder>> C;

    RequestCommand(Consumer<Pair<DBAccess, HTMLBuilder>> function) {
        this.C = function;
    }

}
