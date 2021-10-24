package ru.itmo.sd.vk.stcs;

import ru.itmo.sd.vk.utils.Pair;

import java.util.List;

public interface DataComposer<T, N extends Number> extends HasTimePeriod {
    List<Pair<T, N>> compose(List<T> data);
}
