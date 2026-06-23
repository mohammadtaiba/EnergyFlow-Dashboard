package de.mohammad.energyflow.dto;

import jakarta.validation.constraints.NotBlank;

public record SiteCreateDto(
    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Type is required")
    String type,

    @NotBlank(message = "Location is required")
    String location
)
{
}