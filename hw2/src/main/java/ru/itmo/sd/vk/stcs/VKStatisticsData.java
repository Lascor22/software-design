package ru.itmo.sd.vk.stcs;

import com.google.gson.JsonObject;
import ru.itmo.sd.vk.utils.Pair;
import ru.itmo.sd.vk.utils.time.TimePeriod;
import ru.itmo.sd.vk.utils.time.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VKStatisticsData implements StatisticsData {

    private final List<JsonObject> POSTS;
    private final TimePeriod PERIOD;
    private final String KEY;

    public VKStatisticsData(String key, TimePeriod period, List<JsonObject> wallPosts) {
        if (key == null || period == null || wallPosts == null) {
            throw new IllegalArgumentException("Arguments of " + this.getClass().getSimpleName() + " can't be NULL");
        }

        this.PERIOD = TimePeriod.mtp(TimeUtils.floorToHours(period.first), period.second);
        this.POSTS = new ArrayList<>(wallPosts);
        this.KEY = key;
    }

    @Override
    public List<Pair<Date, Integer>> getUsages(DataComposer<Date, Integer> composer) {
        Function<JsonObject, Date> toDate =
                p -> new Date(p.getAsJsonPrimitive("date").getAsLong() * 1000);
        List<Date> dates = POSTS.stream().map(toDate)
                .collect(Collectors.toList());
        return composer.compose(dates);
    }

    @Override
    public String getRequestKey() {
        return this.KEY;
    }

    @Override
    public TimePeriod getPeriod() {
        return this.PERIOD;
    }

    @Override
    public String getSource() {
        return "vk.com";
    }
}
