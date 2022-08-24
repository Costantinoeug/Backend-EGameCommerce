package it.costantino.egamecommerce.dto;

import it.costantino.egamecommerce.entities.Purchase;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Data
public class PurchaseDTO implements Serializable {
    private Integer id;

    private float price;

    private List<PurchasedGameDTO> pgs=new LinkedList<>();

    public PurchaseDTO(){}

    public PurchaseDTO(Purchase p){
        this.id=p.getId();
        this.price=p.getPrice();
    }

    public void addPurchasedGameDTO(PurchasedGameDTO pgDTO){
        this.pgs.add(pgDTO);
    }
}
