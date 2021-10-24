package ru.akirakozov.sd.refactoring.servlet;

import ru.akirakozov.sd.refactoring.servlet.html.HTMLBuilder;
import ru.akirakozov.sd.refactoring.utils.Pair;

import java.util.Map;

public class QueryServlet extends AbsServlet {
    private static final long serialVersionUID = -4442168039401537019L;

    public QueryServlet() {
        super("Query execution result", new String[]{
                "command"
        });
    }

    @Override
    public void computeRequest(Map<String, String[]> params, HTMLBuilder html) {
        String command = params.get("command")[0].toUpperCase();
        RequestCommand en = RequestCommand.valueOf(command);
        en.C.accept(new Pair<>(this.DB, html));
    }

}
