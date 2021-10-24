package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.db.DBLib;
import ru.akirakozov.sd.refactoring.servlet.html.HTMLBuilder;

import java.sql.ResultSet;
import java.util.Map;

public class GetProductsServlet extends AbsServlet {
    private static final long serialVersionUID = -500173686782666575L;

    public GetProductsServlet() {
        super("List of products", new String[]{});
    }

    @Override
    public void computeRequest(Map<String, String[]> params, HTMLBuilder html)
            throws Exception {
        String query = DBLib.selectOrderedColumn("name", "ASC", false);
        ResultSet result = DB.execute(query);

        html.addSmallHeader("Products in database: ");
        HTMLBuilder.resultToHTML(result, html, "name", "price");
    }

}
