package it.costantino.egamecommerce.dto;

import it.costantino.egamecommerce.entities.Game;
import lombok.Data;

import java.io.Serializable;

@Data
public class GameDTO implements Serializable {

    private String name;

    private String publisher;

    private String developer;

    private Integer pegi;

    private String platform;

    private String genre;

    private String description;

    private Float price;

    private Integer quantity;

    private boolean hidden;

    public GameDTO(Game g){
        this.name=g.getName();
        this.publisher=g.getPublisher();
        this.developer=g.getDeveloper();
        this.pegi=g.getPegi();
        this.platform=g.getPlatform();
        this.genre=g.getGenre();
        this.description=g.getDescription();
        this.price=g.getPrice();
        this.quantity=g.getQuantity();
        this.hidden=g.isHidden();
    }
    public GameDTO(){

    }
}
