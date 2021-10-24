package ru.itmo.sd.vk.stcs;

import ru.itmo.sd.vk.utils.time.TimeDelta;

public interface HasTimePeriod {
    TimeDelta getTimeBounds();
}

