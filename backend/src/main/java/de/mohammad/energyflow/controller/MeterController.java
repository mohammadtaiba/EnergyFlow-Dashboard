package de.mohammad.energyflow.controller;

import de.mohammad.energyflow.dto.MeterCreateDto;
import de.mohammad.energyflow.dto.MeterDto;
import de.mohammad.energyflow.service.MeterService;
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
@RequestMapping("/api")
public class MeterController
{
    private final MeterService meterService;

    public MeterController(MeterService meterService)
    {
        this.meterService = meterService;
    }

    @GetMapping("/meters")
    public List<MeterDto> getAllMeters()
    {
        return meterService.getAllMeters();
    }

    @GetMapping("/meters/{id}")
    public MeterDto getMeterById(@PathVariable Long id)
    {
        return meterService.getMeterById(id);
    }

    @GetMapping("/sites/{siteId}/meters")
    public List<MeterDto> getMetersBySiteId(@PathVariable Long siteId)
    {
        return meterService.getMetersBySiteId(siteId);
    }

    @PostMapping("/meters")
    @ResponseStatus(HttpStatus.CREATED)
    public MeterDto createMeter(@Valid @RequestBody MeterCreateDto meterCreateDto)
    {
        return meterService.createMeter(meterCreateDto);
    }

    @PutMapping("/meters/{id}")
    public MeterDto updateMeter(@PathVariable Long id, @Valid @RequestBody MeterCreateDto meterCreateDto)
    {
        return meterService.updateMeter(id, meterCreateDto);
    }

    @DeleteMapping("/meters/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMeter(@PathVariable Long id)
    {
        meterService.deleteMeter(id);
    }
}
