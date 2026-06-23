package de.mohammad.energyflow.controller;

import de.mohammad.energyflow.dto.SiteCreateDto;
import de.mohammad.energyflow.dto.SiteDto;
import de.mohammad.energyflow.service.SiteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController
{
    private final SiteService siteService;

    public SiteController(SiteService siteService)
    {
        this.siteService = siteService;
    }

    @GetMapping
    public List<SiteDto> getAllSites()
    {
        return siteService.getAllSites();
    }

    @GetMapping("/{id}")
    public SiteDto getSiteById(@PathVariable Long id)
    {
        return siteService.getSiteById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SiteDto createSite(@Valid @RequestBody SiteCreateDto siteCreateDto)
    {
        return siteService.createSite(siteCreateDto);
    }

    @PutMapping("/{id}")
    public SiteDto updateSite(@PathVariable Long id, @Valid @RequestBody SiteCreateDto siteCreateDto)
    {
        return siteService.updateSite(id, siteCreateDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSite(@PathVariable Long id)
    {
        siteService.deleteSite(id);
    }
}