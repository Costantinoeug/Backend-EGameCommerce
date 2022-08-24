package it.costantino.egamecommerce.entities;

import it.costantino.egamecommerce.dto.GameDTO;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="game", schema="egamecommercedb")
public class Game {

    @Id
    @Basic
    @Column(name="name", unique = true, nullable = false)
    private String name;

    @Basic
    @Column(name="publisher", length = 50)
    private String publisher;

    @Basic
    @Column(name="developer", length = 50)
    private String developer;

    @Basic
    @Column(name="pegi")
    private Integer pegi;

    @Basic
    @Column(name="platform",  length = 150)
    private String platform;

    @Basic
    @Column(name="genre", length = 150)
    private String genre;

    @Basic
    @Column(name="description", length = 500)
    private String description;

    @Basic
    @Column(name="price",nullable = false)
    private Float price;

    @Basic
    @Column(name="quantity",nullable = false)
    private Integer quantity;



    @OneToMany(mappedBy = "relatedGame",fetch = FetchType.EAGER)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "relatedGame")
    private List<PurchasedGame> purchasedGames;

    @OneToMany(mappedBy = "relatedGame")
    private List<PurchasedGame> relatedGamesInCart;

    @Column(name="version")
    @Version
    private Long version;

    @Column(name = "hidden")
    private boolean hidden;

    public void setByDTO(GameDTO gameDTO){
        setPrice(gameDTO.getPrice());
        setName(gameDTO.getName());
        setPlatform(gameDTO.getPlatform());
        setDeveloper(gameDTO.getDeveloper());
        setPublisher(gameDTO.getPublisher());
        setPegi(gameDTO.getPegi());
        setDescription(gameDTO.getDescription());
        setQuantity(gameDTO.getQuantity());
        setGenre(gameDTO.getGenre());
    }


}
