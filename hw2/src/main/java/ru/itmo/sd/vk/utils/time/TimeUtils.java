package ru.itmo.sd.vk.utils.time;

import java.util.Date;

public class TimeUtils {
    public static Date floorToHours(Date date) {
        return floorToHours(date.getTime());
    }

    public static Date floorToHours(long time) {
        long mod = 3600000L;
        return new Date(time - time % mod);
    }

    public static Date floorToDays(Date date) {
        long time = date.getTime();
        long mod = 86400000L;
        return new Date(time - time % mod);
    }
}
