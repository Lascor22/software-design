package ru.itmo.sd.tasks.reactive.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemEntityRepository extends JpaRepository<ItemEntity, Long> {

}
