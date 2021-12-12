package ru.itmo.sd.graph.utils;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StreamUtils {
    public static <I, O> Stream<O> whilst(Predicate<I> condition, Function<I, O> step, I obj) {
        Stream.Builder<O> builder = Stream.builder();
        while (condition.test(obj)) {
            O tmp = step.apply(obj);
            builder.add(tmp);
        }

        return builder.build();
    }
}
