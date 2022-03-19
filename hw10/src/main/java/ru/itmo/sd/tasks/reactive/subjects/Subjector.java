package ru.itmo.sd.tasks.reactive.subjects;

import io.reactivex.Observer;

public interface Subjector<T> {

    void subscribe(Observer<T> observer);

    void subject(T subject);

}
