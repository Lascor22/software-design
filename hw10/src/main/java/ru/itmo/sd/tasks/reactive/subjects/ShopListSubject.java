package ru.itmo.sd.tasks.reactive.subjects;

import org.springframework.stereotype.Component;

import io.reactivex.Observer;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import ru.itmo.sd.tasks.reactive.subjects.entities.ShopListEntity;

@Component
public class ShopListSubject implements Subjector<Object> {

    private final Subject<ShopListEntity> subject = ReplaySubject.create();

    @Override
    public void subscribe(Observer<Object> observer) {
        subject.subscribe(observer);
    }

    @Override
    public void subject(Object subject) {
        this.subject.onNext((ShopListEntity) subject);
    }

}
