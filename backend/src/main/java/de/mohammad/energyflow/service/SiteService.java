package de.mohammad.energyflow.service;

import de.mohammad.energyflow.dto.SiteCreateDto;
import de.mohammad.energyflow.dto.SiteDto;
import de.mohammad.energyflow.entity.Site;
import de.mohammad.energyflow.exception.ResourceNotFoundException;
import de.mohammad.energyflow.repository.SiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Handles the business logic for site management.
 * Keeps persistence logic separated from the controller and exposes DTOs instead of entities.
 */
@Service
public class SiteService
{
    private final SiteRepository siteRepository;

    public SiteService(SiteRepository siteRepository)
    {
        this.siteRepository = siteRepository;
    }

    /**
     * Returns all sites as DTOs to avoid exposing database entities to the API layer.
     */
    public List<SiteDto> getAllSites()
    {
        return siteRepository.findAll()
            .stream()
            .map(this::toDto)
            .toList();
    }

    /**
     * Returns a single site by id and throws a custom exception when it does not exist.
     */
    public SiteDto getSiteById(Long id)
    {
        Site site = findSiteById(id);

        return toDto(site);
    }

    /**
     * Creates a new site inside the service to keep the controller free from persistence details.
     */
    public SiteDto createSite(SiteCreateDto siteCreateDto)
    {
        Site site = new Site();

        site.setName(siteCreateDto.name());
        site.setType(siteCreateDto.type());
        site.setLocation(siteCreateDto.location());

        Site savedSite = siteRepository.save(site);

        return toDto(savedSite);
    }

    /**
     * Loads the site first to ensure that only existing records can be updated.
     */
    public SiteDto updateSite(Long id, SiteCreateDto siteCreateDto)
    {
        Site site = findSiteById(id);

        site.setName(siteCreateDto.name());
        site.setType(siteCreateDto.type());
        site.setLocation(siteCreateDto.location());

        Site updatedSite = siteRepository.save(site);

        return toDto(updatedSite);
    }

    /**
     * Loads the site before deletion to return a clear error when the id does not exist.
     */
    public void deleteSite(Long id)
    {
        Site site = findSiteById(id);

        siteRepository.delete(site);
    }

    /**
     * Centralizes lookup logic so missing sites are handled consistently.
     */
    private Site findSiteById(Long id)
    {
        return siteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + id));
    }

    /**
     * Helper method to convert a Site entity to a SiteDto.
     * Keeps the API response independent from the database model.
     */
    private SiteDto toDto(Site site)
    {
        return new SiteDto(
            site.getId(),
            site.getName(),
            site.getType(),
            site.getLocation()
        );
    }
}