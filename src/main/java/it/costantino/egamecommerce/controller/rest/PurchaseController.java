package it.costantino.egamecommerce.controller.rest;

import it.costantino.egamecommerce.dto.PurchaseDTO;
import it.costantino.egamecommerce.services.PurchaseService;
import it.costantino.egamecommerce.support.Utils;
import it.costantino.egamecommerce.support.exception.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/byuser")
    public ResponseEntity getAllBookingsByUser(@RequestParam(value = "username") String username){
        try {
            List<PurchaseDTO> l = purchaseService.getAllPurchasesByUser(username);
            return new ResponseEntity(l, HttpStatus.OK);
        }catch(UsernameNotFoundException unf) {
            return new ResponseEntity("USERNAME_NOT_FOUND", HttpStatus.BAD_REQUEST);
        }catch (RuntimeException re) {
            Utils.stampaEccezioni(re);
            return new ResponseEntity("UNKNOWN_ERROR_-_TRY_AGAIN", HttpStatus.BAD_REQUEST);
        }
    }//getAllBookingsByUser
}
