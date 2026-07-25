package de.mohammad.energyflow.dto;

import de.mohammad.energyflow.enums.EnergyType;

public record MeterDto(
    Long id,
    Long siteId,
    String siteName,
    String name,
    String meterNumber,
    EnergyType energyType
)
{
}
