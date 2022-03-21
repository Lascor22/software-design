package ru.itmo.sd.tasks.docker.account;

import ru.itmo.sd.tasks.docker.account.dao.InMemoryAccountDao;
import ru.itmo.sd.tasks.docker.account.http.RxNettyAccountHttpServer;
import ru.itmo.sd.tasks.docker.account.http.MarketHttpClient;
import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;

public class Main {
    public static void main(String[] args) {
        RxNettyAccountHttpServer server = new RxNettyAccountHttpServer(new InMemoryAccountDao(new MarketHttpClient()));

        HttpServer
                .newServer(8081)
                .start((req, resp) -> {
                    Observable<String> response = server.getResponse(req);
                    return resp.writeString(response.map(r -> r + System.lineSeparator()));
                })
                .awaitShutdown();
    }
}
