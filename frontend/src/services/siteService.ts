import type { Site, SiteInput } from '@/models/site'

const API_BASE_URL = '/api/sites'

export async function getSites(): Promise<Site[]>
{
    const response = await fetch(API_BASE_URL)

    return handleResponse<Site[]>(response)
}

export async function createSite(site: SiteInput): Promise<Site>
{
    const response = await fetch(API_BASE_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(site),
    })

    return handleResponse<Site>(response)
}

export async function updateSite(id: number, site: SiteInput): Promise<Site>
{
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(site),
    })

    return handleResponse<Site>(response)
}

export async function deleteSite(id: number): Promise<void>
{
    const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'DELETE',
    })

    await handleResponse<void>(response)
}

async function handleResponse<T>(response: Response): Promise<T>
{
    if (!response.ok)
    {
        throw new Error(await getErrorMessage(response))
    }

    if (response.status === 204)
    {
        return undefined as T
    }

    return response.json() as Promise<T>
}

async function getErrorMessage(response: Response): Promise<string>
{
    const fallbackMessage = `Request failed with status ${response.status}`
    const text = await response.text()

    if (!text)
    {
        return fallbackMessage
    }

    try
    {
        const errorBody = JSON.parse(text) as {
            message?: string
            error?: string
            title?: string
        }

        return errorBody.message ?? errorBody.error ?? errorBody.title ?? fallbackMessage
    }
    catch
    {
        return text
    }
}