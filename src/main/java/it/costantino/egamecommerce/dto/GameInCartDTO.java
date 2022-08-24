package it.costantino.egamecommerce.dto;

import it.costantino.egamecommerce.entities.GameInCart;
import lombok.Data;

import java.io.Serializable;

@Data
public class GameInCartDTO implements Serializable {
    private Integer id;

    private String gameName;

    private Float price;

    private Integer quantityRequested;

    private Integer quantityAvailable;

    private String username;


    private boolean valid;

   public GameInCartDTO(GameInCart gic){
       this.id=gic.getId();
       this.gameName=gic.getRelatedGame().getName();
       this.price= gic.getPrice();
       this.quantityRequested= gic.getQuantity();
       this.quantityAvailable=gic.getRelatedGame().getQuantity();
       this.username=gic.getRelatedCart().getRelatedUser().getUsername();
       if(quantityRequested<= quantityAvailable) valid=true;
       else valid=false;
   }
   public GameInCartDTO(){}
}
