package it.costantino.egamecommerce.repositories;

import it.costantino.egamecommerce.entities.Cart;
import it.costantino.egamecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {


    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Cart c set c.price = :price where c.id = :id")
    int updatePrice(int id, float price);

    Cart findByRelatedUser(User u);

    Cart findById(int id);

    boolean existsByRelatedUser(User user);

    boolean existsById(int id);
}
