package ru.itmo.sd.graph.graph;

import static java.lang.Math.*;
import static ru.itmo.sd.graph.utils.ColorUtils.getSpectrumColor;

import java.awt.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ru.itmo.sd.graph.gfx.GraphRender;
import ru.itmo.sd.graph.utils.Pair;

public class Graph {

    private final List<Pair<Integer, Integer>> edges = new ArrayList<>();
    private final Set<Integer> vertexes = new HashSet<>();
    private boolean isOrientated = false;

    public void addVertex(int vertex) {
        if (vertexes.contains(vertex)) {
            System.err.println("Vertex already exists");
        }
        vertexes.add(vertex);
    }

    public void addEdge(int from, int to) {
        if (!vertexes.contains(from)) {
            System.err.println("Vertex (from) " + from + " doesn't exist");
        }
        if (!vertexes.contains(to)) {
            System.out.println("Vertex (to) " + to + " doesn't exist");
        }
        edges.add(new Pair<>(from, to));
    }

    public void render(GraphRender render) {
        Map<Integer, Pair<Double, Double>> positions = new HashMap<>();
        List<Integer> vertexes = new ArrayList<>(this.vertexes);
        Collections.sort(vertexes);
        double r = 25.0;
        render.clear();

        Random random = new Random();
        for (Integer vertex : vertexes) {
            double angle = getRadiansFor(vertex, vertexes.size());
            double radius = r + random.nextInt((int) r * 10);
            double x = Math.cos(angle) * radius;
            double y = Math.sin(angle) * radius;
            positions.put(vertex, new Pair<>(x, y));
        }

        // for not orientated
        render.setStroke(new Color(1.0f, 0, 0.25f, 0.55f));
        render.setLineWidth(3d);
        for (Pair<Integer, Integer> edge : edges) {
            if (!positions.containsKey(edge.getFirst()) || !positions.containsKey(edge.getSecond())) {
                continue;
            }
            Pair<Double, Double> from = positions.get(edge.getFirst());
            Pair<Double, Double> to = positions.get(edge.getSecond());
            render.strokeLine(from.getFirst(), from.getSecond(), to.getFirst(), to.getSecond());
        }

        //render.setFill (new Color (1.0f, 0f, 0f, 0.65f));
        double maxRadius = r * 11 * r * 11;
        for (Integer vertex : vertexes) {
            Pair<Double, Double> v = positions.get(vertex);
            double radius = v.getFirst() * v.getFirst() + v.getSecond() * v.getSecond();
            Color color = getSpectrumColor(radius / maxRadius, atan2(v.getFirst(), v.getSecond()));
            render.setFill(color);
            render.fillCircle(v.getFirst(), v.getSecond(), r);
        }
    }

    private double getRadiansFor(int number, int total) {
        return (2 * Math.PI / total) * number;
    }

    public boolean isOrientated() {
        return isOrientated;
    }

    public void setOrientated() {
        isOrientated = true;
    }

    @Override
    public String toString() {
        return "Vertexes: " + vertexes + "\n" +
                "Edges: " + edges + "\n" +
                "Orientated: " + isOrientated();
    }
}
