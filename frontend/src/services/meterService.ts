import type { Meter, MeterInput } from '@/models/meter'
import { handleResponse } from '@/services/apiClient'

const API_BASE_URL = '/api/meters'

export async function getMeters(): Promise<Meter[]>
{
    const response = await fetch(API_BASE_URL)

    return handleResponse<Meter[]>(response)
}

export async function createMeter(meter: MeterInput): Promise<Meter>
{
    const response = await fetch(API_BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(meter),
    })

    return handleResponse<Meter>(response)
}

export async function updateMeter(id: number, meter: MeterInput): Promise<Meter>
{
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(meter),
    })

    return handleResponse<Meter>(response)
}

export async function deleteMeter(id: number): Promise<void>
{
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'DELETE',
    })

    await handleResponse<void>(response)
}
