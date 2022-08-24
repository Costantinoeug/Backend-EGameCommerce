package it.costantino.egamecommerce.services;

import it.costantino.egamecommerce.entities.Cart;
import it.costantino.egamecommerce.entities.User;
import it.costantino.egamecommerce.repositories.CartRepository;
import it.costantino.egamecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AccountingServices {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;


    //crea utente e relativo carrello
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Cart registerUser(String username)  {
        if ( userRepository.existsByUsername(username )){
            User user=userRepository.findByUsername(username);
            return cartRepository.findByRelatedUser(user);
        }
        User user=new User();
        user.setUsername(username);
        userRepository.save(user);
        Cart cart=new Cart();
        cart.setRelatedUser(user);
        cart.setPrice(0);
        return cartRepository.save(cart);
    }//registerUser


}
