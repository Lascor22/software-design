package ru.itmo.sd.tasks.reactive.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByIdentifier(String identifier);

}
