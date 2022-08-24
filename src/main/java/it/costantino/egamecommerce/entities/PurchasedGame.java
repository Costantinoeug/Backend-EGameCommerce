package it.costantino.egamecommerce.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="purchased_game", schema="egamecommercedb")
public class PurchasedGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;


    @Basic
    @Column(name="quantity")
    private Integer quantity;


    @Basic
    @JoinColumn(name="price")
    private float price;

    @ManyToOne
    @JoinColumn(name="related_purchase", referencedColumnName = "id")
    private Purchase relatedPurchase;

    @Column(name = "by_booking")
    private boolean boughtByBooking;

    @ManyToOne
    @JoinColumn(name="related_game", referencedColumnName = "name")
    private Game relatedGame;

    @Column(name="version")
    @Version
    private Long version;


}
