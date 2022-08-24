package it.costantino.egamecommerce.controller.rest;


import it.costantino.egamecommerce.dto.GameInCartDTO;
import it.costantino.egamecommerce.services.AccountingServices;
import it.costantino.egamecommerce.services.CartServices;
import it.costantino.egamecommerce.support.Utils;
import it.costantino.egamecommerce.support.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartServices cartServices;
    @Autowired
    private AccountingServices accountingServices;

    @GetMapping()
    public ResponseEntity getGames(@RequestParam String username){
        try{
            List<GameInCartDTO> gicsDTO=cartServices.getGics(username);
            return new ResponseEntity(gicsDTO,HttpStatus.OK);
        }catch(UsernameNotFoundException e){
            return new ResponseEntity("USERNAME_NOT_FOUND",HttpStatus.BAD_REQUEST);
        }catch(NotExistingCartException re){
            return new ResponseEntity("CART_NOT_FOUND",HttpStatus.BAD_REQUEST);
        }catch(RuntimeException re){
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN",HttpStatus.BAD_REQUEST);
        }
    }//addGameToTheCart

    @GetMapping("/refresh")
    public ResponseEntity refresh(@RequestParam String username){
        try{
            List<GameInCartDTO> gicsDTO=cartServices.refresh(username);
            System.out.println("lala"+gicsDTO.get(0).getPrice());
            return new ResponseEntity(gicsDTO,HttpStatus.OK);
        }catch(UsernameNotFoundException e){
            return new ResponseEntity("USERNAME_NOT_FOUND",HttpStatus.BAD_REQUEST);
        }catch(NotExistingCartException re){
            return new ResponseEntity("CART_NOT_FOUND",HttpStatus.BAD_REQUEST);
        }catch(RuntimeException re){
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN",HttpStatus.BAD_REQUEST);
        }
    }//addGameToTheCart

    @PostMapping("/add")
    public ResponseEntity addGameToTheCart(@RequestBody GameInCartDTO gicDTO){
        try{
            gicDTO=cartServices.addGameToTheCart(gicDTO);
            return new ResponseEntity<>(gicDTO,HttpStatus.OK);
        }catch(GameInCartNotFoundException gicnfe){
            return new ResponseEntity("GAME_IN_CART_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }catch(NotExistingCartException nec){
            return new ResponseEntity("CART_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }catch(RuntimeException re){
            Utils.stampaEccezioni(re);
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN",HttpStatus.BAD_REQUEST);
        }
    }//addGameToTheCart

    @DeleteMapping("/remove")
    public ResponseEntity removeGameFromTheCart(@RequestParam(value="id") int id){
        try{
            float newPrice=cartServices.removeGameToTheCart(id);
            return new ResponseEntity(newPrice,HttpStatus.OK);
        }catch(GameInCartNotFoundException gicnfe){
            return new ResponseEntity("ERROR_GAME_NOT_PRESENT_IN_THE_CART",HttpStatus.BAD_REQUEST);
        }catch (RuntimeException re){
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN",HttpStatus.BAD_REQUEST);
        }
    }//removeGameToTheCart

    @PutMapping("/updatequantity")
    public ResponseEntity updateQuantity(@RequestParam(value="id") int id,@RequestParam(value = "quantity") Integer quantity){
        try{
            GameInCartDTO gicDTO=cartServices.updateQuantity(id,quantity);
            return new ResponseEntity(gicDTO,HttpStatus.OK);
        }catch(GameInCartNotFoundException gicnf){
            return new ResponseEntity("INSERT_A_VALID_GAME_IN_CART", HttpStatus.BAD_REQUEST);
        }catch(InvalidQuantityException iqe){
            return new ResponseEntity("INSERT_A_VALID_QUANTITY", HttpStatus.BAD_REQUEST);
        }
        catch(RuntimeException re){
            Utils.stampaEccezioni(re);
            return new ResponseEntity("UNKNOWN_ERROR_-_UPDATE_AGAIN", HttpStatus.BAD_REQUEST);
        }
    }//updateQuantity

    @DeleteMapping("/empty")
    public ResponseEntity emptyCart(@RequestParam(value = "username") String username){
        try{
            List<GameInCartDTO> res=cartServices.emptyCart(username);
            return new ResponseEntity(res,HttpStatus.OK);
        }catch (UsernameNotFoundException nece){
            return new ResponseEntity("USER_NOT_FOUND",HttpStatus.BAD_REQUEST);
        }catch (NotExistingCartException nece){
            return new ResponseEntity("CART_NOT_FOUND",HttpStatus.BAD_REQUEST);
        }catch (RuntimeException re){
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN",HttpStatus.BAD_REQUEST);
        }
    }//emptyCart


    @PostMapping("/addpurchase")
    public ResponseEntity addPurchase(@RequestParam(value = "username") String username){
        try{
            List<GameInCartDTO> l=cartServices.addPurchaseByCart(username);
            return new ResponseEntity(l,HttpStatus.OK);
        }catch (InvalidQuantityException ece){
            return new ResponseEntity("ERROR_INVALID_QUANTITY",HttpStatus.BAD_REQUEST);
        }catch (EmptyCartException ece){
            return new ResponseEntity("ERROR_CART_IS_EMPTY",HttpStatus.BAD_REQUEST);
        }catch( UsernameNotFoundException unfe){
            return new ResponseEntity("USERNAME_NOT_FOUND",HttpStatus.BAD_REQUEST);
        }catch (NotExistingCartException pce){
            return new ResponseEntity("CART_NOT_FOUND",HttpStatus.BAD_REQUEST);
        }catch(RuntimeException re){
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN",HttpStatus.BAD_REQUEST);
        }
    }//addPurchase



}
