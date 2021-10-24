package ru.itmo.sd.vk.stcs;

import ru.itmo.sd.vk.utils.Pair;
import ru.itmo.sd.vk.utils.time.TimePeriod;

import java.util.Date;
import java.util.List;

public interface StatisticsData {
    /**
     * @param composer instance of class that organize data to groups
     * @return list of data from request that is organized to groups
     */
    List<Pair<Date, Integer>> getUsages(DataComposer<Date, Integer> composer);

    /**
     * @return source of data (request service destination)
     */
    String getSource();

    /**
     * @return key that is used for searching in service
     */
    String getRequestKey();

    /**
     * @return time period when {@link #getRequestKey () key} would be searched
     */
    TimePeriod getPeriod();
}
