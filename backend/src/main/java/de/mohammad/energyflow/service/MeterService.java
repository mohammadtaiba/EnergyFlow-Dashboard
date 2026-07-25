package de.mohammad.energyflow.service;

import de.mohammad.energyflow.dto.MeterCreateDto;
import de.mohammad.energyflow.dto.MeterDto;
import de.mohammad.energyflow.entity.Meter;
import de.mohammad.energyflow.entity.Site;
import de.mohammad.energyflow.exception.ResourceNotFoundException;
import de.mohammad.energyflow.repository.MeterRepository;
import de.mohammad.energyflow.repository.SiteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeterService
{
    private final MeterRepository meterRepository;
    private final SiteRepository siteRepository;

    public MeterService(MeterRepository meterRepository, SiteRepository siteRepository)
    {
        this.meterRepository = meterRepository;
        this.siteRepository = siteRepository;
    }

    public List<MeterDto> getAllMeters()
    {
        return meterRepository.findAll()
            .stream()
            .map(this::toDto)
            .toList();
    }

    public List<MeterDto> getMetersBySiteId(Long siteId)
    {
        ensureSiteExists(siteId);

        return meterRepository.findBySiteId(siteId)
            .stream()
            .map(this::toDto)
            .toList();
    }

    public MeterDto getMeterById(Long id)
    {
        Meter meter = findMeterById(id);

        return toDto(meter);
    }

    @Transactional
    public MeterDto createMeter(MeterCreateDto meterCreateDto)
    {
        Site site = findSiteById(meterCreateDto.siteId());
        Meter meter = new Meter();

        applyInput(meter, site, meterCreateDto);

        Meter savedMeter = meterRepository.save(meter);

        return toDto(savedMeter);
    }

    @Transactional
    public MeterDto updateMeter(Long id, MeterCreateDto meterCreateDto)
    {
        Meter meter = findMeterById(id);
        Site site = findSiteById(meterCreateDto.siteId());

        applyInput(meter, site, meterCreateDto);

        Meter updatedMeter = meterRepository.save(meter);

        return toDto(updatedMeter);
    }

    @Transactional
    public void deleteMeter(Long id)
    {
        Meter meter = findMeterById(id);

        meterRepository.delete(meter);
    }

    private void applyInput(Meter meter, Site site, MeterCreateDto meterCreateDto)
    {
        meter.setSite(site);
        meter.setName(meterCreateDto.name());
        meter.setMeterNumber(meterCreateDto.meterNumber());
        meter.setEnergyType(meterCreateDto.energyType());
    }

    private void ensureSiteExists(Long siteId)
    {
        if (!siteRepository.existsById(siteId))
        {
            throw new ResourceNotFoundException("Site not found with id: " + siteId);
        }
    }

    private Site findSiteById(Long siteId)
    {
        return siteRepository.findById(siteId)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + siteId));
    }

    private Meter findMeterById(Long id)
    {
        return meterRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Meter not found with id: " + id));
    }

    private MeterDto toDto(Meter meter)
    {
        Site site = meter.getSite();

        return new MeterDto(
            meter.getId(),
            site.getId(),
            site.getName(),
            meter.getName(),
            meter.getMeterNumber(),
            meter.getEnergyType()
        );
    }
}
