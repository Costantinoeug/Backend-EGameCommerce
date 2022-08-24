package it.costantino.egamecommerce.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="purchase", schema="egamecommercedb")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;


    @ManyToOne
    @JoinColumn(name="buyer", referencedColumnName = "id")
    private User buyer;

    @Basic
    @Column(name="price")
    private float price;

    @Column(name="version")
    @Version
    private Long version;

    @OneToMany(mappedBy = "relatedPurchase")
    private List<PurchasedGame> purchasedGames;

}
