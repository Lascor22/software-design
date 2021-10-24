package ru.itmo.sd.vk;

import ru.itmo.sd.vk.ntwk.NetworkSession;
import ru.itmo.sd.vk.ntwk.VKSession;
import ru.itmo.sd.vk.stcs.HoursComposer;
import ru.itmo.sd.vk.stcs.StatisticsData;
import ru.itmo.sd.vk.utils.Pair;
import ru.itmo.sd.vk.utils.PropertiesLoader;
import ru.itmo.sd.vk.utils.time.TimeDelta;
import ru.itmo.sd.vk.utils.time.TimeDelta.TDUnit;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String... args) throws IOException {
        PropertiesLoader.load("src/main/resources/properties");

        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("First argument missed: [hashtag]");
        }

        if (args.length < 2) {
            throw new IllegalArgumentException("Second argument missed: [period in hours]");
        }

        String hashtag = args[0];
        int hours;

        try {
            hours = Math.abs(Integer.parseInt(args[1]));
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("Second argument [period in hours] must be a number");
        }

        TimeDelta delta = TimeDelta.valueOf(TDUnit.HWR, -hours);
        NetworkSession session = new VKSession("azat_karimow");
        session.tryConnect();

        if (session.isConnected()) {
            StatisticsData provider = session.sendRequest(hashtag, delta.getPeriod());
            List<Pair<Date, Integer>> usages = provider.getUsages(new HoursComposer());
            for (Pair<Date, Integer> usage : usages) {
                System.out.println(usage.first + " " + usage.second);
            }
        } else {
            System.err.println("Not connected");
        }
    }
}
