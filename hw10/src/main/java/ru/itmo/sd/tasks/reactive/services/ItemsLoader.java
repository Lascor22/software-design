package ru.itmo.sd.tasks.reactive.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.itmo.sd.tasks.reactive.db.ItemEntity;
import ru.itmo.sd.tasks.reactive.db.ItemEntityRepository;
import ru.itmo.sd.tasks.reactive.subjects.ShopListSubject;
import ru.itmo.sd.tasks.reactive.subjects.entities.ShopListEntity;

@Component
public class ItemsLoader implements Observer<Object> {

    private final ItemEntityRepository itemsRepository;
    private final ShopListSubject shopListSubject;

    public ItemsLoader(ItemEntityRepository itemsRepository, ShopListSubject shopListSubject) {
        this.itemsRepository = itemsRepository;
        this.shopListSubject = shopListSubject;
    }

    @Override
    public void onNext(Object entity) {
        if (!(entity instanceof ShopListEntity.ShopListRequest)) {
            return;
        }

        ShopListEntity.ShopListRequest request = (ShopListEntity.ShopListRequest) entity;
        List<ItemEntity> items = itemsRepository.findAll();
        final int total = items.size();

        for (ItemEntity item : items) {
            ShopListEntity.ShopListItem listItem = new ShopListEntity.ShopListItem(item, request, total);
            shopListSubject.subject(listItem);
        }

        if (total == 0) {
            ShopListEntity.ShopListItem listItem = new ShopListEntity.ShopListItem(null, request, total);
            shopListSubject.subject(listItem);
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