# API-Dokumentation

Dieses Dokument beschreibt die REST-API von **EnergyFlow Dashboard**.

---

## Basis-URL

```text
http://localhost:8080/api
```

---

## Aktueller API-Status

### Bereits umgesetzt

```text
GET     /api/sites
GET     /api/sites/{id}
POST    /api/sites
PUT     /api/sites/{id}
DELETE  /api/sites/{id}
```

### Geplant

```text
/api/meters
/api/measurements
/api/thresholds
/api/dashboard
```

---

## Sites API

Die Sites API verwaltet Standorte.

### Standort-Datenmodell

```json
{
  "id": 1,
  "name": "Verwaltungsgebaeude Ilmenau",
  "type": "OFFICE",
  "location": "Ilmenau"
}
```

---

## Endpunkte

### Alle Standorte abrufen

```text
GET /api/sites
```

PowerShell:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/sites"
```

Beispiel-Antwort:

```text
id name                        type   location
-- ----                        ----   --------
1  Verwaltungsgebaeude Ilmenau OFFICE Ilmenau
```

---

### Standort nach ID abrufen

```text
GET /api/sites/{id}
```

PowerShell:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/sites/1"
```

Beispiel-Antwort:

```text
id name                        type   location
-- ----                        ----   --------
1  Verwaltungsgebaeude Ilmenau OFFICE Ilmenau
```

---

### Standort anlegen

```text
POST /api/sites
```

Request Body:

```json
{
  "name": "Verwaltungsgebaeude Ilmenau",
  "type": "OFFICE",
  "location": "Ilmenau"
}
```

PowerShell:

```powershell
$body = @{
    name = "Verwaltungsgebaeude Ilmenau"
    type = "OFFICE"
    location = "Ilmenau"
} | ConvertTo-Json

Invoke-RestMethod `
    -Method POST `
    -Uri "http://localhost:8080/api/sites" `
    -ContentType "application/json" `
    -Body $body
```

Beispiel-Antwort:

```text
id name                        type   location
-- ----                        ----   --------
1  Verwaltungsgebaeude Ilmenau OFFICE Ilmenau
```

---

### Standort aktualisieren

```text
PUT /api/sites/{id}
```

Request Body:

```json
{
  "name": "Verwaltungsgebaeude Ilmenau Updated",
  "type": "OFFICE",
  "location": "Ilmenau"
}
```

PowerShell:

```powershell
$body = @{
    name = "Verwaltungsgebaeude Ilmenau Updated"
    type = "OFFICE"
    location = "Ilmenau"
} | ConvertTo-Json

Invoke-RestMethod `
    -Method PUT `
    -Uri "http://localhost:8080/api/sites/1" `
    -ContentType "application/json" `
    -Body $body
```

---

### Standort löschen

```text
DELETE /api/sites/{id}
```

PowerShell:

```powershell
Invoke-RestMethod `
    -Method DELETE `
    -Uri "http://localhost:8080/api/sites/1"
```

Danach prüfen:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/sites"
```

---

## Fehlerfälle

### Standort existiert nicht

```text
GET /api/sites/999
```

PowerShell:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/sites/999"
```

Erwartetes Ergebnis:

```text
404 Not Found
```

---

### Ungültiger Request Body

Beispiel:

```json
{
  "name": "",
  "type": "",
  "location": ""
}
```

Erwartetes Ergebnis:

```text
400 Bad Request
```

---

## Geplante API-Endpunkte

Diese Endpunkte sind Teil der geplanten MVP-Erweiterung.

---

## Meters API

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
  "name": "Hauptstromzaehler",
  "meterNumber": "STR-2026-001",
  "energyType": "ELECTRICITY"
}
```

---

## Measurements API

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

## Thresholds API

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

## Dashboard API

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
    "siteName": "Verwaltungsgebaeude Ilmenau",
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
    "siteName": "Verwaltungsgebaeude Ilmenau",
    "meterName": "Hauptstromzaehler",
    "measuredAt": "2026-01-15T10:00:00",
    "consumptionKwh": 450.5,
    "thresholdKwh": 400.0,
    "status": "WARNING"
  }
]
```

---

## Hinweise für PowerShell

In PowerShell ist `curl` oft ein Alias für `Invoke-WebRequest`.

Deshalb besser verwenden:

```powershell
Invoke-RestMethod
```

Oder echtes curl explizit aufrufen:

```powershell
curl.exe http://localhost:8080/api/sites
```

Für JSON-POST-Requests ist `Invoke-RestMethod` in PowerShell meistens sauberer.