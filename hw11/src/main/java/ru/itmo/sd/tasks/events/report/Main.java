package ru.itmo.sd.tasks.events.report;

import ru.itmo.sd.tasks.events.dao.DaoUtils;
import io.reactivex.netty.protocol.http.server.HttpServer;
import ru.itmo.sd.tasks.events.report.http.ReportHttpServer;
import ru.itmo.sd.tasks.events.report.http.RxNettyHttpReportServer;
import rx.Observable;

public class Main {
    public static void main(String[] args) {
        ReportHttpServer server = new RxNettyHttpReportServer(DaoUtils.createDao());

        HttpServer
                .newServer(8081)
                .start((req, resp) -> {
                    Observable<String> response = server.getResponse(req);
                    return resp.writeString(response.map(r -> r + System.lineSeparator()));
                })
                .awaitShutdown();
    }
}
