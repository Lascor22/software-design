package ru.itmo.sd.tasks.reactive.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyEntityRepository extends JpaRepository<CurrencyEntity, Long> {

    CurrencyEntity findByCodeISO(String codeISO);

}
