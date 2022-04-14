package com.krisfrell.repository;

import com.krisfrell.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAppointmentsByMasterId(Long masterId);

    List<Appointment> findAppointmentsByUserId(Long userId);
}
