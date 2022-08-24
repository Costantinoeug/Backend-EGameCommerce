package it.costantino.egamecommerce.entities;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="user", schema="egamecommercedb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Basic
    @Column(name="username", unique = true, length = 70)
    private String username;

    @OneToMany(mappedBy = "buyer")
    private List<Purchase> purchases;

    @OneToMany(mappedBy = "buyer")
    private List<Booking> bookings;

    @OneToOne(mappedBy = "relatedUser")
    private Cart relatedCart;
}
