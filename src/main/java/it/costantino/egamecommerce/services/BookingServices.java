package it.costantino.egamecommerce.services;

import it.costantino.egamecommerce.dto.BookingDTO;
import it.costantino.egamecommerce.entities.Booking;
import it.costantino.egamecommerce.entities.Game;
import it.costantino.egamecommerce.entities.User;
import it.costantino.egamecommerce.repositories.BookingRepository;
import it.costantino.egamecommerce.repositories.GameRepository;
import it.costantino.egamecommerce.repositories.PurchasedGameRepository;
import it.costantino.egamecommerce.repositories.UserRepository;
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
public class BookingServices {

    @Autowired
    UserRepository userRep;
    @Autowired
    BookingRepository bookingRep;
    @Autowired
    PurchasedGameRepository purchasedGameRep;
    @Autowired
    GameRepository gameRep;
    @Autowired
    EntityManager entityManager;


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingDTO updateQuantity(int id, int quantity){
        if (!bookingRep.existsById(id)) throw new NotExistingBookingException();
        if (quantity<=0) throw new InvalidQuantityException();
        Booking b=bookingRep.findById(id);
        entityManager.lock(b,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        bookingRep.updateQuantity(id,quantity);

        return new BookingDTO(bookingRep.findById(id));
    }//updateQuantity

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingDTO bookGame(BookingDTO bookingDTO){
        if(bookingDTO.getQuantity()<=0) throw new InvalidQuantityException();
        if(!gameRep.existsByNameAndHiddenFalse(bookingDTO.getName())) throw new NotExistingGameException();
        if(!userRep.existsByUsername(bookingDTO.getUser())) throw new UsernameNotFoundException();

        Game game=gameRep.findByNameAndHiddenFalse(bookingDTO.getName());
        User user=userRep.findByUsername(bookingDTO.getUser());
        entityManager.lock(game,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        if(bookingRep.existsByRelatedGameAndBuyer(game,user)){
            Booking booking=bookingRep.findByRelatedGameAndBuyer(game,user);
            int newQuantity=bookingDTO.getQuantity()+booking.getQuantity();
            return updateQuantity(booking.getId(), newQuantity);
        }
        else {
            Booking booking = new Booking();
            booking.setQuantity(bookingDTO.getQuantity());
            booking.setRelatedGame(game);
            booking.setBuyer(user);
            Booking b = bookingRep.save(booking);
            return new BookingDTO(b);
        }
    }//bookGame

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingDTO removeBooking(int id){
        Booking result=bookingRep.findById(id);
        if (result==null) throw new NullBookingPointerException();
        entityManager.lock(result,LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        bookingRep.delete(result);
        return new BookingDTO(result);
    }//removeBooking








    @Transactional(isolation = Isolation.READ_COMMITTED,readOnly = true)
    public List<BookingDTO> getAllBookingsByUser(String username){
        if (!userRep.existsByUsername(username)) throw new UsernameNotFoundException();
        User user=userRep.findByUsername(username);
        List<BookingDTO> l=new LinkedList<>();
        for(Booking b: bookingRep.findAllByBuyer(user))
            l.add(new BookingDTO(b));
        return l;
    }//getAllBookingsByUser


}
