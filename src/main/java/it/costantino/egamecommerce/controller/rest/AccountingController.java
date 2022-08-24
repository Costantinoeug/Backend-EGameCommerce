package it.costantino.egamecommerce.controller.rest;

import it.costantino.egamecommerce.entities.Cart;
import it.costantino.egamecommerce.services.AccountingServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AccountingController {
    @Autowired
    private AccountingServices accountingServices;

    @PostMapping("/create")
    public ResponseEntity register(@RequestBody String username) {
        try {
            Cart cart = accountingServices.registerUser(username);
            return new ResponseEntity(cart.getId(), HttpStatus.OK);
        } catch(RuntimeException re){
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN", HttpStatus.BAD_REQUEST);
        }
    }//create




}
