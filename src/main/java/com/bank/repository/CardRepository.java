package com.bank.repository;

import com.bank.model.Card;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CardRepository implements PanacheRepository<Card> {

    public Optional<Card> findByTracking(String tracking) {
        return find("tracking", tracking).firstResultOptional();
    }

    public Optional<Card> findByCardId(String cardId) {
        return find("id", cardId).firstResultOptional();
    }
}
