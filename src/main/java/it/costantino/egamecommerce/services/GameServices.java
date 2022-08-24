package it.costantino.egamecommerce.services;

import it.costantino.egamecommerce.dto.GameDTO;
import it.costantino.egamecommerce.entities.*;
import it.costantino.egamecommerce.repositories.*;
import it.costantino.egamecommerce.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.List;

@Service
public class GameServices {

    @Autowired
    private GameRepository gameRep;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRep;
    @Autowired
    private PurchaseRepository purchaseRep;

    @Autowired
    private PurchasedGameRepository purchasedGameRep;
    @Autowired
    private BookingRepository bookingRep;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private GameInCartRepository gicRep;


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GameDTO updateQuantityAndPurchaseBooking(String name, int quantity){
        if (!gameRep.existsByName(name)) throw new NotExistingGameException();
        if (quantity<0) throw new InvalidQuantityException();
        Game game=gameRep.findByName(name);

        entityManager.lock(game,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        gameRep.updateQuantity(game.getName(), quantity);
        for(Booking b: bookingRep.findAllByRelatedGame(game))
            entityManager.lock(b,LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        List<Booking> bookings=game.getBookings();
        for(Booking b: bookings){
            game=gameRep.findByName(name);
            if(game.getQuantity()>= b.getQuantity()){
                Purchase purchase=new Purchase();
                purchase.setBuyer(b.getBuyer());
                float price=b.getQuantity()*game.getPrice();
                purchase.setPrice(price);
                purchase=purchaseRep.save(purchase);
                PurchasedGame pg=new PurchasedGame();
                pg.setRelatedPurchase(purchase);
                pg.setRelatedGame(game);
                pg.setQuantity(b.getQuantity());
                pg.setPrice(game.getPrice());
                pg.setBoughtByBooking(true);
                pg=purchasedGameRep.save(pg);
                int newQuantity=game.getQuantity()-b.getQuantity();
                gameRep.updateQuantity(game.getName(),newQuantity);
                bookingRep.deleteById(b.getId());
            }
        }
        return new GameDTO(gameRep.findByName(name));
    }//updateQuantityAndManageBookings



    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GameDTO updatePrice(String name,float price){
        if (!gameRep.existsByName(name)) throw new NotExistingGameException();
        if(price<=0) throw new InvalidPriceException();
        Game game=gameRep.findByName(name);
        entityManager.lock(game,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        gameRep.updatePrice(game.getName(),price);
        return new GameDTO(gameRep.findByName(name));
    }//updatePrice


    @Transactional( isolation = Isolation.READ_COMMITTED)
    public Game addGame(GameDTO gameDTO){
        if (gameRep.existsByName(gameDTO.getName())) throw new AlreadyExistingGameException();
        Game game=new Game();
        game.setByDTO(gameDTO);
        gameRep.save(game);
        return gameRep.findByName(gameDTO.getName());
    }//addGame

    @Transactional( isolation = Isolation.READ_COMMITTED)
    public int displayGame(String name) {
        if (!gameRep.existsByName(name)) throw new NotExistingGameException();
        return gameRep.displayGame(name);
    }//displayGame







    @Transactional( isolation = Isolation.READ_COMMITTED)
    public int hideGame(String name) {
        if (!gameRep.existsByName(name)) throw new NotExistingGameException();
        Game game=gameRep.findByName(name);
        entityManager.lock(game,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        int returnValue=gameRep.hideGame(game.getName());
        List<GameInCart> gics=gicRep.findAllByRelatedGame(game);
        for(GameInCart gic: gics)
            removeGicAndUpdateCartAfterGameHided(gic);
        List<Booking> bookings=bookingRep.findAllByRelatedGame(game);
        for(Booking b: bookings)
            removeBookingAfterGameHided(b);
        return returnValue;
    }//hideGame

    //sottometodo di hideGame()
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void removeBookingAfterGameHided(Booking b){
        entityManager.lock(b,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        bookingRep.deleteById(b.getId());
    }

    //sottometodo di hideGame()
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void removeGicAndUpdateCartAfterGameHided(GameInCart gic){
        Cart cart=cartRepository.findById((int)gic.getRelatedCart().getId());
        entityManager.lock(cart,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        entityManager.lock(gic,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        float newPrice=cart.getPrice()-gic.getQuantity()*gic.getRelatedGame().getPrice();
        cartRepository.updatePrice(cart.getId(),newPrice);
        gicRep.deleteById(gic.getId());
    }

















    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByAdvancedSearch(String name,String pub,String dev,int pegi,String platform,String genre, float priceMin, float priceMax,int quantity){
        return gameRep.advancedSearch(name,pub,dev,pegi,platform,genre,priceMin,priceMax,quantity);
    }//showByAdvancedSearch

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByAdvancedSearchAndHiddenFalse(String name,String pub,String dev,int pegi,String platform,String genre, float priceMin, float priceMax,int quantity){
        return gameRep.advancedSearchAndHiddenFalse(name,pub,dev,pegi,platform,genre,priceMin,priceMax,quantity);
    }//showByAdvancedSearch



    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByNameContaining(String name){
        return gameRep.findByNameContaining(name);
    }//showByNameContaining

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByNameContainingAndHiddenFalse(String name){
        return gameRep.findByNameContainingAndHiddenFalse(name);
    }//showByNameContaining


    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByPlatformContaining(String platform){
        return gameRep.findByPlatformContaining(platform);
    }//showByplatformContaining

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByPlatformContainingAndHiddenFalse(String platform){
        return gameRep.findByPlatformContainingAndHiddenFalse(platform);
    }//showByplatformContaining


    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByGenreContaining(String genre){
        return gameRep.findByGenreContaining(genre);
    }//showBygenreContaining

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByGenreContainingAndHiddenFalse(String genre){
        return gameRep.findByGenreContainingAndHiddenFalse(genre);
    }//showBygenreContaining


    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByPriceBetweenAndHiddenFalse(float p1,float p2){
        return gameRep.findByPriceBetweenAndHiddenFalse(p1,p2);
    }//showByPriceBetween

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByPriceBetween(float p1,float p2){
        return gameRep.findByPriceBetween(p1,p2);
    }//showByPriceBetween




    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByPriceGreaterThanEqualAndHiddenFalse(float p){
        return gameRep.findByPriceGreaterThanEqualAndHiddenFalse(p);
    }//showByPriceGreaterThanEqual

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByPriceGreaterThanEqual(float p){
        return gameRep.findByPriceGreaterThanEqual(p);
    }//showByPriceGreaterThanEqual


    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByPriceLessThanEqual(float p){
        return gameRep.findByPriceLessThanEqual(p);
    }//showByPriceLessThanEqual

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Game> showByPriceLessThanEqualAndHiddenFalse(float p){
        return gameRep.findByPriceLessThanEqualAndHiddenFalse(p);
    }//showByPriceLessThanEqual




}
