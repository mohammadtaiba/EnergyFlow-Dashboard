<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { Site, SiteInput } from '@/models/site'
import { createSite, deleteSite, getSites, updateSite } from '@/services/siteService'

const sites          = ref<Site[]>([])
const form           = ref<SiteInput>(createEmptyForm())
const editingSiteId  = ref<number | null>(null)
const isLoading      = ref(false)
const isSaving       = ref(false)
const errorMessage   = ref('')
const successMessage = ref('')

const isEditing     = computed(() => editingSiteId.value !== null)
const formTitle     = computed(() => isEditing.value ? 'Edit site' : 'Create site')
const submitLabel   = computed(() => isEditing.value ? 'Save changes' : 'Create site')

onMounted(async () =>
{
    await loadSites()
})

function createEmptyForm(): SiteInput
{
    return {
        name: '',
        type: '',
        location: '',
    }
}

async function loadSites(): Promise<void>
{
    isLoading.value = true
    errorMessage.value = ''

    try
    {
        sites.value = await getSites()
    }
    catch (error)
    {
        errorMessage.value = getMessage(error)
    }
    finally
    {
        isLoading.value = false
    }
}

async function submitForm(): Promise<void>
{
    errorMessage.value = ''
    successMessage.value = ''

    const site = normalizeForm()

    if (!isValid(site))
    {
        errorMessage.value = 'Name, type and location are required.'

        return
    }

    isSaving.value = true

    try
    {
        if (editingSiteId.value === null)
        {
            await createSite(site)
            successMessage.value = 'Site created successfully.'
        }
        else
        {
            await updateSite(editingSiteId.value, site)
            successMessage.value = 'Site updated successfully.'
        }

        resetForm()
        await loadSites()
    }
    catch (error)
    {
        errorMessage.value = getMessage(error)
    }
    finally
    {
        isSaving.value = false
    }
}

function editSite(site: Site): void
{
    editingSiteId.value = site.id

    form.value = {
        name: site.name,
        type: site.type,
        location: site.location,
    }

    errorMessage.value = ''
    successMessage.value = ''
}

async function removeSite(site: Site): Promise<void>
{
    const shouldDelete = window.confirm(`Delete site "${site.name}"?`)

    if (!shouldDelete)
    {
        return
    }

    errorMessage.value = ''
    successMessage.value = ''

    try
    {
        await deleteSite(site.id)

        if (editingSiteId.value === site.id)
        {
            resetForm()
        }

        successMessage.value = 'Site deleted successfully.'
        await loadSites()
    }
    catch (error)
    {
        errorMessage.value = getMessage(error)
    }
}

function resetForm(): void
{
    editingSiteId.value = null
    form.value = createEmptyForm()
}

function normalizeForm(): SiteInput
{
    return {
        name: form.value.name.trim(),
        type: form.value.type.trim(),
        location: form.value.location.trim(),
    }
}

function isValid(site: SiteInput): boolean
{
    return site.name.length > 0 && site.type.length > 0 && site.location.length > 0
}

function getMessage(error: unknown): string
{
    if (error instanceof Error)
    {
        return error.message
    }

    return 'Unexpected error.'
}
</script>

<template>
    <section class="sites-page">
        <div class="sites-toolbar">
            <div>
                <p class="eyebrow">Site Management</p>
                <h2>Sites</h2>
                <p>
                    Create, update and delete energy monitoring locations.
                </p>
            </div>

            <button
                class="secondary-button"
                :disabled="isLoading"
                type="button"
                @click="loadSites"
            >
                {{ isLoading ? 'Loading...' : 'Refresh' }}
            </button>
        </div>

        <p v-if="errorMessage" class="message error">
            {{ errorMessage }}
        </p>

        <p v-if="successMessage" class="message success">
            {{ successMessage }}
        </p>

        <div class="sites-layout">
            <section class="card form-card">
                <h3>{{ formTitle }}</h3>
                <p>
                    Fill in the site data and save it through the backend API.
                </p>

                <form class="site-form" @submit.prevent="submitForm">
                    <div class="form-field">
                        <label for="site-name">Name</label>
                        <input
                            id="site-name"
                            v-model="form.name"
                            autocomplete="off"
                            placeholder="Berlin Office"
                            type="text"
                        />
                    </div>

                    <div class="form-field">
                        <label for="site-type">Type</label>
                        <input
                            id="site-type"
                            v-model="form.type"
                            autocomplete="off"
                            placeholder="Office"
                            type="text"
                        />
                    </div>

                    <div class="form-field">
                        <label for="site-location">Location</label>
                        <input
                            id="site-location"
                            v-model="form.location"
                            autocomplete="off"
                            placeholder="Berlin"
                            type="text"
                        />
                    </div>

                    <div class="button-row">
                        <button
                            class="primary-button"
                            :disabled="isSaving"
                            type="submit"
                        >
                            {{ isSaving ? 'Saving...' : submitLabel }}
                        </button>

                        <button
                            v-if="isEditing"
                            class="ghost-button"
                            type="button"
                            @click="resetForm"
                        >
                            Cancel
                        </button>
                    </div>
                </form>
            </section>

            <section class="card list-card">
                <div class="list-header">
                    <div>
                        <h3>Existing sites</h3>
                        <p>
                            {{ sites.length }} site{{ sites.length === 1 ? '' : 's' }} available.
                        </p>
                    </div>
                </div>

                <div v-if="isLoading" class="empty-state">
                    Loading sites...
                </div>

                <div v-else-if="sites.length === 0" class="empty-state">
                    No sites found. Create your first site.
                </div>

                <div v-else class="site-list">
                    <article
                        v-for="site in sites"
                        :key="site.id"
                        class="site-item"
                    >
                        <div>
                            <h4>{{ site.name }}</h4>

                            <div class="site-meta">
                                <span class="site-tag">{{ site.type }}</span>
                                <span class="site-location">{{ site.location }}</span>
                            </div>
                        </div>

                        <div class="button-row">
                            <button
                                class="secondary-button"
                                type="button"
                                @click="editSite(site)"
                            >
                                Edit
                            </button>

                            <button
                                class="danger-button"
                                type="button"
                                @click="removeSite(site)"
                            >
                                Delete
                            </button>
                        </div>
                    </article>
                </div>
            </section>
        </div>
    </section>
</template>