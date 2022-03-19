package ru.itmo.sd.tasks.reactive.subjects.entities;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import lombok.*;
import ru.itmo.sd.tasks.reactive.db.CurrencyQuatationsEntity;
import ru.itmo.sd.tasks.reactive.db.ItemEntity;
import ru.itmo.sd.tasks.reactive.db.UserEntity;

public abstract class ShopListEntity {

    public abstract ShopListRequest getRequest();

    @ToString
    @Getter
    @Setter
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static class ShopListRequest extends ShopListEntity {

        private final DeferredResult<ModelAndView> future;

        private final String userIdentifier;

        @Override
        public ShopListRequest getRequest() {
            return this;
        }

    }

    @ToString
    @Getter
    @Setter
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static class ShopListItem extends ShopListEntity {

        private final ItemEntity item;

        private final ShopListRequest request;
        private final int total;

    }

    @ToString
    @Getter
    @Setter
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static class ShopListUser extends ShopListEntity {

        private final UserEntity user;

        private final ShopListRequest request;

    }

    @ToString
    @Getter
    @Setter
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static class ShopListCurrency extends ShopListEntity {

        private final CurrencyQuatationsEntity quatation;

        private final ShopListRequest request;

    }

}
