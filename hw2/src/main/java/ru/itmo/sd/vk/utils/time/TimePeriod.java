package ru.itmo.sd.vk.utils.time;

import ru.itmo.sd.vk.utils.Pair;

import java.util.Date;

public class TimePeriod extends Pair<Date, Date> {
    public TimePeriod(Date from, Date to) {
        super(from, to);
    }

    public boolean contains(Date date) {
        return (this.first.before(date) || this.first.equals(date)) &&
                (this.second.after(date) || this.second.equals(date));
    }

    public TimePeriod swap() {
        return mtp(this.second, this.first);
    }

    public static TimePeriod mtp(Date from, Date to) {
        return new TimePeriod(from, to);
    }
}
