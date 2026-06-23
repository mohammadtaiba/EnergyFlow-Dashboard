# Architektur

Dieses Dokument beschreibt die technische Architektur von **EnergyFlow Dashboard**.

---

## Überblick

Die Anwendung nutzt eine klassische Full-Stack-Architektur mit klarer Trennung zwischen Frontend, Backend und Datenbank.

```text
Frontend: Vue + TypeScript
|
v
REST-API
|
v
Backend: Java Spring Boot
|
|--- Controller Layer
|--- Service Layer
|--- Repository Layer
|--- Entity Layer
|--- DTO Layer
|--- Validation Layer
|--- Exception Handling
|
v
Database: PostgreSQL
```

---

## Architekturprinzipien

Das Projekt folgt diesen Grundsätzen:

* klare Trennung von Verantwortlichkeiten
* einfache und verständliche Struktur
* REST-basierte Kommunikation
* DTOs für kontrollierten Datenaustausch
* Service Layer für Geschäftslogik
* Repository Layer für Datenbankzugriff
* eigene Fehlerklassen für Fehlerfälle
* PostgreSQL als relationale Datenbasis

---

## Frontend

Das Frontend basiert auf Vue 3 und TypeScript.

Aufgaben des Frontends:

* Seiten und Komponenten anzeigen
* Formulare bereitstellen
* Benutzereingaben validieren
* REST-API über Axios aufrufen
* Daten tabellarisch darstellen
* Dashboard-Kennzahlen und Diagramme anzeigen

### Frontend-Struktur

```text
frontend
|
|--- src
     |
     |--- App.vue
     |--- main.ts
     |
     |--- router
     |    |--- index.ts
     |
     |--- views
     |    |--- DashboardView.vue
     |    |--- SitesView.vue
     |    |--- MetersView.vue
     |    |--- MeasurementsView.vue
     |    |--- AlertsView.vue
     |
     |--- components
     |    |
     |    |--- layout
     |    |    |--- HeaderComponent.vue
     |    |    |--- SidebarComponent.vue
     |    |
     |    |--- dashboard
     |    |    |--- EnergySummaryCards.vue
     |    |    |--- ConsumptionChart.vue
     |    |    |--- ConsumptionBySiteChart.vue
     |    |    |--- AlertOverview.vue
     |    |
     |    |--- sites
     |    |    |--- SiteTable.vue
     |    |    |--- SiteForm.vue
     |    |
     |    |--- meters
     |    |    |--- MeterTable.vue
     |    |    |--- MeterForm.vue
     |    |
     |    |--- measurements
     |    |    |--- MeasurementTable.vue
     |    |    |--- MeasurementForm.vue
     |    |    |--- MeasurementFilter.vue
     |
     |--- services
     |    |--- api.ts
     |    |--- siteService.ts
     |    |--- meterService.ts
     |    |--- measurementService.ts
     |    |--- thresholdService.ts
     |    |--- dashboardService.ts
     |
     |--- types
          |--- site.ts
          |--- meter.ts
          |--- measurement.ts
          |--- threshold.ts
          |--- dashboard.ts
          |--- alert.ts
```

### Views

```text
DashboardView.vue        // Übersicht mit Kennzahlen, Diagrammen und Warnungen
SitesView.vue            // Verwaltung der Standorte
MetersView.vue           // Verwaltung der Energiezähler
MeasurementsView.vue     // Verwaltung und Filterung der Messwerte
AlertsView.vue           // Übersicht auffälliger Messwerte
```

### Components

```text
SiteTable.vue                 // Tabelle für Standorte
SiteForm.vue                  // Formular für Standortdaten
MeterTable.vue                // Tabelle für Energiezähler
MeterForm.vue                 // Formular für Zählerdaten
MeasurementFilter.vue         // Filter für Standort, Zähler und Zeitraum
ConsumptionChart.vue          // Diagramm für Verbrauchsentwicklung
ConsumptionBySiteChart.vue    // Diagramm für Verbrauch pro Standort
AlertOverview.vue             // Anzeige aktueller Warnungen
```

### Services

```text
api.ts                    // zentrale Axios-Konfiguration und Basis-URL
siteService.ts            // API-Aufrufe für Standorte
meterService.ts           // API-Aufrufe für Energiezähler
measurementService.ts     // API-Aufrufe für Messwerte
thresholdService.ts       // API-Aufrufe für Grenzwerte
dashboardService.ts       // API-Aufrufe für Dashboard-Daten und Warnungen
```

### Types

