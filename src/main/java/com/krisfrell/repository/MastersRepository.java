package com.krisfrell.repository;

import com.krisfrell.entity.Master;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MastersRepository extends JpaRepository<Master, Long> {
    List<Master> findMastersBySalonIdAndServicesId(Long salonId, Long serviceId);
}
