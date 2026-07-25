package de.mohammad.energyflow.service;

import de.mohammad.energyflow.dto.MeterCreateDto;
import de.mohammad.energyflow.dto.MeterDto;
import de.mohammad.energyflow.entity.Meter;
import de.mohammad.energyflow.entity.Site;
import de.mohammad.energyflow.enums.EnergyType;
import de.mohammad.energyflow.exception.ResourceNotFoundException;
import de.mohammad.energyflow.repository.MeterRepository;
import de.mohammad.energyflow.repository.SiteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeterServiceTest
{
    @Mock
    private MeterRepository meterRepository;

    @Mock
    private SiteRepository siteRepository;

    @InjectMocks
    private MeterService meterService;

    @Test
    void createMeterAssignsSiteAndReturnsDto()
    {
        Site site = createSite(1L, "Berlin Office");
        MeterCreateDto input = new MeterCreateDto(1L, "Main meter", "STR-2026-001", EnergyType.ELECTRICITY);

        when(siteRepository.findById(1L)).thenReturn(Optional.of(site));
        when(meterRepository.save(any(Meter.class))).thenAnswer(invocation ->
        {
            Meter meter = invocation.getArgument(0);
            ReflectionTestUtils.setField(meter, "id", 10L);

            return meter;
        });

        MeterDto result = meterService.createMeter(input);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.siteId()).isEqualTo(1L);
        assertThat(result.siteName()).isEqualTo("Berlin Office");
        assertThat(result.name()).isEqualTo("Main meter");
        assertThat(result.meterNumber()).isEqualTo("STR-2026-001");
        assertThat(result.energyType()).isEqualTo(EnergyType.ELECTRICITY);
    }

    @Test
    void getMetersBySiteIdFailsWhenSiteDoesNotExist()
    {
        when(siteRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> meterService.getMetersBySiteId(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Site not found with id: 99");
    }

    @Test
    void deleteMeterFailsWhenMeterDoesNotExist()
    {
        when(meterRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> meterService.deleteMeter(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Meter not found with id: 99");
    }

    @Test
    void getAllMetersMapsSiteData()
    {
        Site site = createSite(1L, "Berlin Office");
        Meter meter = createMeter(20L, site, "Main gas meter", "GAS-2026-001", EnergyType.GAS);

        when(meterRepository.findAll()).thenReturn(List.of(meter));

        List<MeterDto> result = meterService.getAllMeters();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().siteName()).isEqualTo("Berlin Office");
        assertThat(result.getFirst().energyType()).isEqualTo(EnergyType.GAS);
    }

    @Test
    void deleteMeterRemovesExistingMeter()
    {
        Site site = createSite(1L, "Berlin Office");
        Meter meter = createMeter(20L, site, "Main gas meter", "GAS-2026-001", EnergyType.GAS);

        when(meterRepository.findById(20L)).thenReturn(Optional.of(meter));

        meterService.deleteMeter(20L);

        verify(meterRepository).delete(meter);
    }

    private Site createSite(Long id, String name)
    {
        Site site = new Site();

        ReflectionTestUtils.setField(site, "id", id);
        site.setName(name);
        site.setType("OFFICE");
        site.setLocation("Berlin");

        return site;
    }

    private Meter createMeter(Long id, Site site, String name, String meterNumber, EnergyType energyType)
    {
        Meter meter = new Meter();

        ReflectionTestUtils.setField(meter, "id", id);
        meter.setSite(site);
        meter.setName(name);
        meter.setMeterNumber(meterNumber);
        meter.setEnergyType(energyType);

        return meter;
    }
}
