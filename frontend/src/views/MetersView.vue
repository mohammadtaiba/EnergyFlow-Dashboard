<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { EnergyType, Meter, MeterInput } from '@/models/meter'
import type { Site } from '@/models/site'
import { createMeter, deleteMeter, getMeters, updateMeter } from '@/services/meterService'
import { getSites } from '@/services/siteService'

const energyTypes: EnergyType[] = ['ELECTRICITY', 'GAS', 'WATER', 'HEATING']

const meters         = ref<Meter[]>([])
const sites          = ref<Site[]>([])
const form           = ref<MeterInput>(createEmptyForm())
const editingMeterId = ref<number | null>(null)
const isLoading      = ref(false)
const isSaving       = ref(false)
const errorMessage   = ref('')
const successMessage = ref('')

const isEditing   = computed(() => editingMeterId.value !== null)
const formTitle   = computed(() => isEditing.value ? 'Edit meter' : 'Create meter')
const submitLabel = computed(() => isEditing.value ? 'Save changes' : 'Create meter')

onMounted(async () =>
{
    await loadPageData()
})

function createEmptyForm(): MeterInput
{
    return {
        siteId: null,
        name: '',
        meterNumber: '',
        energyType: '',
    }
}

async function loadPageData(): Promise<void>
{
    isLoading.value = true
    errorMessage.value = ''

    try
    {
        const [siteData, meterData] = await Promise.all([
            getSites(),
            getMeters(),
        ])

        sites.value = siteData
        meters.value = meterData
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

    const meter = normalizeForm()

    if (!isValid(meter))
    {
        errorMessage.value = 'Site, name, meter number and energy type are required.'

        return
    }

    isSaving.value = true

    try
    {
        if (editingMeterId.value === null)
        {
            await createMeter(meter)
            successMessage.value = 'Meter created successfully.'
        }
        else
        {
            await updateMeter(editingMeterId.value, meter)
            successMessage.value = 'Meter updated successfully.'
        }

        resetForm()
        await loadPageData()
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

function editMeter(meter: Meter): void
{
    editingMeterId.value = meter.id

    form.value = {
        siteId: meter.siteId,
        name: meter.name,
        meterNumber: meter.meterNumber,
        energyType: meter.energyType,
    }

    errorMessage.value = ''
    successMessage.value = ''
}

async function removeMeter(meter: Meter): Promise<void>
{
    const shouldDelete = window.confirm(`Delete meter "${meter.name}"?`)

    if (!shouldDelete)
    {
        return
    }

    errorMessage.value = ''
    successMessage.value = ''

    try
    {
        await deleteMeter(meter.id)

        if (editingMeterId.value === meter.id)
        {
            resetForm()
        }

        successMessage.value = 'Meter deleted successfully.'
        await loadPageData()
    }
    catch (error)
    {
        errorMessage.value = getMessage(error)
    }
}

function resetForm(): void
{
    editingMeterId.value = null
    form.value = createEmptyForm()
}

function normalizeForm(): MeterInput
{
    return {
        siteId: form.value.siteId,
        name: form.value.name.trim(),
        meterNumber: form.value.meterNumber.trim(),
        energyType: form.value.energyType,
    }
}

function isValid(meter: MeterInput): meter is MeterInput & { siteId: number; energyType: EnergyType }
{
    return meter.siteId !== null
        && meter.name.length > 0
        && meter.meterNumber.length > 0
        && meter.energyType.length > 0
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
    <section class="meters-page">
        <div class="meters-toolbar">
            <div>
                <p class="eyebrow">Meter Management</p>
                <h2>Meters</h2>
                <p>
                    Assign energy meters to sites and manage their technical identifiers.
                </p>
            </div>

            <button
                class="secondary-button"
                :disabled="isLoading"
                type="button"
                @click="loadPageData"
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

        <div class="meters-layout">
            <section class="card form-card">
                <h3>{{ formTitle }}</h3>
                <p>
                    Select a site, define the meter and save it through the backend API.
                </p>

                <form class="meter-form" @submit.prevent="submitForm">
                    <div class="form-field">
                        <label for="meter-site">Site</label>
                        <select
                            id="meter-site"
                            v-model.number="form.siteId"
                            :disabled="sites.length === 0"
                        >
                            <option :value="null">
                                Select site
                            </option>
                            <option
                                v-for="site in sites"
                                :key="site.id"
                                :value="site.id"
                            >
                                {{ site.name }}
                            </option>
                        </select>
                    </div>

                    <div class="form-field">
                        <label for="meter-name">Name</label>
                        <input
                            id="meter-name"
                            v-model="form.name"
                            autocomplete="off"
                            placeholder="Main electricity meter"
                            type="text"
                        />
                    </div>

                    <div class="form-field">
                        <label for="meter-number">Meter number</label>
                        <input
                            id="meter-number"
                            v-model="form.meterNumber"
                            autocomplete="off"
                            placeholder="STR-2026-001"
                            type="text"
                        />
                    </div>

                    <div class="form-field">
                        <label for="meter-energy-type">Energy type</label>
                        <select id="meter-energy-type" v-model="form.energyType">
                            <option value="">
                                Select energy type
                            </option>
                            <option
                                v-for="energyType in energyTypes"
                                :key="energyType"
                                :value="energyType"
                            >
                                {{ energyType }}
                            </option>
                        </select>
                    </div>

                    <div class="button-row">
                        <button
                            class="primary-button"
                            :disabled="isSaving || sites.length === 0"
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
                        <h3>Existing meters</h3>
                        <p>
                            {{ meters.length }} meter{{ meters.length === 1 ? '' : 's' }} available.
                        </p>
                    </div>
                </div>

                <div v-if="isLoading" class="empty-state">
                    Loading meters...
                </div>

                <div v-else-if="sites.length === 0" class="empty-state">
                    Create a site before adding meters.
                </div>

                <div v-else-if="meters.length === 0" class="empty-state">
                    No meters found. Create your first meter.
                </div>

                <div v-else class="meter-list">
                    <article
                        v-for="meter in meters"
                        :key="meter.id"
                        class="meter-item"
                    >
                        <div>
                            <h4>{{ meter.name }}</h4>

                            <div class="meter-meta">
                                <span class="meter-tag">{{ meter.energyType }}</span>
                                <span class="meter-number">{{ meter.meterNumber }}</span>
                                <span class="meter-site">{{ meter.siteName }}</span>
                            </div>
                        </div>

                        <div class="button-row">
                            <button
                                class="secondary-button"
                                type="button"
                                @click="editMeter(meter)"
                            >
                                Edit
                            </button>

                            <button
                                class="danger-button"
                                type="button"
                                @click="removeMeter(meter)"
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
