package ru.itmo.sd.vk.test;

import com.xebialabs.restito.server.StubServer;
import org.apache.mina.core.RuntimeIoException;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.itmo.sd.vk.ntwk.FakeTwitterSession;
import ru.itmo.sd.vk.ntwk.NetworkSession;
import ru.itmo.sd.vk.ntwk.VKSession;
import ru.itmo.sd.vk.stcs.HoursComposer;
import ru.itmo.sd.vk.stcs.StatisticsData;
import ru.itmo.sd.vk.stcs.VKStatisticsData;
import ru.itmo.sd.vk.utils.Pair;
import ru.itmo.sd.vk.utils.PropertiesLoader;
import ru.itmo.sd.vk.utils.time.TimeDelta;
import ru.itmo.sd.vk.utils.time.TimeDelta.TDUnit;
import ru.itmo.sd.vk.utils.time.TimePeriod;
import ru.itmo.sd.vk.utils.time.TimeUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.method;
import static org.glassfish.grizzly.http.Method.POST;
import static org.junit.jupiter.api.Assertions.*;

public class TestUnit {

    private final Random RANDOM = new Random();

    static {
        PropertiesLoader.load("src/main/resources/properties");
    }

    @Nested
    public class TestTimeLibrary {

        @Nested
        public class TestTimeUtils {

            @RepeatedTest(16)
            public void testTimeFloor() {
                long hourLength = 1000 * 60 * 60,
                        hours = RANDOM.nextInt(10000) * hourLength;

                Date date = new Date(hours + RANDOM.nextInt((int) hourLength));
                assertEquals(hours, TimeUtils.floorToHours(date).getTime());

                date = new Date(hours + (1000 * 60 * 60 - 1));
                assertEquals(hours, TimeUtils.floorToHours(date).getTime());

                date = new Date(hours + 1000 * 60 * 60);
                assertEquals(hours + hourLength, TimeUtils.floorToHours(date).getTime());
            }

        }

        @Nested
        public class TestTimeDelta {

            @RepeatedTest(8)
            public void testTimeDeltaInit() {
                long delta;
                assertEquals(Math.abs(delta = RANDOM.nextLong()),
                        TimeDelta.valueOf(delta).getLength());

                int days = 1 + RANDOM.nextInt(5);
                TimeDelta dt = TimeDelta.valueOf(TDUnit.DAY, days);
                assertEquals(days * 24 * 60 * 60 * 1000, dt.getLength());
            }

            @RepeatedTest(8)
            public void testTimeDeltaInitFromDate() {
                Date from = new Date(1000 + RANDOM.nextInt(10000)),
                        to = new Date(from.getTime() + 45364);
                TimeDelta delta = TimeDelta.deltaOf(from, to);
                assertEquals(45364, delta.getLength());

                int randomDelta = RANDOM.nextInt();
                from = new Date(1000 + RANDOM.nextInt(10000));
                to = new Date(from.getTime() + randomDelta);
                delta = TimeDelta.deltaOfPeriod(new Pair<>(from, to));

                assertEquals(Math.abs(randomDelta), delta.getLength());
            }

            @RepeatedTest(32)
            public void testTimeDeltaFloorGet() {
                int random = RANDOM.nextInt();
                TimeDelta deltaMLS = TimeDelta.valueOf(TDUnit.MLS, random);
                assertEquals(random, deltaMLS.floorTo(TDUnit.MLS));

                random = RANDOM.nextInt();
                TimeDelta deltaSEC = TimeDelta.valueOf(TDUnit.SEC, random);
                assertEquals(random, deltaSEC.floorTo(TDUnit.SEC));

                random = RANDOM.nextInt();
                TimeDelta deltaMIN = TimeDelta.valueOf(TDUnit.MIN, random);
                assertEquals(random, deltaMIN.floorTo(TDUnit.MIN));

                random = RANDOM.nextInt();
                TimeDelta deltaHWR = TimeDelta.valueOf(TDUnit.HWR, random);
                assertEquals(random, deltaHWR.floorTo(TDUnit.HWR));

                random = RANDOM.nextInt();
                TimeDelta deltaDAY = TimeDelta.valueOf(TDUnit.DAY, random);
                assertEquals(random, deltaDAY.floorTo(TDUnit.DAY));
            }

