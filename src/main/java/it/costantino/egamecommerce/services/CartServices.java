package it.costantino.egamecommerce.services;

import it.costantino.egamecommerce.dto.GameInCartDTO;
import it.costantino.egamecommerce.entities.*;
import it.costantino.egamecommerce.repositories.*;
import it.costantino.egamecommerce.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.LinkedList;
import java.util.List;

@Service
public class CartServices {

    @Autowired
    EntityManager entityManager;


    @Autowired
    private PurchaseRepository purchaseRep;
    @Autowired
    private GameInCartRepository gicRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRep;

    @Autowired
    private PurchasedGameRepository purchasedGameRep;




    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GameInCartDTO addGameToTheCart(GameInCartDTO gicDTO) {
        User user=userRepository.findByUsername(gicDTO.getUsername());
        if(!cartRepository.existsById(user.getRelatedCart().getId())) throw new NotExistingCartException();
        Cart cart=user.getRelatedCart();
        Game game=gameRep.findByNameAndHiddenFalse(gicDTO.getGameName());
        if (gicRepository.existsByRelatedGameAndRelatedCart(game,cart)){
            GameInCart gic=gicRepository.findByRelatedGameAndRelatedCart(game,cart);
            int newQuantity=gicDTO.getQuantityRequested()+gic.getQuantity();
            return updateQuantity(gic.getId(), newQuantity);
        }
        else {
            entityManager.lock(cart, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            GameInCart gic = new GameInCart();
            gic.setRelatedCart(cart);
            gic.setRelatedGame(game);
            gic.setQuantity(gicDTO.getQuantityRequested());
            gic.setPrice(game.getPrice());
            GameInCart result = gicRepository.save(gic);
            float newCartPrice = cart.getPrice() + gicDTO.getPrice() * gicDTO.getQuantityRequested();
            cartRepository.updatePrice(cart.getId(), newCartPrice);
            return new GameInCartDTO(result);
        }
    }//addGameToTheCart

    //elimina il gic e aggiorna e ritorna il prezzo del carrello
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public float removeGameToTheCart(int id) throws GameInCartNotFoundException {
        if (!gicRepository.existsById(id)) throw new GameInCartNotFoundException();
        GameInCart gic=gicRepository.findById(id);
        entityManager.lock(gic,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        if(!cartRepository.existsById(gic.getRelatedCart().getId())) throw new NotExistingCartException();
        Cart cart=cartRepository.findById((int)gic.getRelatedCart().getId());
        entityManager.lock(cart,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        gicRepository.deleteById(gic.getId());
        float newCartPrice=cart.getPrice()-gic.getRelatedGame().getPrice()*gic.getQuantity();
        cartRepository.updatePrice(cart.getId(), newCartPrice );
        return cartRepository.findById((int)cart.getId()).getPrice();

    }//removeGameToTheCart

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GameInCartDTO updateQuantity( int id, int quantity){
        if (!gicRepository.existsById(id)) throw new GameInCartNotFoundException();
        if (quantity<=0) throw new InvalidQuantityException();
        GameInCart gic=gicRepository.findById(id);
        Cart cart=cartRepository.findById((int)gic.getRelatedCart().getId());
        entityManager.lock(cart,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        entityManager.lock(gic,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        int deltaQuantity=quantity-gic.getQuantity();
        gicRepository.updateQuantity(id, quantity);
        float newCartPrice=cart.getPrice()+deltaQuantity*gic.getPrice();
        cartRepository.updatePrice(gic.getRelatedCart().getId(), newCartPrice );
        return new GameInCartDTO(gicRepository.findById(id));
    }//updateQuantity

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<GameInCartDTO> emptyCart(String username){
        if(!userRepository.existsByUsername(username)) throw new UsernameNotFoundException();
        User user=userRepository.findByUsername(username);
        if (!cartRepository.existsByRelatedUser(user)) throw new NotExistingCartException();
        Cart cart=cartRepository.findByRelatedUser(user);
        entityManager.lock(cart, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        gicRepository.deleteAllByRelatedCart(cart);
        cartRepository.updatePrice(cart.getId(),0);
        return new LinkedList<>();
    }//emptyCart



    @Transactional(isolation=Isolation.READ_COMMITTED, readOnly = true)
    public List<GameInCartDTO> getGics(String username){
        if (!userRepository.existsByUsername(username)) throw new UsernameNotFoundException();
        User user=userRepository.findByUsername(username);
        if(!cartRepository.existsByRelatedUser(user)) throw new NotExistingCartException();
        Cart cart=cartRepository.findByRelatedUser(user);
        LinkedList<GameInCartDTO> gicsDTO=new LinkedList<>();
        for (GameInCart gic: gicRepository.findAllByRelatedCart(cart))
            gicsDTO.addLast(new GameInCartDTO(gic));
        return gicsDTO;
    }

    @Transactional(isolation=Isolation.READ_COMMITTED)
    public List<GameInCartDTO> refresh(String username){
        if (!userRepository.existsByUsername(username) ) throw new UsernameNotFoundException();
        User user=userRepository.findByUsername(username);
        if(!cartRepository.existsByRelatedUser(user)) throw new NotExistingCartException();
        Cart cart=cartRepository.findByRelatedUser(user);
        LinkedList<GameInCartDTO> gicsDTO=new LinkedList<>();
        for (GameInCart gic: gicRepository.findAllByRelatedCart(cart)) {
            if (gic.getPrice() != gic.getRelatedGame().getPrice())
                gicRepository.updatePrice(gic.getId(), gic.getRelatedGame().getPrice());
            gicsDTO.addLast(new GameInCartDTO(gic));
        }
        return gicsDTO;
    }






    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<GameInCartDTO> addPurchaseByCart(String username) {
        if (!userRepository.existsByUsername(username)) throw new UsernameNotFoundException();
        User user=userRepository.findByUsername(username);
        if (!cartRepository.existsByRelatedUser(user)) throw new NotExistingCartException();
        Cart cart=cartRepository.findByRelatedUser(user);
        if (cart.getRelatedGamesInCart()==null || cart.getRelatedGamesInCart().size()==0) throw new EmptyCartException();
        entityManager.lock(cart, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        for (GameInCart gic: cart.getRelatedGamesInCart()) {
            if(gic.getQuantity()>gic.getRelatedGame().getQuantity()) throw new NotExistingGameException();
            if (gic.getPrice() != gic.getRelatedGame().getPrice()) throw new InvalidPriceException();
            entityManager.lock(gic, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            entityManager.lock(gic.getRelatedGame(),LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        }
        Purchase purchase=new Purchase();
        purchase.setBuyer(user);
        float totalPrice=0.0f;
        for(GameInCart gic: cart.getRelatedGamesInCart()) {
            totalPrice += gic.getQuantity() * gic.getPrice();
            Game game=gic.getRelatedGame();
            int newQuantityGame=game.getQuantity()-gic.getQuantity();
            gameRep.updateQuantity(game.getName(),newQuantityGame);
        }
        purchase.setPrice(totalPrice);
        purchaseRep.save(purchase);
        for(GameInCart gic: cart.getRelatedGamesInCart()){
            PurchasedGame pg=new PurchasedGame();
            pg.setRelatedPurchase(purchase);
            pg.setRelatedGame(gic.getRelatedGame());
            pg.setPrice(gic.getPrice());
            pg.setQuantity(gic.getQuantity());
            purchasedGameRep.save(pg);
        }
        cartRepository.updatePrice(cart.getId(), 0);
        gicRepository.deleteAllByRelatedCart(cart);
        return new LinkedList<>();
    }//addPurchase





}
