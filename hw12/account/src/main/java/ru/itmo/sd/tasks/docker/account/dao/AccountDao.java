package ru.itmo.sd.tasks.docker.account.dao;

import com.mongodb.rx.client.Success;
import ru.itmo.sd.tasks.docker.account.model.Stocks;
import rx.Observable;

public interface AccountDao {
    Observable<Success> addUser(long id);

    Observable<Success> addMoney(long id, int count);

    Observable<Stocks> getUserStocksInfo(long id);

    Observable<Integer> getAllMoney(long id);

    Observable<Success> buyStocks(long id, String companyName, int count);

    Observable<Success> sellStocks(long id, String companyName, int count);
}
