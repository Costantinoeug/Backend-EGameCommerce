package it.costantino.egamecommerce.repositories;

import it.costantino.egamecommerce.entities.Booking;
import it.costantino.egamecommerce.entities.Game;
import it.costantino.egamecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Integer> {

    Booking findById(int id);

    List<Booking> findAllByRelatedGame(Game g);

    List<Booking> findAllByBuyer(User u);

    boolean existsByRelatedGameAndBuyer(Game g,User buyer);


    Booking findByRelatedGameAndBuyer(Game g,User buyer);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Booking b set b.quantity = :quantity where b.id = :id")
    int updateQuantity(int id, int quantity);



    @Query("select b from Booking b "+
            "where (b.relatedGame=:relatedGame or :relatedGame is null) and "+
            "      (b.buyer=:buyer or :buyer is null) "
    )
    List<Booking> advancedSearch(Game relatedGame,User buyer);


}
