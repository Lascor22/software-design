package ru.itmo.sd.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import ru.itmo.sd.graph.io.EdgesGraphReader;
import ru.itmo.sd.graph.io.GraphReader;
import ru.itmo.sd.graph.io.MatrixGraphReader;

public enum GraphFormat {

    EDGES(EdgesGraphReader::new),
    MATRIX(MatrixGraphReader::new);

    private static final Map<String, GraphFormat> MATCHES;

    static {
        MATCHES = new HashMap<>();
        for (GraphFormat graphFormat : GraphFormat.values()) {
            MATCHES.put(graphFormat.name().toLowerCase(), graphFormat);
        }
    }

    public static GraphFormat matchOrDefault(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        return MATCHES.get(input.toLowerCase().replace(' ', '_'));
    }

    private final Supplier<GraphReader> SUPPLIER;

    GraphFormat(Supplier<GraphReader> supplier) {
        this.SUPPLIER = supplier;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace('_', ' ');
    }

    public GraphReader getInstance() {
        return SUPPLIER.get();
    }


}
