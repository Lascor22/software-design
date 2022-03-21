package ru.itmo.sd.tasks.events.turnstile;

import ru.itmo.sd.tasks.events.dao.DaoUtils;
import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;
import ru.itmo.sd.tasks.events.turnstile.http.RxNettyHttpTurnstileServer;
import ru.itmo.sd.tasks.events.turnstile.http.TurnstileHttpServer;

public class Main {
    public static void main(String[] args) {
        TurnstileHttpServer server = new RxNettyHttpTurnstileServer(DaoUtils.createDao());

        HttpServer
                .newServer(8082)
                .start((req, resp) -> {
                    Observable<String> response = server.getResponse(req);
                    return resp.writeString(response.map(r -> r + System.lineSeparator()));
                })
                .awaitShutdown();
    }
}
