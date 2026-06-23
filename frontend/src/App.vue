<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
    <div class="app-shell">
        <header class="app-header">
            <div>
                <p class="app-eyebrow">EnergyFlow Dashboard</p>
                <h1>Energy management overview</h1>
                <p class="app-description">
                    Manage sites, meters and energy measurements through one dashboard.
                </p>
            </div>

            <nav>
                <RouterLink to="/sites">Sites</RouterLink>
            </nav>
        </header>

        <main>
            <RouterView />
        </main>
    </div>
</template>