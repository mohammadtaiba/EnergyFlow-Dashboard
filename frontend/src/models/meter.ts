export type EnergyType = 'ELECTRICITY' | 'GAS' | 'WATER' | 'HEATING'

export interface Meter
{
    id: number
    siteId: number
    siteName: string
    name: string
    meterNumber: string
    energyType: EnergyType
}

export interface MeterInput
{
    siteId: number | null
    name: string
    meterNumber: string
    energyType: EnergyType | ''
}
