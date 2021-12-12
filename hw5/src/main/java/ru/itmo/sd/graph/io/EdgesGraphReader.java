package ru.itmo.sd.graph.io;

import java.util.List;

import ru.itmo.sd.graph.graph.Graph;

public class EdgesGraphReader extends AbsGraphReader {

    @Override
    public void readUnknownLine(Graph graph, List<String> values) {
        int from = Integer.parseInt(values.get(0));
        int to = Integer.parseInt(values.get(1));
        graph.addEdge(from, to);
    }

}
