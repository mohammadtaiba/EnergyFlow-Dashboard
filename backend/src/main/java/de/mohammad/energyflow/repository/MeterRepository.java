package de.mohammad.energyflow.repository;

import de.mohammad.energyflow.entity.Meter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeterRepository extends JpaRepository<Meter, Long>
{
    List<Meter> findBySiteId(Long siteId);
}
