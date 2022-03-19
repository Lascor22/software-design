package ru.itmo.sd.tasks.reactive.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.itmo.sd.tasks.reactive.db.UserEntity;
import ru.itmo.sd.tasks.reactive.db.UserEntityRepository;
import ru.itmo.sd.tasks.reactive.subjects.RegisterSubject;
import ru.itmo.sd.tasks.reactive.subjects.ShopListSubject;
import ru.itmo.sd.tasks.reactive.subjects.UpdateSubject;
import ru.itmo.sd.tasks.reactive.subjects.entities.RegisterEntity;
import ru.itmo.sd.tasks.reactive.subjects.entities.ShopListEntity;
import ru.itmo.sd.tasks.reactive.subjects.entities.UpdateEntity;

@Service
public class UsersLoader implements Observer<Object> {

    private final UserEntityRepository userRepository;
    private final RegisterSubject registerSubject;
    private final ShopListSubject shopListSubject;
    private final UpdateSubject updateSubject;

    public UsersLoader(
            UserEntityRepository userRepository,
            RegisterSubject registerSubject,
            ShopListSubject shopListSubject,
            UpdateSubject updateSubject
    ) {
        this.userRepository = userRepository;
        this.registerSubject = registerSubject;
        this.shopListSubject = shopListSubject;
        this.updateSubject = updateSubject;
    }

    @Override
    public void onNext(Object entity) {
        if (entity instanceof ShopListEntity.ShopListRequest) {
            ShopListEntity.ShopListRequest request = (ShopListEntity.ShopListRequest) entity;
            final String userID = request.getUserIdentifier();

            UserEntity user = userRepository.findByIdentifier(userID);
            ShopListEntity.ShopListUser listUser = new ShopListEntity.ShopListUser(user, request);
            shopListSubject.subject(listUser);
        } else if (entity instanceof RegisterEntity.RegisterUserRequest) {
            RegisterEntity.RegisterUserRequest request = (RegisterEntity.RegisterUserRequest) entity;

            final String identifier = request.getName().trim().toLowerCase();
            UserEntity user = userRepository.findByIdentifier(identifier);

            RegisterEntity.RegisterUserUser registerUser = new RegisterEntity.RegisterUserUser(user, request);
            registerSubject.subject(registerUser);
        } else if (entity instanceof UpdateEntity.UpdateUserRequest) {
            UpdateEntity.UpdateUserRequest request = (UpdateEntity.UpdateUserRequest) entity;

            final String identifier = request.getUpdatedUser().getIdentifier();
            UserEntity user = userRepository.findByIdentifier(identifier);

            final UserEntity updatedUser = request.getUpdatedUser();
            updatedUser.setCurrency(user.getCurrency());
            updatedUser.setLogin(user.getLogin());
            updatedUser.setId(user.getId());

            UpdateEntity.UpdateUserUser userUser = new UpdateEntity.UpdateUserUser(updatedUser, request);
            updateSubject.subject(userUser);
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
