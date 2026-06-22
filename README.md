# EnergyFlow Dashboard

**EnergyFlow Dashboard** ist eine serviceorientierte Full-Stack-Webanwendung zur Erfassung, Verwaltung, Analyse und Visualisierung von Energieverbrauchsdaten.

Das Projekt zeigt den kompletten Ablauf einer modernen Webanwendung: von der Benutzeroberfläche über eine REST-API bis zur relationalen PostgreSQL-Datenbank. Der fachliche Fokus liegt auf Energiemanagement, Standortverwaltung, Energiezählern, Messwerten, Grenzwerten, Warnlogik und einfachen Auswertungen.

---

## Inhaltsverzeichnis

* [Projektziel](#projektziel)
* [Beispiel-Szenario](#beispiel-szenario)
* [Kernfunktionen](#kernfunktionen)
* [Tech-Stack](#tech-stack)
* [Architektur](#architektur)
* [Frontend-Struktur](#frontend-struktur)
* [Backend-Struktur](#backend-struktur)
* [Datenbankmodell](#datenbankmodell)
* [REST-API-Endpunkte](#rest-api-endpunkte)
* [Performance und Skalierbarkeit](#performance-und-skalierbarkeit)
* [Setup](#setup)
* [Projekt lokal starten](#projekt-lokal-starten)
* [Tests](#tests)
* [Beispiel-Use-Case](#beispiel-use-case)
* [Roadmap](#roadmap)
* [Lernziele](#lernziele)
* [Projektstatus](#projektstatus)
* [Autor](#autor)
* [Lizenz](#lizenz)

---

## Projektziel

Ziel des Projekts ist eine Webanwendung, mit der Energieverbrauchsdaten verschiedener Standorte und Energiezähler verwaltet, ausgewertet und visualisiert werden können.

Die Anwendung soll zeigen, wie Frontend, Backend und Datenbank in einer serviceorientierten Architektur zusammenspielen. Zusätzlich werden fachliche Logiken wie Grenzwertprüfung, Warnstatus und Dashboard-Kennzahlen umgesetzt.

---

## Beispiel-Szenario

Ein Stadtwerk oder Unternehmen möchte Energieverbrauchsdaten mehrerer Standorte überwachen.

Jeder Standort besitzt einen oder mehrere Energiezähler. Zu diesen Zählern werden regelmäßig Messwerte gespeichert. Das Dashboard zeigt Kennzahlen, Verbrauchsentwicklungen und Warnungen an. Bei auffälligen Verbrauchswerten erkennt das Backend Grenzwertüberschreitungen und markiert diese automatisch.

Beispiel:

* Standort: Verwaltungsgebäude Ilmenau
* Zähler: Stromzähler Hauptgebäude
* Messwert: 450 kWh am 2026-01-15
* Grenzwert: 400 kWh pro Tag
* Ergebnis: Warnstatus `WARNING`

---

## Kernfunktionen

### Dashboard

* Anzeige des Gesamtverbrauchs
* Anzeige des durchschnittlichen Verbrauchs
* Anzeige des höchsten Verbrauchswerts
* Anzeige der Anzahl aktueller Warnungen
* Verbrauch pro Standort
* Verbrauchsentwicklung über Zeiträume
* Anzeige kritischer oder auffälliger Messwerte

### Standortverwaltung

* Standorte anzeigen
* Standort nach ID abrufen
* Standort anlegen
* Standort bearbeiten
* Standort löschen
* Standorte nach Typ oder Ort filtern

### Zählerverwaltung

* Energiezähler anzeigen
* Energiezähler einem Standort zuordnen
* Energiezähler anlegen
* Energiezähler bearbeiten
* Energiezähler löschen
* Zähler nach Standort filtern

### Messwertverwaltung

* Messwerte erfassen
* Messwerte anzeigen
* Messwerte bearbeiten
* Messwerte löschen
* Messwerte nach Standort filtern
* Messwerte nach Zähler filtern
* Messwerte nach Zeitraum filtern
* Verbrauchswerte speichern und validieren

### Grenzwerte und Warnlogik

* Grenzwerte pro Standort oder Zähler definieren
* automatische Prüfung neuer Messwerte
* Statusvergabe für Messwerte
* Warnung bei Grenzwertüberschreitung
* Anzeige kritischer Werte im Dashboard

Mögliche Statuswerte:

```text
NORMAL
WARNING
CRITICAL
```

---

## Tech-Stack

### Frontend

* Vue 3
* TypeScript
* HTML
* CSS
* Vue Router
* Axios
* Chart.js

### Backend

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Maven
* REST-API
* DTOs
* Bean Validation

### Datenbank

* PostgreSQL

### Tests

* JUnit
* Mockito
* Spring Boot Test

### Entwicklungs- und Projekttools

* Git
* Docker
* Docker Compose
* Postman
* Swagger / OpenAPI

### Geplante Erweiterungen

* Flyway oder Liquibase für Datenbankmigrationen
* vollständiges Docker Compose für Frontend, Backend und Datenbank
* Go-Service für CSV-Import, Benachrichtigungen oder Hintergrundverarbeitung
* Mock-ERP-Service über REST/OData, angelehnt an typische SAP-S/4HANA-Integrationsszenarien


---

## Architektur

Die Anwendung nutzt eine klare Trennung zwischen Frontend, Backend und Datenbank.

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

Optionale spätere Erweiterung:

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

### Erklärung

* **Frontend:** zeigt Daten, Tabellen, Formulare und Diagramme an.
* **REST-API:** verbindet Frontend und Backend über HTTP.
* **Controller:** nimmt HTTP-Anfragen entgegen.
* **Service:** enthält Geschäftslogik, Validierung und Berechnungen.
* **Repository:** kapselt den Datenbankzugriff.
* **Entity:** beschreibt Datenbanktabellen im Java-Code.
* **DTO:** überträgt nur benötigte Daten zwischen Backend und Frontend.
* **PostgreSQL:** speichert Standorte, Zähler, Messwerte und Grenzwerte dauerhaft.
* **Go-Service:** kann später für Import- oder Hintergrundprozesse genutzt werden.

---

## Frontend-Struktur

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
          |--- dashboard.ts
          |--- threshold.ts
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

## Backend-Struktur

```text
backend
|
|--- src/main/java/de/mohammad/energyflow
     |
     |--- EnergyFlowApplication.java
     |
     |--- controller
     |    |--- SiteController.java
     |    |--- MeterController.java
     |    |--- MeasurementController.java
     |    |--- DashboardController.java
     |    |--- ThresholdController.java
     |
     |--- service
     |    |--- SiteService.java
     |    |--- MeterService.java
     |    |--- MeasurementService.java
     |    |--- DashboardService.java
     |    |--- ThresholdService.java
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
     |    |--- MeterDto.java
     |    |--- MeasurementDto.java
     |    |--- MeasurementCreateDto.java
     |    |--- DashboardSummaryDto.java
     |    |--- ConsumptionBySiteDto.java
     |    |--- ConsumptionOverTimeDto.java
     |    |--- ThresholdDto.java
     |    |--- ThresholdCreateDto.java
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

### Controller Layer

Nimmt HTTP-Requests entgegen und gibt HTTP-Responses zurück.

Beispiele:

```text
GET /api/sites
POST /api/measurements
GET /api/dashboard/summary
```

### Service Layer

Enthält die Geschäftslogik.

Beispiele:

* Verbrauchswerte berechnen
* Messwerte validieren
* Grenzwerte prüfen
* Warnstatus setzen
* Dashboard-Daten vorbereiten
* Daten für Diagramme aggregieren

### Repository Layer

Greift auf die PostgreSQL-Datenbank zu.

Beispiel:

```java
public interface SiteRepository extends JpaRepository<Site, Long> {
}
```

Beispiel mit Filter:

```java
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByMeterIdAndMeasuredAtBetween(
        Long meterId,
        LocalDateTime from,
        LocalDateTime to
    );
}
```

### Entity Layer

Beschreibt die Datenbanktabellen im Java-Code.

Beispiel:

```java
@Entity
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String location;

    private LocalDateTime createdAt;
}
```

### DTO Layer

DTOs werden genutzt, um nur benötigte Daten zwischen Backend und Frontend zu übertragen.

Beispiel:

```java
public class SiteDto {
    private Long id;
    private String name;
    private String type;
    private String location;
}
```

---

## Datenbankmodell

Die Datenbank besteht in der MVP-Version aus vier Haupttabellen:

* `sites`
* `meters`
* `measurements`
* `thresholds`

### Tabelle: sites

Speichert Standorte, an denen Energieverbrauch gemessen wird.

```text
sites
|
|--- id
|--- name
|--- type
|--- location
|--- created_at
|--- updated_at
```

Beispiel:

```text
id: 1
name: Verwaltungsgebäude Ilmenau
type: OFFICE
location: Ilmenau
```

---

### Tabelle: meters

Speichert Energiezähler, die einem Standort zugeordnet sind.

```text
meters
|
|--- id
|--- site_id
|--- name
|--- meter_number
|--- energy_type
|--- created_at
|--- updated_at
```

Beispiel:

```text
id: 1
site_id: 1
name: Hauptstromzähler
meter_number: STR-2026-001
energy_type: ELECTRICITY
```

Mögliche Energiearten:

```text
ELECTRICITY
GAS
WATER
HEATING
```

---

### Tabelle: measurements

Speichert Verbrauchswerte zu einem bestimmten Zähler und Zeitpunkt.

```text
measurements
|
|--- id
|--- meter_id
|--- measured_at
|--- consumption_kwh
|--- status
|--- created_at
|--- updated_at
```

Beispiel:

```text
id: 1
meter_id: 1
measured_at: 2026-01-15T10:00:00
consumption_kwh: 450.50
status: WARNING
```

Statuswerte:

```text
NORMAL
WARNING
CRITICAL
```

---

### Tabelle: thresholds

Speichert Grenzwerte für Standorte oder Zähler.

```text
thresholds
|
|--- id
|--- site_id
|--- meter_id
|--- max_consumption_kwh
|--- period
|--- created_at
|--- updated_at
```

Beispiel:

```text
id: 1
site_id: 1
meter_id: 1
max_consumption_kwh: 400.00
period: DAILY
```

Mögliche Perioden:

```text
HOURLY
DAILY
WEEKLY
MONTHLY
```

---

## Beziehungen

```text
Site 1 -------- n Meter
Meter 1 ------- n Measurement
Site 1 -------- n Threshold
Meter 1 ------- n Threshold
```

Erklärung:

* Ein Standort kann mehrere Energiezähler besitzen.
* Ein Energiezähler gehört zu genau einem Standort.
* Ein Energiezähler kann viele Messwerte besitzen.
* Ein Messwert gehört zu genau einem Energiezähler.
* Grenzwerte können für Standorte oder einzelne Zähler definiert werden.

---

## REST-API-Endpunkte

Basis-URL:

```text
http://localhost:8080/api
```

---

### Sites

```text
GET     /api/sites
GET     /api/sites/{id}
POST    /api/sites
PUT     /api/sites/{id}
DELETE  /api/sites/{id}
```

Beispiel für `POST /api/sites`:

```json
{
  "name": "Verwaltungsgebäude Ilmenau",
  "type": "OFFICE",
  "location": "Ilmenau"
}
```

---

### Meters

```text
GET     /api/meters
GET     /api/meters/{id}
GET     /api/sites/{siteId}/meters
POST    /api/meters
PUT     /api/meters/{id}
DELETE  /api/meters/{id}
```

Beispiel für `POST /api/meters`:

```json
{
  "siteId": 1,
  "name": "Hauptstromzähler",
  "meterNumber": "STR-2026-001",
  "energyType": "ELECTRICITY"
}
```

---

### Measurements

```text
GET     /api/measurements
GET     /api/measurements/{id}
POST    /api/measurements
PUT     /api/measurements/{id}
DELETE  /api/measurements/{id}
```

Filter:

```text
GET /api/measurements?siteId=1
GET /api/measurements?meterId=1
GET /api/measurements?from=2026-01-01T00:00:00&to=2026-01-31T23:59:59
GET /api/measurements?siteId=1&from=2026-01-01T00:00:00&to=2026-01-31T23:59:59
```

Beispiel für `POST /api/measurements`:

```json
{
  "meterId": 1,
  "measuredAt": "2026-01-15T10:00:00",
  "consumptionKwh": 450.5
}
```

Beispiel-Antwort:

```json
{
  "id": 1,
  "meterId": 1,
  "siteId": 1,
  "measuredAt": "2026-01-15T10:00:00",
  "consumptionKwh": 450.5,
  "status": "WARNING"
}
```

---

### Thresholds

```text
GET     /api/thresholds
GET     /api/thresholds/{id}
GET     /api/sites/{siteId}/thresholds
GET     /api/meters/{meterId}/thresholds
POST    /api/thresholds
PUT     /api/thresholds/{id}
DELETE  /api/thresholds/{id}
```

Beispiel für `POST /api/thresholds`:

```json
{
  "siteId": 1,
  "meterId": 1,
  "maxConsumptionKwh": 400.0,
  "period": "DAILY"
}
```

---

### Dashboard

```text
GET /api/dashboard/summary
GET /api/dashboard/consumption-by-site
GET /api/dashboard/consumption-over-time
GET /api/dashboard/alerts
```

Beispiel für `GET /api/dashboard/summary`:

```json
{
  "totalConsumptionKwh": 12540.5,
  "averageConsumptionKwh": 418.0,
  "highestConsumptionKwh": 980.0,
  "alertCount": 3
}
```

Beispiel für `GET /api/dashboard/consumption-by-site`:

```json
[
  {
    "siteId": 1,
    "siteName": "Verwaltungsgebäude Ilmenau",
    "totalConsumptionKwh": 4200.5
  },
  {
    "siteId": 2,
    "siteName": "Produktionshalle Erfurt",
    "totalConsumptionKwh": 8340.0
  }
]
```

Beispiel für `GET /api/dashboard/consumption-over-time`:

```json
[
  {
    "date": "2026-01-01",
    "totalConsumptionKwh": 390.0
  },
  {
    "date": "2026-01-02",
    "totalConsumptionKwh": 410.5
  }
]
```

Beispiel für `GET /api/dashboard/alerts`:

```json
[
  {
    "measurementId": 1,
    "siteName": "Verwaltungsgebäude Ilmenau",
    "meterName": "Hauptstromzähler",
    "measuredAt": "2026-01-15T10:00:00",
    "consumptionKwh": 450.5,
    "thresholdKwh": 400.0,
    "status": "WARNING"
  }
]
```

---

## Performance und Skalierbarkeit

Für größere Datenmengen werden folgende Punkte berücksichtigt:

* Pagination für Messwertlisten
* Filterung nach Standort, Zähler und Zeitraum
* Datenbank-Indizes auf häufig genutzten Spalten
* Aggregationen im Backend statt im Frontend
* DTOs für schlanke API-Antworten
* getrennte Service-Schicht für Geschäftslogik
* klare Trennung zwischen UI, API und Datenhaltung

Empfohlene Indizes:

```sql
CREATE INDEX idx_meters_site_id ON meters(site_id);
CREATE INDEX idx_measurements_meter_id ON measurements(meter_id);
CREATE INDEX idx_measurements_measured_at ON measurements(measured_at);
CREATE INDEX idx_measurements_status ON measurements(status);
```

---

## Setup

### Voraussetzungen

* Java 21 oder Java 17
* Maven
* Node.js
* npm
* Docker
* Docker Compose
* PostgreSQL

---

## Projekt lokal starten

### 1. Repository klonen

```bash
git clone https://github.com/USERNAME/energyflow-dashboard.git
cd energyflow-dashboard
```

---

### 2. `.env` anlegen

Datei im Projekt-Root erstellen:

```text
.env
```

Beispiel:

```env
POSTGRES_DB=energyflow
POSTGRES_USER=postgres
POSTGRES_PASSWORD=change-me
POSTGRES_PORT=5432
```

Für öffentliche Repositories sollte nur eine `.env.example` versioniert werden.

Beispiel:

```text
.env.example
```

```env
POSTGRES_DB=energyflow
POSTGRES_USER=postgres
POSTGRES_PASSWORD=change-me
POSTGRES_PORT=5432
```

---

### 3. Datenbank mit Docker starten

```bash
docker compose up -d
```

Beispiel für `docker-compose.yml`:

```yaml
services:
  postgres:
    image: postgres:16
    container_name: energyflow-postgres
    environment:
      POSTGRES_DB: ${POSTGRES_DB}
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    ports:
      - "${POSTGRES_PORT}:5432"
    volumes:
      - energyflow_data:/var/lib/postgresql/data

volumes:
  energyflow_data:
```

Hinweis:

```text
Docker Compose wird in der MVP-Version für PostgreSQL genutzt.
Frontend und Backend werden lokal gestartet.
Eine vollständige Dockerisierung von Frontend, Backend und Datenbank ist für eine spätere Version vorgesehen.
```

---

### 4. Backend konfigurieren

Datei:

```text
backend/src/main/resources/application.yml
```

Beispiel für lokale Entwicklung:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/energyflow
    username: postgres
    password: change-me

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8080
```

Hinweis:

```text
ddl-auto: update ist nur für lokale Entwicklung gedacht.
Für produktionsnahe Projekte sollten Datenbankmigrationen mit Flyway oder Liquibase verwendet werden.
```

---

### 5. Backend starten

```bash
cd backend
mvn spring-boot:run
```

Backend läuft standardmäßig unter:

```text
http://localhost:8080
```

---

### 6. Frontend starten

```bash
cd frontend
npm install
npm run dev
```

Frontend läuft standardmäßig unter:

```text
http://localhost:5173
```

---

## Tests

### Backend-Tests starten

```bash
cd backend
mvn test
```

Getestet werden unter anderem:

* Service-Logik
* Validierung von Messwerten
* Grenzwertprüfung
* Berechnung von Dashboard-Kennzahlen
* Fehlerfälle
* Repository-Methoden

Beispiele für sinnvolle Tests:

```text
MeasurementServiceTest
|
|--- shouldCreateMeasurementWithNormalStatus()
|--- shouldCreateMeasurementWithWarningStatus()
|--- shouldRejectNegativeConsumptionValue()
|--- shouldCalculateTotalConsumption()
|--- shouldFilterMeasurementsByDateRange()
```

---

## Beispiel-Use-Case

1. Nutzer legt einen Standort an.
2. Nutzer legt für diesen Standort einen Energiezähler an.
3. Nutzer definiert einen Grenzwert für den Energiezähler.
4. Nutzer erfasst einen Messwert.
5. Das Backend validiert den Messwert.
6. Das Backend prüft den Messwert gegen den Grenzwert.
7. Das Backend speichert Messwert und Status in PostgreSQL.
8. Das Dashboard ruft aggregierte Daten über die REST-API ab.
9. Das Frontend zeigt Kennzahlen, Diagramme und Warnungen an.

---

## Screenshots

Geplante Screenshot-Struktur:

```text
docs
|
|--- screenshots
     |--- dashboard.png
     |--- sites.png
     |--- meters.png
     |--- measurements.png
     |--- alerts.png
```

Beispiele:

```text
docs/screenshots/dashboard.png
docs/screenshots/sites.png
docs/screenshots/measurements.png
```

---

## Roadmap

### Version 1: MVP

* Vue-Frontend mit mehreren Views
* Spring-Boot-Backend
* PostgreSQL-Datenbank
* CRUD-Funktionen für Standorte
* CRUD-Funktionen für Energiezähler
* CRUD-Funktionen für Messwerte
* einfache Grenzwertlogik
* Dashboard mit Kennzahlen
* Anzeige aktueller Warnungen
* Backend-Unit-Tests

---

### Version 2: Professionalisierung

* Diagramme für verschiedene Zeiträume
* Pagination und Sortierung für Messwertlisten
* Swagger/OpenAPI-Dokumentation
* globale Fehlerbehandlung
* Validierung mit Bean Validation
* Datenbankmigrationen mit Flyway oder Liquibase
* vollständiges Docker Compose für Frontend, Backend und Datenbank
* einfache Authentifizierung und Rollenmodell

---

### Version 3: Erweiterungen

* CSV-Upload für Messwerte
* Export von Messwerten als CSV
* Export von Dashboard-Berichten als PDF
* Benachrichtigungen bei kritischen Grenzwertüberschreitungen
* Mock-ERP-Service zur Simulation externer Systeme
* optionaler Go-Service für CSV-Import oder Hintergrundverarbeitung


---

## Möglicher Go-Service

Ein optionaler Go-Service kann später für Import- oder Hintergrundprozesse ergänzt werden.

Beispiel:

```text
go-import-service
|
|--- liest CSV-Dateien ein
|--- validiert Messwerte
|--- erkennt fehlerhafte Datensätze
|--- schreibt gültige Messwerte in PostgreSQL
|--- gibt Importberichte zurück
```

Beispiel-Endpunkte:

```text
POST /import/csv
GET  /import/status/{id}
GET  /import/reports/{id}
```

Dieser Service ist nicht Teil des MVP, sondern eine Erweiterung für spätere Versionen.

---

## Lernziele

Mit diesem Projekt werden folgende Themen praktisch umgesetzt:

* moderne Frontend-Entwicklung mit Vue und TypeScript
* komponentenbasierte Frontend-Architektur
* REST-API mit Java Spring Boot
* saubere Backend-Struktur mit Controller, Service und Repository
* DTOs für kontrollierten Datenaustausch
* relationale Datenmodellierung mit PostgreSQL
* Modellierung von 1:n-Beziehungen
* Validierung von Eingabedaten
* einfache Geschäftslogik im Service Layer
* Grenzwertprüfung und Statuslogik
* Unit-Tests mit JUnit und Mockito
* Docker-basierte lokale Entwicklungsumgebung
* Grundlagen von Performance-Optimierung durch Filter, Pagination und Indizes

---

## Projektstatus

Aktueller Status:

```text
In Entwicklung
```

Geplanter Fokus der MVP-Version:

```text
Frontend + Backend + PostgreSQL + CRUD + Dashboard + Warnlogik
```

---

## Autor

Mohammad Taiba

---

## Lizenz

Dieses Projekt dient als eigenständiges Portfolio- und Lernprojekt.

Der Quellcode ist öffentlich einsehbar. Eine Nutzung oder Weiterverwendung ist ohne vorherige Zustimmung nicht gestattet.