package it.costantino.egamecommerce.repositories;

import it.costantino.egamecommerce.entities.Game;
import it.costantino.egamecommerce.entities.Purchase;
import it.costantino.egamecommerce.entities.PurchasedGame;
import it.costantino.egamecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasedGameRepository extends JpaRepository<PurchasedGame,Integer> {

    //qui ci sono metodi di interrogazione legati alle singole copie di giochi

    PurchasedGame findById(int id);
    List<PurchasedGame> findAllByRelatedPurchase(Purchase p);
    List<PurchasedGame> findAllByRelatedGame(Game g);

    @Query("select sum(pg.quantity) from PurchasedGame pg where pg.relatedGame=:game")
    Integer copiesSoldOfGame(Game game);

    @Query("select sum(pg.quantity) from PurchasedGame  pg where pg.relatedPurchase.buyer=:buyer")
    Integer copiesOfAllGamesPurchasedByBuyer(User buyer);

    @Query("select sum(pg.quantity) from PurchasedGame  pg where pg.relatedPurchase.buyer=:buyer and pg.relatedGame=:game")
    Integer copiesOfGamePurchasedByBuyer(User buyer,Game g);


}
