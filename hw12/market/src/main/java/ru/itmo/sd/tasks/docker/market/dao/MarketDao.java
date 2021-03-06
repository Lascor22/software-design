package ru.itmo.sd.tasks.docker.market.dao;

import com.mongodb.rx.client.Success;
import ru.itmo.sd.tasks.docker.market.model.Stocks;
import rx.Observable;

public interface MarketDao {
    Observable<Success> addCompany(String name, int stocksCount, int stocksPrice);

    Observable<Stocks> getCompanies();

    Observable<Success> addStocks(String companyName, int stocksCount);

    Observable<Stocks> getStocksInfo(String companyName);

    Observable<Success> buyStocks(String companyName, int count);

    Observable<Success> changeStocksPrice(String companyName, int newStocksPrice);
}
