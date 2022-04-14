package com.krisfrell.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "service_id")
    private long id;

    @Column(name = "service")
    private String service;

    @JsonIgnore
    @ManyToMany(mappedBy = "services")
    Set<Salon> salons;

    @JsonIgnore
    @ManyToMany(mappedBy = "services")
    Set<Master> masters;
}
