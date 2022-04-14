package com.krisfrell.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "salons")
public class Salon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "salon_id")
    private long id;

    @Column(name = "salon")
    private String salon;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "salon_service", joinColumns = @JoinColumn(name = "salon_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    Set<Service> services;

    @OneToMany(mappedBy = "salon")
    Set<Master> masters;
}
