package ru.itmo.sd.tasks.reactive.controllers;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import ru.itmo.sd.tasks.reactive.subjects.ShopListSubject;
import ru.itmo.sd.tasks.reactive.subjects.entities.ShopListEntity;
import ru.itmo.sd.tasks.reactive.subjects.entities.ShopListEntity.ShopListRequest;

@Controller
public class ShopListController {

    private final ShopListSubject shopListSubject;

    public ShopListController(ShopListSubject shopListSubject) {
        this.shopListSubject = shopListSubject;
    }

    @GetMapping(path = {"/goods"})
    public DeferredResult<ModelAndView> handleShopList(HttpServletRequest request) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies())
                .orElse(new Cookie[]{});

        Cookie cookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("Client"))
                .findFirst().orElse(new Cookie("Client", "guest"));

        DeferredResult<ModelAndView> result = new DeferredResult<>();
        ShopListEntity desc = new ShopListRequest(result, cookie.getValue());
        shopListSubject.subject(desc);

        return result;
    }

}
