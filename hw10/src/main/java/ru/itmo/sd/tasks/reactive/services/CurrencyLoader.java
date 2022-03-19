package ru.itmo.sd.tasks.reactive.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.itmo.sd.tasks.reactive.db.CurrencyEntity;
import ru.itmo.sd.tasks.reactive.db.CurrencyEntityRepository;
import ru.itmo.sd.tasks.reactive.db.CurrencyQuatationsEntity;
import ru.itmo.sd.tasks.reactive.db.CurrencyQuatationsEntityRepository;
import ru.itmo.sd.tasks.reactive.subjects.RegisterSubject;
import ru.itmo.sd.tasks.reactive.subjects.ShopListSubject;
import ru.itmo.sd.tasks.reactive.subjects.UpdateSubject;
import ru.itmo.sd.tasks.reactive.subjects.entities.RegisterEntity.RegisterUserCurrency;
import ru.itmo.sd.tasks.reactive.subjects.entities.RegisterEntity.RegisterUserRequest;
import ru.itmo.sd.tasks.reactive.subjects.entities.ShopListEntity.ShopListCurrency;
import ru.itmo.sd.tasks.reactive.subjects.entities.ShopListEntity.ShopListRequest;
import ru.itmo.sd.tasks.reactive.subjects.entities.UpdateEntity.UpdateUserCurrency;
import ru.itmo.sd.tasks.reactive.subjects.entities.UpdateEntity.UpdateUserRequest;

@Service
public class CurrencyLoader implements Observer<Object> {

    private final CurrencyQuatationsEntityRepository currencyQuatationsEntityRepository;
    private final CurrencyEntityRepository currencyEntityRepository;
    private final RegisterSubject registerSubject;
    private final ShopListSubject shopListSubject;
    private final UpdateSubject updateSubject;

    public CurrencyLoader(
            CurrencyQuatationsEntityRepository currencyQuatationsEntityRepository,
            CurrencyEntityRepository currencyEntityRepository,
            RegisterSubject registerSubject,
            ShopListSubject shopListSubject,
            UpdateSubject updateSubject
    ) {
        this.currencyQuatationsEntityRepository = currencyQuatationsEntityRepository;
        this.currencyEntityRepository = currencyEntityRepository;
        this.registerSubject = registerSubject;
        this.shopListSubject = shopListSubject;
        this.updateSubject = updateSubject;
    }

    @Override
    public void onNext(Object entity) {
        if (entity instanceof ShopListRequest) {
            ShopListRequest request = (ShopListRequest) entity;
            List<CurrencyQuatationsEntity> quotations = currencyQuatationsEntityRepository
                    .findAll();
            for (CurrencyQuatationsEntity quotation : quotations) {
                shopListSubject.subject(new ShopListCurrency(quotation, request));
            }
        } else if (entity instanceof RegisterUserRequest) {
            RegisterUserRequest request = (RegisterUserRequest) entity;
            List<CurrencyEntity> currencies = currencyEntityRepository
                    .findAll();
            for (CurrencyEntity currency : currencies) {
                registerSubject.subject(new RegisterUserCurrency(currency, request));
            }
        } else if (entity instanceof UpdateUserRequest) {
            UpdateUserRequest request = (UpdateUserRequest) entity;
            List<CurrencyEntity> currencies = currencyEntityRepository
                    .findAll();
            for (CurrencyEntity currency : currencies) {
                updateSubject.subject(new UpdateUserCurrency(currency, request));
            }
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {
    }

}
