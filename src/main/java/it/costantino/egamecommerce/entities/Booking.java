package it.costantino.egamecommerce.entities;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name="booking", schema="egamecommercedb")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;


    @Basic
    @Column(name="quantity")
    private Integer quantity;


    @ManyToOne
    @JoinColumn(name="buyer", referencedColumnName = "id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name="related_game", referencedColumnName = "name")
    private Game relatedGame;




    @Column(name="version")
    @Version
    private Long version;

}
