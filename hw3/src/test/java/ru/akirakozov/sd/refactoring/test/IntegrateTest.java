package ru.akirakozov.sd.refactoring.test;

import org.junit.jupiter.api.*;
import ru.akirakozov.sd.refactoring.db.DBAccess;
import ru.akirakozov.sd.refactoring.db.DBLib;
import ru.akirakozov.sd.refactoring.servlet.AddProductServlet;
import ru.akirakozov.sd.refactoring.servlet.GetProductsServlet;
import ru.akirakozov.sd.refactoring.servlet.QueryServlet;
import ru.akirakozov.sd.refactoring.servlet.html.HTMLBuilder;

import java.io.File;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrateTest {

    public static final Random RANDOM = new Random();
    public static final String TEMP_FILE = "temp.ru.itmo.sd.db";

    @BeforeAll
    public static void init() {
        System.setProperty("shop.ru.itmo.sd.db.url", TEMP_FILE);
    }

    @Nested
    public class TestDB {

        @Test
        public void testConnection() throws Exception {
            DBAccess db = DBAccess.getInstanceOf();
            db.close();
        }

        @RepeatedTest(2)
        public void testCreateTable() throws Exception {
            DBAccess db = DBAccess.getInstanceOf();
            db.update(DBLib.createTable());

            // Repeated action to check stability on existing table
            db.update(DBLib.createTable());

            // Clearing database for any cases
            db.update("DROP TABLE `products`");
            db.close();
        }

        @RepeatedTest(2)
        public void testCountRows() throws Exception {
            DBAccess db = DBAccess.getInstanceOf();
            db.update(DBLib.createTable());

            int rows = 10 + RANDOM.nextInt(27);
            for (int i = 1; i <= rows; i++) {
                db.update(DBLib.insertProduct("test" + i, i + 1));
            }

            ResultSet result = db.execute(DBLib.selectAggregation("COUNT", "*"));
            assertEquals("" + rows, result.getString("result"));
            db.close();

            db = DBAccess.getInstanceOf();
            // Clearing database for any cases
            db.update("DROP TABLE `products`");
            db.close();
        }

        @Test
        public void testGetMaxAndMin() throws Exception {
            DBAccess db = DBAccess.getInstanceOf();
            db.update(DBLib.createTable());

            int rows = 10 + RANDOM.nextInt(24);
            for (int i = 1; i <= rows; i++) {
                db.update(DBLib.insertProduct("test" + i, i + 1));
            }

            ResultSet result = db.execute(DBLib.selectOrderedColumn("price", "DESC", true));
            assertEquals("test" + rows, result.getString("name"));

            result = db.execute(DBLib.selectOrderedColumn("price", "ASC", true));
            assertEquals("test1", result.getString("name"));
            db.close();

            db = DBAccess.getInstanceOf();
            // Clearing database for any cases
            db.update("DROP TABLE `products`");
            db.close();
        }

    }

    @Nested
    public class TestServlets {

        @Test
        public void testAddProduct() throws Exception {
            AddProductServlet add = new AddProductServlet();

            DBAccess db = DBAccess.getInstanceOf();
            db.update(DBLib.createTable());

            int rows = 5 + RANDOM.nextInt(23),
                    defaultLength = new HTMLBuilder("test")
                            .toString().length();

            for (int i = 0; i < rows; i++) {
                Map<String, String[]> params = new HashMap<>();
                params.put("name", new String[]{"test" + i});
                params.put("price", new String[]{"" + (i + 1)});

                HTMLBuilder builder = new HTMLBuilder("test");
                add.computeRequest(params, builder);

                assertTrue(builder.toString().length() > defaultLength);
            }

            ResultSet result = db.execute(DBLib.selectAggregation("COUNT", "*"));
            assertEquals("" + rows, result.getString("result"));
            db.close();
        }

        @Test
        public void testGetProducts() throws Exception {
            testAddProduct();

            GetProductsServlet get = new GetProductsServlet();
            HTMLBuilder builder = new HTMLBuilder("test");

            get.computeRequest(new HashMap<>(), builder);
            assertTrue(builder.toString().contains("table"));

            DBAccess db = DBAccess.getInstanceOf();
            // Clearing database for any cases
            db.update("DROP TABLE `products`");
            db.close();
        }

        @Test
        public void testQuery() throws Exception {
            testAddProduct();

            QueryServlet query = new QueryServlet();
            HTMLBuilder builder = new HTMLBuilder("page title");

            Map<String, String[]> params = new HashMap<>();
            params.put("command", new String[]{"min"});

            query.computeRequest(params, builder);

            String html = builder.toString();
            assertTrue(html.contains("test0"));
            assertTrue(html.contains("1"));

            DBAccess db = DBAccess.getInstanceOf();
            // Clearing database for any cases
            db.update("DROP TABLE `products`");
            db.close();
        }

    }

    @AfterAll
    public static void clear() {
        File db = new File(TEMP_FILE);
        if (db.exists()) {
            db.delete();
        }
    }

}
