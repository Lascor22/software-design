package ru.itmo.sd.vk.ntwk;

import ru.itmo.sd.vk.stcs.StatisticsData;
import ru.itmo.sd.vk.utils.time.TimePeriod;

import java.io.IOException;

public interface NetworkSession {
    /**
     * @return <i>true</i> if request can be sent to the service (etc.);
     * <i>false</i> otherwise
     */
    boolean isConnected();

    /**
     * @throws IOException in case of connection error
     */
    void tryConnect() throws IOException;

    /**
     * @param key    word that is used for searching in service API
     * @param period when required key should be searched
     * @return instance of data container
     * @throws IOException in case of request errors
     * @see StatisticsData
     */
    StatisticsData sendRequest(String key, TimePeriod period) throws IOException;
}
