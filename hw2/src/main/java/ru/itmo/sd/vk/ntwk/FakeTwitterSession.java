package ru.itmo.sd.vk.ntwk;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import ru.itmo.sd.vk.stcs.StatisticsData;
import ru.itmo.sd.vk.utils.time.TimePeriod;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FakeTwitterSession implements NetworkSession {

    private final String LOGIN, PASSWORD;
    private boolean isConnected = false;

    public FakeTwitterSession(String profile) {
        this.PASSWORD = System.getProperty("api.twitter." + profile + ".password");
        this.LOGIN = System.getProperty("api.twitter." + profile + ".login");
    }

    @Override
    public boolean isConnected() {
        return this.isConnected;
    }

    @Override
    public void tryConnect() throws IOException {
        try {
            byte[] toEncode = String.join(":", LOGIN, PASSWORD).getBytes(StandardCharsets.UTF_8);
            String basic = Base64.getEncoder().encodeToString(toEncode);

            HttpResponse<JsonNode> response = Unirest
                    .post("http://localhost:22041/authorize")
                    .header("authorization", "Basic " + basic)
                    .header("accept", "application/json")
                    .asJson();
            this.isConnected = response.getStatus() == 202;
        } catch (UnirestException ue) {
            throw new IOException(ue);
        }
    }

    @Override
    public StatisticsData sendRequest(String key, TimePeriod period) {
        return null;
    }
}