            @RepeatedTest(8)
            public void testTimeDeltaGet() {
                int random = RANDOM.nextInt();
                TimeDelta deltaMLS = TimeDelta.valueOf(TDUnit.MLS, random);
                assertEquals(random % TDUnit.MLS.CAPACITY, deltaMLS.get(TDUnit.MLS));

                random = RANDOM.nextInt();
                TimeDelta deltaSEC = TimeDelta.valueOf(TDUnit.SEC, random);
                assertEquals(random % TDUnit.SEC.CAPACITY, deltaSEC.get(TDUnit.SEC));

                random = RANDOM.nextInt();
                TimeDelta deltaMIN = TimeDelta.valueOf(TDUnit.MIN, random);
                assertEquals(random % TDUnit.MIN.CAPACITY, deltaMIN.get(TDUnit.MIN));

                random = RANDOM.nextInt();
                TimeDelta deltaHWR = TimeDelta.valueOf(TDUnit.HWR, random);
                assertEquals(random % TDUnit.HWR.CAPACITY, deltaHWR.get(TDUnit.HWR));
            }

            @RepeatedTest(16)
            public void testTimeDeltaAdd() {
                TimeDelta delta = new TimeDelta();
                long length = delta.getLength();

                int dt = RANDOM.nextInt(4536756);
                delta.add(TDUnit.MLS, dt);
                assertEquals(dt, delta.getLength() - length);

                TimeDelta delta2 = new TimeDelta(RANDOM.nextInt());
                delta.add(delta2, false);
                delta.add(delta2);

                assertEquals(dt, delta.getLength() - length);

                int dt2 = RANDOM.nextInt(92319832);
                delta.add(TDUnit.MLS, dt2);
                assertEquals(dt + dt2, delta.getLength() - length);
            }

            @Test
            public void testTimeDeltaToString() {
                TimeDelta delta = new TimeDelta(0);
                delta.add(TDUnit.MLS, 56).add(TDUnit.SEC, 55).add(TDUnit.MIN, 19)
                        .add(TDUnit.HWR, 6).add(TDUnit.DAY, 567);

                assertEquals("dt = +56 MLS, 55 SEC, 19 MIN, 6 HWR, 567 DAY", delta.toString());
            }

            @RepeatedTest(16)
            public void testTimeDeltaToPeriod() {
                TimeDelta delta = TimeDelta.valueOf(RANDOM.nextInt());

                int dateTime = RANDOM.nextInt();
                Date date = new Date(dateTime);

                TimePeriod period = delta.getPeriodFor(date);

                long periodLength = TimeDelta.deltaOfPeriod(period).getLength();
                assertEquals(delta.getLength(), periodLength);
            }

            @Test
            public void testTimeDeltaOnErrors() {
                TimeDelta delta = TimeDelta.valueOf(RANDOM.nextInt());
                try {
                    delta.floorTo(null);
                } catch (Exception | AssertionError ignored) {
                }

                try {
                    delta.add(null, 0);
                } catch (Exception | AssertionError e) {
                    return;
                }

                fail("Exception was expected on null argument");
            }
        }

        @Nested
        public class TestTimePeriod {

            @Test
            public void testTimePeriodSwap() {
                Date from = new Date(RANDOM.nextInt(4567)),
                        to = new Date(320300 + RANDOM.nextInt(56766456));

                TimePeriod period = TimePeriod.mtp(from, to), swap = period.swap().swap();
                assertEquals(period.first, swap.first);
                assertEquals(period.second, swap.second);
            }
        }
    }

    @Nested
    public class TestProvider {

        @Nested
        public class TestComposer {

