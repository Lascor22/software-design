package ru.itmo.sd.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import ru.itmo.sd.graph.gfx.AWTGraphRender;
import ru.itmo.sd.graph.gfx.GraphRender;
import ru.itmo.sd.graph.gfx.JavaFXGraphRender;

public enum RenderType {

    AWT(() -> new AWTGraphRender(800, 600)),
    JAVAFX(() -> new JavaFXGraphRender(800, 600));

    private static final Map<String, RenderType> MATCHES;

    static {
        MATCHES = new HashMap<>();
        for (RenderType renderType : RenderType.values()) {
            MATCHES.put(renderType.name().toLowerCase(), renderType);
        }
    }

    public static RenderType matchOrDefault(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        return MATCHES.get(input.toLowerCase().replace(' ', '_'));
    }

    private final Supplier<GraphRender> SUPPLIER;

    RenderType(Supplier<GraphRender> supplier) {
        this.SUPPLIER = supplier;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace('_', ' ');
    }

    public void getInstance() {
        SUPPLIER.get();
    }

}
