export async function handleResponse<T>(response: Response): Promise<T>
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