            @Test
            public void testHoursComposer() {
                HoursComposer composer = new HoursComposer();

                long hours = composer.getTimeBounds().get(TDUnit.HWR);
                assertEquals(1, hours);

                long hourLength = 1000 * 60 * 60;

                List<Date> usages = new ArrayList<>(Arrays.asList(
                        new Date(54), new Date(hourLength + 657), new Date(hourLength * 2 + 454),
                        new Date(hourLength * 3 + 5657), new Date(934), new Date(hourLength),
                        new Date(hourLength + 567), new Date(76), new Date(hourLength * 2)
                ));
                List<Pair<Date, Integer>> list = composer.compose(usages);
                assertEquals(4, list.size());

                assertEquals(3, list.get(0).second.intValue());
                assertEquals(3, list.get(1).second.intValue());
                assertEquals(2, list.get(2).second.intValue());
                assertEquals(1, list.get(3).second.intValue());
            }
        }

        @Test
        public void testProviderGetKey() {
            TimePeriod period = new TimePeriod(new Date(), new Date());

            StatisticsData provider = new VKStatisticsData("Some key", period, new ArrayList<>());
            assertEquals("Some key", provider.getRequestKey());
        }

        @Test
        public void testProviderGetPeriod() {
            TimePeriod period = new TimePeriod(new Date(), new Date());
            Date from = TimeUtils.floorToHours(period.first);

            StatisticsData provider = new VKStatisticsData("Some key", period, new ArrayList<>());
            TimePeriod period2 = provider.getPeriod();

            assertEquals(from, period2.first);
            assertEquals(period.second, period2.second);
        }
    }

    @Nested
    public class TestNetwork {

        @Nested
        public class TestTwitterNetwork {

            @Test
            @SuppressWarnings("static-access")
            public void testConnectionWithStubServer() {
                withStubServer(s -> {
                    byte[] toEncode = String.join(":", "test", "past").getBytes(StandardCharsets.UTF_8);
                    String basic = Base64.getEncoder().encodeToString(toEncode);

                    whenHttp(s).match(
                            method(POST).startsWithUri("/authorize")
                                    .withHeader("authorization", "Basic " + basic)
                                    .withHeader("accept", "application/json")
                    ).then(status(HttpStatus.ACCEPTED_202));
					
					/*
					DateFormat format = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z YYYY", Locale.ENGLISH);
					*/

                    NetworkSession session = new FakeTwitterSession("test");
                    try {
                        session.tryConnect();
                        if (!session.isConnected()) {
                            throw new IllegalStateException("not connected");
                        }
                    } catch (IOException ioe) {
                        throw new RuntimeIoException(ioe);
                    }
                });
            }

            private void withStubServer(Consumer<StubServer> callback) {
                StubServer server = new StubServer(22041);
                try {
                    callback.accept(server.run());
                } finally {
                    server.stop();
                }
            }
        }

        @Nested
        public class TestVKNetwork {

            private final NetworkSession SESSION = new VKSession("azat_karimow");

            @Test
            public void testConnection() {
                try {
                    SESSION.tryConnect();
                    if (!SESSION.isConnected()) {
                        throw new IllegalStateException("not connected");
                    }
                } catch (IOException e) {
                    throw new RuntimeIoException(e);
                }
            }

            @Test
            public void testRequest() {
                try {
                    SESSION.tryConnect();

                    Date from = new Date(System.currentTimeMillis() - 3254 - RANDOM.nextInt(3294834));
                    TimePeriod period = TimePeriod.mtp(from, new Date());

                    StatisticsData provider = SESSION.sendRequest("vk", period);
                    assertNotNull(provider);

                    List<Pair<Date, Integer>> list = provider.getUsages(new HoursComposer());
                    for (Pair<Date, Integer> dateIntegerPair : list) {
                        if (dateIntegerPair.second <= 0) {
                            fail("Zero or negative entry");
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeIoException(e);
                } catch (IllegalStateException e) {
                    // It means that no connection failed
                }
            }
        }
    }
}
