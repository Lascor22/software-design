package ru.itmo.sd.tasks.reactive.subjects.entities;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.async.DeferredResult;

import lombok.*;
import ru.itmo.sd.tasks.reactive.db.CurrencyEntity;
import ru.itmo.sd.tasks.reactive.db.UserEntity;

@ToString
@Getter
@Setter
public abstract class RegisterEntity {

    public abstract RegisterRequest getRequest();

    @ToString
    @Getter
    @Setter
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static abstract class RegisterRequest extends RegisterEntity {

        protected final DeferredResult<String> future;

    }

    @ToString
    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    public static class RegisterUserRequest extends RegisterRequest {

        private final String name, currency;
        private final HttpServletResponse response;

        public RegisterUserRequest(DeferredResult<String> future, String name,
                                   String currency, HttpServletResponse response) {
            super(future);
            this.name = name;
            this.currency = currency;
            this.response = response;
        }

        @Override
        public RegisterRequest getRequest() {
            return this;
        }

    }

    @ToString
    @Getter
    @Setter
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class RegisterUserCurrency extends RegisterEntity {

        private final CurrencyEntity currency;

        private final RegisterUserRequest request;

    }

    @ToString
    @Getter
    @Setter
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class RegisterUserUser extends RegisterEntity {

        private final UserEntity user;

        private final RegisterUserRequest request;

    }

}
