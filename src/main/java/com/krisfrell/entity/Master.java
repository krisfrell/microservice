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
@Table(name = "staff")
public class Master {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "master_id")
    private long id;

    @Column(name = "master")
    private String master;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "master_service", joinColumns = @JoinColumn(name = "master_id"), inverseJoinColumns = @JoinColumn(name = "service_id"))
    Set<Service> services;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "salon_id")
    private Salon salon;

    @OneToMany(mappedBy = "masterId")
    private Set<Appointment> appointment;
}
