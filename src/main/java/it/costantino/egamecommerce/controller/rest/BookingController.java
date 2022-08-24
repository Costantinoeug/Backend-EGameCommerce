package it.costantino.egamecommerce.controller.rest;

import it.costantino.egamecommerce.dto.BookingDTO;
import it.costantino.egamecommerce.services.BookingServices;
import it.costantino.egamecommerce.support.Utils;
import it.costantino.egamecommerce.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingServices bookingServices;




    @PostMapping("/book")
    public ResponseEntity bookGame(@RequestBody BookingDTO bookingDTO){
        try{
            BookingDTO booking=bookingServices.bookGame(bookingDTO);
            return new ResponseEntity(booking,HttpStatus.OK);
        }catch(InvalidQuantityException iqe){
            return new ResponseEntity("ERROR_INVALID_QUANTITY", HttpStatus.BAD_REQUEST);
        }catch(UsernameNotFoundException unf){
            return new ResponseEntity("ERROR_USERNAME_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }catch(NotExistingGameException neg){
            return new ResponseEntity("ERROR_GAME_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }catch(RuntimeException re){
            Utils.stampaEccezioni(re);
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN", HttpStatus.BAD_REQUEST);
        }
    }//bookGame

    @DeleteMapping("/remove")
    public ResponseEntity removeBooking(@RequestParam(value="id") int id){
        try{
            BookingDTO bDTO=bookingServices.removeBooking(id);
            return new ResponseEntity(bDTO,HttpStatus.OK);
        }catch (NullBookingPointerException nbpe){
            return new ResponseEntity("ERROR_BOOKING_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }catch(RuntimeException re){
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN", HttpStatus.BAD_REQUEST);
        }
    }//removeBooking


    @GetMapping("/byuser")
    public ResponseEntity getAllBookingsByUser(@RequestParam(value = "username") String username){
      try {
      List<BookingDTO> l = bookingServices.getAllBookingsByUser(username);
      return new ResponseEntity(l, HttpStatus.OK);
      }catch(UsernameNotFoundException unf) {
          return new ResponseEntity("USERNAME_NOT_FOUND", HttpStatus.BAD_REQUEST);
      }catch (RuntimeException re) {
          return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN", HttpStatus.BAD_REQUEST);
      }
    }//getAllBookingsByUser

    @PutMapping("/updatequantity")
    public ResponseEntity updateQuantity(@RequestParam(value="id") int id,@RequestParam(value = "quantity") Integer quantity){
        try{
            BookingDTO bookingDTO=bookingServices.updateQuantity(id,quantity);
            return new ResponseEntity(bookingDTO,HttpStatus.OK);
        }catch(NotExistingBookingException nege){
            return new ResponseEntity("INSERT_A_VALID_BOOKING", HttpStatus.BAD_REQUEST);
        }catch(InvalidQuantityException iqe){
            return new ResponseEntity("INSERT_A_VALID_QUANTITY", HttpStatus.BAD_REQUEST);
        }
        catch(RuntimeException re){
            Utils.stampaEccezioni(re);
            return new ResponseEntity("UNKNOWN_ERROR_-_UPDATE_AGAIN", HttpStatus.BAD_REQUEST);
        }
    }//updateQuantity


}
