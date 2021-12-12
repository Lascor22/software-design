package ru.itmo.sd.graph;

import java.util.Arrays;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import ru.itmo.sd.graph.gfx.GraphRender;
import ru.itmo.sd.graph.graph.Graph;
import ru.itmo.sd.graph.io.GraphReader;
import ru.itmo.sd.graph.io.Parameter;
import ru.itmo.sd.graph.io.ParametersData;

public class RunGraphLayout {

    private static Graph graph;

    public static void main(String... args) throws Exception {
        ParametersData parametersData = ParametersData.parse(args);
        requireNotNullArgs(parametersData);

        GraphFormat format = GraphFormat.matchOrDefault(parametersData.getValue(Parameter.FORMAT));
        if (format == null) {
            String msg = "Unknown format of graph. Valid values: " + Arrays.toString(GraphFormat.values());
            throw new MissingFormatArgumentException(msg);
        }

        RenderType renderType = RenderType.matchOrDefault(parametersData.getValue(Parameter.DRAWING_API));
        if (renderType == null) {
            String msg = "Unknown type of render type. Valid values: " + Arrays.toString(RenderType.values());
            throw new MissingFormatArgumentException(msg);
        }

        GraphReader reader = format.getInstance();
        String file = parametersData.getValue(Parameter.GRAPH_FILE);
        graph = reader.read(file);
        renderType.getInstance();
    }

    private static void requireNotNullArgs(ParametersData parametersData) {
        List<Parameter> missed = Arrays.stream(Parameter.values())
                .filter(p -> p.IS_REQUIRED && parametersData.getValue(p) == null)
                .collect(Collectors.toList());
        if (missed.size() > 0) {
            StringJoiner sj = new StringJoiner(", ");
            missed.forEach(p -> sj.add("[" + p.KEY + " = " + p + "]"));
            throw new MissingFormatArgumentException("Missed arguments: " + sj);
        }
    }

    public static void render(GraphRender render) {
        graph.render(render);
    }

}
