package it.costantino.egamecommerce.dto;

import it.costantino.egamecommerce.entities.Booking;
import lombok.Data;

import java.io.Serializable;

@Data
public class BookingDTO implements Serializable {
    private Integer id;

    private String name;


    private Integer quantity;

    private Float price;

    private String user;


    public BookingDTO(Booking b){
        this.id=b.getId();
        this.quantity=b.getQuantity();
        this.name=b.getRelatedGame().getName();
        this.price=b.getRelatedGame().getPrice();
        this.user=b.getBuyer().getUsername();
    }
    public BookingDTO(){}
}
