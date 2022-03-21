package ru.itmo.sd.tasks.events.dao;

import com.mongodb.rx.client.Success;
import ru.itmo.sd.tasks.events.model.TurnstileEvent;
import ru.itmo.sd.tasks.events.model.Subscription;
import rx.Observable;

import java.time.LocalDateTime;

public interface FitnessCenterDao {
    Observable<Subscription> getSubscriptions(long subscriptionId);

    Observable<Subscription> getLatestVersionSubscription(long subscriptionId);

    Observable<Success> createSubscription(long id, LocalDateTime subscriptionEnd);

    Observable<Success> renewSubscription(long id, LocalDateTime newSubscriptionEnd);

    Observable<Success> addEvent(TurnstileEvent event);

    Observable<TurnstileEvent> getEvents();
}
