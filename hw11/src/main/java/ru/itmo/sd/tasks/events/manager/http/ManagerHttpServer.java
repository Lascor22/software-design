package ru.itmo.sd.tasks.events.manager.http;

import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import rx.Observable;

public interface ManagerHttpServer {
    <T> Observable<String> getResponse(HttpServerRequest<T> request);
}
