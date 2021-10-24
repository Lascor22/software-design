package ru.akirakozov.sd.refactoring;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.akirakozov.sd.refactoring.db.DBAccess;
import ru.akirakozov.sd.refactoring.db.DBLib;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.servlet.ResourceServlet;
import ru.akirakozov.sd.refactoring.utils.Pair;
import ru.akirakozov.sd.refactoring.utils.PropertiesLoader;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.List;

import static org.eclipse.jetty.servlet.ServletContextHandler.SESSIONS;

public class Main {

    static {
        PropertiesLoader.load("src/main/resources/properties");
    }

    private static List<Pair<String, Servlet>> SERVLETS = new ArrayList<>();

    static {
        SERVLETS.add(new Pair<>("/add-product", new AddProductServlet()));
        SERVLETS.add(new Pair<>("/get-products", new GetProductsServlet()));
        SERVLETS.add(new Pair<>("/query", new QueryServlet()));
        SERVLETS.add(new Pair<>("/src/*", new ResourceServlet()));
    }

    public static void main(String... args) throws Exception {
        DBAccess db = DBAccess.getInstanceOf();
        db.update(DBLib.createTable());

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(SESSIONS);
        SERVLETS.forEach(p -> context.addServlet(new ServletHolder(p.second), p.first));
        context.setContextPath("/");
        server.setHandler(context);

        server.start();
        server.join();

        db.close();
    }
}
