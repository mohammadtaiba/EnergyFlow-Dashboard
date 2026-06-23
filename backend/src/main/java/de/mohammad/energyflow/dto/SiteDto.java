package de.mohammad.energyflow.dto;

public record SiteDto(
    Long id,
    String name,
    String type,
    String location
)
{
}