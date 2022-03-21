package ru.itmo.sd.tasks.events.turnstile.http;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import rx.Observable;

public interface TurnstileHttpServer {
    <T> Observable<String> getResponse(HttpServerRequest<T> request);
}
