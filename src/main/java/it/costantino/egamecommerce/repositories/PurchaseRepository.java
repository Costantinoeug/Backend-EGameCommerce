package it.costantino.egamecommerce.repositories;

import it.costantino.egamecommerce.entities.Purchase;
import it.costantino.egamecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {

    //qui ci sono metodi di interrogazione legati agli aquisti e non alle singole copie

    List<Purchase> findAllByBuyer(User buyer);



}
