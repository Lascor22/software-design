package ru.itmo.sd.tasks.docker.market;

import io.reactivex.netty.protocol.http.server.HttpServer;
import ru.itmo.sd.tasks.docker.market.http.RxNettyMarketHttpServer;
import ru.itmo.sd.tasks.docker.market.dao.DaoUtils;
import rx.Observable;

public class Main {
    public static void main(String[] args) {
        RxNettyMarketHttpServer server = new RxNettyMarketHttpServer(DaoUtils.createDao());

        HttpServer
                .newServer(8080)
                .start((req, resp) -> {
                    Observable<String> response = server.getResponse(req);
                    return resp.writeString(response.map(r -> r + System.lineSeparator()));
                })
                .awaitShutdown();
    }
}
