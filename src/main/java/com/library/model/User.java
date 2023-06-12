package com.library.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "phone_number"}))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;
    
    private String username;
    
    @Column(name = "phone_number")
    private String phoneNumber;
   
    private String address;

    private String password;
    
    @OneToOne(mappedBy = "user")
    private ShoppingCart shoppingCart;
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable( name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private List<Role> roles;
}