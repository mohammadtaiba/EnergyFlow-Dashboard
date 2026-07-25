package de.mohammad.energyflow.dto;

import de.mohammad.energyflow.enums.EnergyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MeterCreateDto(
    @NotNull(message = "Site id is required")
    Long siteId,

    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Meter number is required")
    String meterNumber,

    @NotNull(message = "Energy type is required")
    EnergyType energyType
)
{
}
