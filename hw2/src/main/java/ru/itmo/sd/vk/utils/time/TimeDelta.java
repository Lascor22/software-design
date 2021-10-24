package ru.itmo.sd.vk.utils.time;

import ru.itmo.sd.vk.utils.Pair;

import java.util.Date;

public class TimeDelta {
    private long milliseconds;

    public static TimeDelta valueOf(long milliseconds) {
        return new TimeDelta(milliseconds);
    }

    public static TimeDelta valueOf(TimeDelta.TDUnit unit, int amount) {
        return (new TimeDelta(0L)).add(unit, amount);
    }

    public static TimeDelta deltaOf(Date from, Date to) {
        long delta = to.getTime() - from.getTime();
        return valueOf(delta);
    }

    public static TimeDelta deltaOfPeriod(Pair<Date, Date> period) {
        return deltaOf(period.first, period.second);
    }

    public TimeDelta() {
        this(System.currentTimeMillis());
    }

    public TimeDelta(long milliseconds) {
        this.milliseconds = 0L;
        this.milliseconds = milliseconds;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("dt = ");
        sb.append(this.milliseconds >= 0L ? "+" : "-");
        int length = TimeDelta.TDUnit.values().length - 1;
        long rest = this.milliseconds;

        for (int i = 0; i < length; ++i) {
            TimeDelta.TDUnit unit = TimeDelta.TDUnit.values()[i];
            long value = Math.abs(rest % unit.CAPACITY);
            sb.append(value).append(" ").append(unit).append(", ");
            rest /= unit.CAPACITY;
        }

        sb.append(rest).append(" ").append(TimeDelta.TDUnit.values()[length]);
        return sb.toString();
    }

    public long getLength() {
        return Math.abs(this.milliseconds);
    }

    public long floorTo(TimeDelta.TDUnit unit) {
        if (unit == null) {
            String text = "TDUnit can't be NULL";
            throw new IllegalArgumentException(text);
        } else {
            long result = this.milliseconds;

            for (int i = 0; i < unit.ordinal(); ++i) {
                result /= TimeDelta.TDUnit.values()[i].CAPACITY;
            }

            return result;
        }
    }

    public long get(TimeDelta.TDUnit unit) {
        boolean isLast = TimeDelta.TDUnit.values().length - 1 == unit.ordinal();
        return this.floorTo(unit) % (isLast ? 9223372036854775807L : unit.CAPACITY);
    }

    public TimePeriod getPeriod() {
        return this.getPeriodFor(new Date());
    }

    public TimePeriod getPeriodFor(Date from) {
        TimePeriod period = TimePeriod.mtp(from, new Date(from.getTime() + this.milliseconds));
        if (this.milliseconds < 0L) {
            period = period.swap();
        }

        return period;
    }

    public TimeDelta add(TimeDelta.TDUnit unit, int amount) {
        if (unit == null) {
            String text = "TDUnit can't be NULL";
            throw new IllegalArgumentException(text);
        } else {
            TimeDelta.TDUnit current = unit;

            long delta;
            TimeDelta.TDUnit less;
            for (delta = amount; current.ordinal() > 0; current = less) {
                less = TimeDelta.TDUnit.values()[current.ordinal() - 1];
                delta *= less.CAPACITY;
            }

            this.milliseconds += delta;
            return this;
        }
    }

    public void add(TimeDelta delta) {
        this.add(delta, true);
    }

    public void add(TimeDelta delta, boolean positive) {
        this.milliseconds += (long) (positive ? 1 : -1) * delta.milliseconds;
    }

    public enum TDUnit {
        MLS(1000L),
        SEC(60L),
        MIN(60L),
        HWR(24L),
        DAY(0L);

        public final long CAPACITY;

        TDUnit(long capacity) {
            this.CAPACITY = capacity;
        }
    }
}
