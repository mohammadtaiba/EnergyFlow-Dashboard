# Datenbankmodell

Dieses Dokument beschreibt das geplante Datenbankmodell von **EnergyFlow Dashboard**.

---

## Datenbank

```text
PostgreSQL 16
```

---

## Aktueller Stand

Aktuell ist die PostgreSQL-Datenbank über Docker Compose eingerichtet.

Die erste implementierte Tabelle ist:

```text
sites
```

Weitere Tabellen sind für die MVP-Erweiterung geplant.

---

## Geplantes MVP-Datenmodell

Die Datenbank besteht aus vier Haupttabellen:

```text
sites
meters
measurements
thresholds
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

## Tabelle: sites

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
name: Verwaltungsgebaeude Ilmenau
type: OFFICE
location: Ilmenau
```

### Bedeutung der Spalten

```text
id           eindeutige ID des Standorts
name         Name des Standorts
type         Standorttyp, z. B. OFFICE oder PRODUCTION
location     Ort oder Adresse
created_at   Erstellzeitpunkt
updated_at   Zeitpunkt der letzten Änderung
```

---

## Tabelle: meters

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
name: Hauptstromzaehler
meter_number: STR-2026-001
energy_type: ELECTRICITY
```

### Bedeutung der Spalten

```text
id              eindeutige ID des Energiezählers
site_id         Fremdschlüssel auf sites.id
name            Anzeigename des Zählers
meter_number    technische oder interne Zählernummer
energy_type     Energieart
created_at      Erstellzeitpunkt
updated_at      Zeitpunkt der letzten Änderung
```

### Mögliche Energiearten

```text
ELECTRICITY
GAS
WATER
HEATING
```

---

## Tabelle: measurements

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

### Bedeutung der Spalten

```text
id                 eindeutige ID des Messwerts
meter_id           Fremdschlüssel auf meters.id
measured_at        Zeitpunkt der Messung
consumption_kwh    Verbrauchswert in kWh
status             fachlicher Status des Messwerts
created_at         Erstellzeitpunkt
updated_at         Zeitpunkt der letzten Änderung
```

### Statuswerte

```text
NORMAL
WARNING
CRITICAL
```

---

## Tabelle: thresholds

Speichert Grenzwerte für Standorte oder Energiezähler.

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

### Bedeutung der Spalten

```text
id                    eindeutige ID des Grenzwerts
site_id               optionaler Fremdschlüssel auf sites.id
meter_id              optionaler Fremdschlüssel auf meters.id
max_consumption_kwh   maximal erlaubter Verbrauch
period                Zeitraum des Grenzwerts
created_at            Erstellzeitpunkt
updated_at            Zeitpunkt der letzten Änderung
```

### Mögliche Perioden

```text
HOURLY
DAILY
WEEKLY
MONTHLY
```

---

## SQL-Übersicht

### sites

```sql
CREATE TABLE sites (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

### meters

```sql
CREATE TABLE meters (
    id BIGSERIAL PRIMARY KEY,
    site_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    meter_number VARCHAR(255) NOT NULL,
    energy_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_meters_site FOREIGN KEY (site_id) REFERENCES sites(id)
);
```

### measurements

```sql
CREATE TABLE measurements (
    id BIGSERIAL PRIMARY KEY,
    meter_id BIGINT NOT NULL,
    measured_at TIMESTAMP NOT NULL,
    consumption_kwh NUMERIC(12, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_measurements_meter FOREIGN KEY (meter_id) REFERENCES meters(id)
);
```

### thresholds

```sql
CREATE TABLE thresholds (
    id BIGSERIAL PRIMARY KEY,
    site_id BIGINT,
    meter_id BIGINT,
    max_consumption_kwh NUMERIC(12, 2) NOT NULL,
    period VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_thresholds_site FOREIGN KEY (site_id) REFERENCES sites(id),
    CONSTRAINT fk_thresholds_meter FOREIGN KEY (meter_id) REFERENCES meters(id)
);
```

---

## Empfohlene Indizes

Für größere Datenmengen sind folgende Indizes sinnvoll:

```sql
CREATE INDEX idx_meters_site_id ON meters(site_id);
CREATE INDEX idx_measurements_meter_id ON measurements(meter_id);
CREATE INDEX idx_measurements_measured_at ON measurements(measured_at);
CREATE INDEX idx_measurements_status ON measurements(status);
CREATE INDEX idx_thresholds_site_id ON thresholds(site_id);
CREATE INDEX idx_thresholds_meter_id ON thresholds(meter_id);
```

---

## Aktuelle JPA-Konfiguration

Während der lokalen Entwicklung nutzt das Backend:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

Hinweis:

```text
ddl-auto: update ist nur für lokale Entwicklung gedacht.
Für produktionsnahe Projekte sollten Datenbankmigrationen mit Flyway oder Liquibase verwendet werden.
```

---

## Geplante Migrationen

Später kann Flyway genutzt werden:

```text
backend/src/main/resources/db/migration
|
|--- V1__create_sites_table.sql
|--- V2__create_meters_table.sql
|--- V3__create_measurements_table.sql
|--- V4__create_thresholds_table.sql
```