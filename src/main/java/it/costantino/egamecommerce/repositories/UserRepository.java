package it.costantino.egamecommerce.repositories;

import it.costantino.egamecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    //non ritorna una lista poichè username è unique
    User findByUsername(String username);

    boolean existsByUsername(String username);

}
