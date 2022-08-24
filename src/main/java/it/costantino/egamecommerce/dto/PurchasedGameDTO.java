package it.costantino.egamecommerce.dto;

import it.costantino.egamecommerce.entities.PurchasedGame;
import lombok.Data;

import java.io.Serializable;

@Data
public class PurchasedGameDTO implements Serializable {
    private Integer id;

    private Integer quantity;

    private float price;

    private boolean boughtByBooking;

    private String name;

    private String publisher;

    private String developer;

    private Integer pegi;

    private String platform;

    private String genre;

    private String description;

    public  PurchasedGameDTO(){}

    public PurchasedGameDTO(PurchasedGame pg){

        this.id=pg.getId();
        this.quantity=pg.getQuantity();
        this.price= pg.getPrice();
        this.name=pg.getRelatedGame().getName();
        this.description=pg.getRelatedGame().getDescription();
        this.genre=pg.getRelatedGame().getGenre();
        this.developer=pg.getRelatedGame().getDeveloper();
        this.publisher=pg.getRelatedGame().getPublisher();
        this.pegi=pg.getRelatedGame().getPegi();
        this.platform=pg.getRelatedGame().getPlatform();
        this.boughtByBooking=pg.isBoughtByBooking();
    }
}
