package ru.akirakozov.sd.refactoring.servlet;


import ru.akirakozov.sd.refactoring.db.DBLib;
import ru.akirakozov.sd.refactoring.servlet.html.HTMLBuilder;

import java.util.Map;

public class AddProductServlet extends AbsServlet {
    private static final long serialVersionUID = -7511012126994740512L;

    public AddProductServlet() {
        super("Add new product", new String[]{
                "name", "price"
        });
    }

    @Override
    public void computeRequest(Map<String, String[]> params, HTMLBuilder html)
            throws Exception {
        long price = Long.parseLong(params.get("price")[0]);
        String name = params.get("name")[0];

        String query = DBLib.insertProduct(name, price);
        this.DB.update(query);

        html.addSmallHeader("New product added successfully");
        html.addParagraph("<b>Product:</b> " + name + " ("
                + price + " c.u.)");
    }

}
