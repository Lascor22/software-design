package ru.itmo.sd.tasks.reactive;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import ru.itmo.sd.tasks.reactive.services.*;
import ru.itmo.sd.tasks.reactive.subjects.RegisterSubject;
import ru.itmo.sd.tasks.reactive.subjects.ShopListSubject;
import ru.itmo.sd.tasks.reactive.subjects.UpdateSubject;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class RunReactiveShop {

    public RunReactiveShop(
            EntitiesRegister entitiesRegister,
            ShopListComposer shopListComposer,
            CurrencyLoader currencyLoader,
            UsersLoader usersLoader,
            ItemsLoader itemsLoader,
            RegisterSubject registerSubject,
            ShopListSubject shopListSubject,
            UpdateSubject updateSubject
    ) {
        this.entitiesRegister = entitiesRegister;
        this.shopListComposer = shopListComposer;
        this.currencyLoader = currencyLoader;
        this.usersLoader = usersLoader;
        this.itemsLoader = itemsLoader;
        this.registerSubject = registerSubject;
        this.shopListSubject = shopListSubject;
        this.updateSubject = updateSubject;
    }

    public static void main(String... args) {
        Class<RunReactiveShop> mainClass = RunReactiveShop.class;
        final ConfigurableApplicationContext context
                = SpringApplication.run(mainClass, args);

        context.getBean(mainClass).initializeReactivity();
    }

    private final EntitiesRegister entitiesRegister;
    private final ShopListComposer shopListComposer;
    private final CurrencyLoader currencyLoader;
    private final UsersLoader usersLoader;
    private final ItemsLoader itemsLoader;

    private final RegisterSubject registerSubject;
    private final ShopListSubject shopListSubject;
    private final UpdateSubject updateSubject;

    public void initializeReactivity() {
        shopListSubject.subscribe(shopListComposer);
        shopListSubject.subscribe(currencyLoader);
        shopListSubject.subscribe(itemsLoader);
        shopListSubject.subscribe(usersLoader);

        registerSubject.subscribe(entitiesRegister);
        registerSubject.subscribe(currencyLoader);
        registerSubject.subscribe(usersLoader);

        updateSubject.subscribe(entitiesRegister);
        updateSubject.subscribe(currencyLoader);
        updateSubject.subscribe(usersLoader);
    }

    @Configuration
    @EnableAsync
    public static class SpringAsyncConfig implements AsyncConfigurer {

        @Override
        public Executor getAsyncExecutor() {
            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.setPoolSize(4);
            scheduler.initialize();
            return scheduler;
        }

    }

}