```text
site.ts                   // TypeScript-Typen für Standorte
meter.ts                  // TypeScript-Typen für Energiezähler
measurement.ts            // TypeScript-Typen für Messwerte
threshold.ts              // TypeScript-Typen für Grenzwerte
dashboard.ts              // TypeScript-Typen für Dashboard-Kennzahlen und Diagrammdaten
alert.ts                  // TypeScript-Typen für Warnungen und auffällige Messwerte
```

---

## Backend

Das Backend basiert auf Java und Spring Boot.

Aufgaben des Backends:

* REST-Endpunkte bereitstellen
* Anfragen validieren
* Geschäftslogik ausführen
* Daten speichern und abrufen
* DTOs erzeugen
* Fehlerfälle sauber behandeln
* Dashboard-Daten aggregieren

### Backend-Struktur

```text
backend
|
|--- src/main/java/de/mohammad/energyflow
     |
     |--- EnergyflowApplication.java
     |
     |--- controller
     |    |--- SiteController.java
     |    |--- MeterController.java
     |    |--- MeasurementController.java
     |    |--- ThresholdController.java
     |    |--- DashboardController.java
     |
     |--- service
     |    |--- SiteService.java
     |    |--- MeterService.java
     |    |--- MeasurementService.java
     |    |--- ThresholdService.java
     |    |--- DashboardService.java
     |
     |--- repository
     |    |--- SiteRepository.java
     |    |--- MeterRepository.java
     |    |--- MeasurementRepository.java
     |    |--- ThresholdRepository.java
     |
     |--- entity
     |    |--- Site.java
     |    |--- Meter.java
     |    |--- Measurement.java
     |    |--- Threshold.java
     |
     |--- dto
     |    |--- SiteDto.java
     |    |--- SiteCreateDto.java
     |    |--- MeterDto.java
     |    |--- MeterCreateDto.java
     |    |--- MeasurementDto.java
     |    |--- MeasurementCreateDto.java
     |    |--- ThresholdDto.java
     |    |--- ThresholdCreateDto.java
     |    |--- DashboardSummaryDto.java
     |    |--- ConsumptionBySiteDto.java
     |    |--- ConsumptionOverTimeDto.java
     |    |--- AlertDto.java
     |
     |--- enums
     |    |--- EnergyType.java
     |    |--- MeasurementStatus.java
     |    |--- ThresholdPeriod.java
     |
     |--- exception
     |    |--- ResourceNotFoundException.java
     |    |--- BadRequestException.java
     |    |--- GlobalExceptionHandler.java
     |
     |--- config
          |--- CorsConfig.java
```

---

## Backend-Layer

### Controller Layer

Controller nehmen HTTP-Anfragen entgegen und geben HTTP-Antworten zurück.

Beispiele:

```text
GET /api/sites
POST /api/sites
GET /api/dashboard/summary
```

Controller enthalten keine Geschäftslogik. Sie leiten Anfragen an Services weiter.

---

### Service Layer

Services enthalten die fachliche Logik.

Beispiele:

* Standorte verwalten
* Messwerte validieren
* Grenzwerte prüfen
* Warnstatus setzen
* Dashboard-Daten berechnen
* DTOs aus Entities erzeugen

---

### Repository Layer

Repositories kapseln den Datenbankzugriff.

Beispiel:

```java
public interface SiteRepository extends JpaRepository<Site, Long>
{
}
```

---

### Entity Layer

Entities bilden Datenbanktabellen im Java-Code ab.

Beispiel:

```java
@Entity
@Table(name = "sites")
public class Site
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
```

---

### DTO Layer

DTOs übertragen nur die Daten, die Frontend oder API wirklich benötigen.

Beispiel:

```java
public record SiteDto(
    Long id,
    String name,
    String type,
    String location
)
{
}
```

---

## Datenfluss

### Beispiel: Standort anlegen

```text
Frontend
|
v
POST /api/sites
|
v
SiteController
|
v
SiteService
|
v
SiteRepository
|
v
PostgreSQL
```

### Beispiel: Standorte abrufen

```text
PostgreSQL
|
v
SiteRepository
|
v
SiteService
|
v
SiteController
|
v
Frontend
```

---

## Optionale spätere Erweiterung

Ein Go-Service kann später für Import- oder Hintergrundprozesse ergänzt werden.

```text
CSV-Datei
|
v
Go Import Service
|
v
Spring Boot REST-API
|
v
PostgreSQL
|
v
Vue Frontend
```

Der Go-Service soll nicht direkt an der Geschäftslogik vorbeischreiben, sondern die Spring-Boot-REST-API nutzen. So bleiben Validierung und Datenlogik zentral im Backend.