package it.costantino.egamecommerce.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="cart", schema="egamecommercedb")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @OneToMany(mappedBy = "relatedCart")
    private List<GameInCart> relatedGamesInCart;

    @OneToOne
    @JoinColumn(name="related_user", referencedColumnName = "id")
    private User relatedUser;

    @Basic
    @Column(name="price")
    private float price;

    @Version
    private long version;


}
