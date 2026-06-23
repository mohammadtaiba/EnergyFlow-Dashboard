package de.mohammad.energyflow.repository;

import de.mohammad.energyflow.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long>
{
}
