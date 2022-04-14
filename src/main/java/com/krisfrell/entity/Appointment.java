package com.krisfrell.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "appointment_id")
    private long id;

    @Column(name = "datetime")
    private long dateTime;

    @Column(name = "salon")
    private long salon;

    private long masterId;

    @Column(name = "service")
    private long service;

    private long userId;
}
