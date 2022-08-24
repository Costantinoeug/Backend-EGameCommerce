package it.costantino.egamecommerce.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="game_in_cart",schema="egamecommercedb")
public class GameInCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Basic
    @Column(name="quantity")
    private Integer quantity;

    @Basic
    @Column(name="price")
    private Float price;

    @ManyToOne
    @JoinColumn(name="related_cart", referencedColumnName = "id")
    private Cart relatedCart;

    @ManyToOne
    @JoinColumn(name="related_game",referencedColumnName = "name")
    private Game relatedGame;

    @Column(name="version")
    @Version
    private Long version;
}

