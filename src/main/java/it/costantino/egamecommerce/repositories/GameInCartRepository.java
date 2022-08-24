package it.costantino.egamecommerce.repositories;

import it.costantino.egamecommerce.entities.Cart;
import it.costantino.egamecommerce.entities.Game;
import it.costantino.egamecommerce.entities.GameInCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameInCartRepository extends JpaRepository<GameInCart,Integer> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update GameInCart gic set gic.quantity = :quantity where gic.id = :id")
    int updateQuantity(int id, int quantity);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update GameInCart gic set gic.price = :price where gic.id = :id")
    int updatePrice(int id, float price);

    boolean existsByRelatedGameAndRelatedCart(Game game, Cart cart);

    GameInCart findByRelatedGameAndRelatedCart(Game game,Cart cart);

    void deleteById(int id);

    void deleteAllByRelatedGame(Game g);

    GameInCart findById(int id);

    List<GameInCart> findAllByRelatedGame(Game game);

    List<GameInCart> findAllByRelatedCart(Cart cart);

    void deleteAllByRelatedCart(Cart relatedCart);
}
