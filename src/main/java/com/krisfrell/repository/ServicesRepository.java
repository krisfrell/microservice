package com.krisfrell.repository;

import com.krisfrell.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicesRepository extends JpaRepository<Service, Long> {
    List<Service> findServicesBySalonsId(Long salonId);
}
