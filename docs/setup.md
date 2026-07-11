# Setup

Dieses Dokument beschreibt die lokale Installation und den Start von **EnergyFlow Dashboard**.

---

## Voraussetzungen

Für den Standardstart werden benötigt:

```text
Docker
Docker Compose
Git
```

Node.js und npm werden zusätzlich für den Frontend-Entwicklungsmodus benötigt. Java 21 ist für lokale Backend-Tests erforderlich.

Prüfen:

```powershell
docker --version
docker compose version
git --version
```

---

## Repository klonen

```powershell
git clone https://github.com/USERNAME/EnergyFlow-Dashboard.git
cd EnergyFlow-Dashboard
```

---

## Projektstruktur

```text
EnergyFlow-Dashboard
|
|--- backend
|--- frontend
|--- docs
|--- docker-compose.yml
|--- README.md
```

---

## Full-Stack starten

Die Standardkonfiguration startet Frontend, Backend und PostgreSQL gemeinsam.

### Umgebungsvariablen vorbereiten

```powershell
Copy-Item .env.example .env
```

In `.env` muss vor dem ersten Start ein eigenes `POSTGRES_PASSWORD` gesetzt werden.

> **Hinweis:** PostgreSQL übernimmt die `POSTGRES_*`-Werte nur beim erstmaligen Anlegen des Datenbankvolumes. Eine spätere Passwortänderung in `.env` ändert das Passwort in einer bestehenden Datenbank nicht. In diesem Fall muss das Passwort migriert oder `.env` wieder an den vorhandenen Wert angepasst werden.

### Anwendung bauen und starten

```powershell
docker compose up --build -d --wait --wait-timeout 120
```

Der Befehl wartet, bis alle drei Dienste bereit sind. Status und Logs können anschließend geprüft werden:

```powershell
docker compose ps
docker compose logs -f
```

| Dienst | Erreichbarkeit |
| --- | --- |
| Frontend | `http://localhost` |
| API über Nginx | `http://localhost/api/sites` |
| Backend direkt | `http://localhost:8080` |
| PostgreSQL | nur intern unter `postgres:5432` |

PostgreSQL wird bewusst nicht am Host unter `localhost:5432` veröffentlicht.

---

## Optionaler Frontend-Entwicklungsmodus

Für Hot Reload können PostgreSQL und Backend über Compose sowie das Frontend lokal gestartet werden:

```powershell
docker compose up --build -d --wait postgres backend
cd frontend
npm install
npm run dev
```

Das Entwicklungsfrontend läuft unter `http://localhost:5173` und leitet `/api` an das Backend weiter.

Mit `Ctrl + C` wird der Vite-Entwicklungsserver beendet. Die übrigen Dienste werden im Projektordner mit `docker compose down` gestoppt.

---

## API testen

Voraussetzungen:

```text
Der Docker-Compose-Stack läuft
```

### Alle Standorte abrufen

```powershell
Invoke-RestMethod -Uri "http://localhost/api/sites"
```

### Standort anlegen

```powershell
$body = @{
    name = "Verwaltungsgebäude Ilmenau"
    type = "OFFICE"
    location = "Ilmenau"
} | ConvertTo-Json

Invoke-RestMethod `
    -Method POST `
    -Uri "http://localhost/api/sites" `
    -ContentType "application/json" `
    -Body $body
```

### Standort nach ID abrufen

```powershell
Invoke-RestMethod -Uri "http://localhost/api/sites/1"
```

---

## Tests starten

### Backend-Tests

```powershell
Push-Location backend
.\mvnw.cmd test
Pop-Location
```

Die Backend-Tests laufen gegen ein H2-Testprofil und benötigen keinen laufenden PostgreSQL-Container.

### Frontend Build

```powershell
Push-Location frontend
npm run build
Pop-Location
```

### Frontend Lint

```powershell
Push-Location frontend
npm run lint
Pop-Location
```

---

## Full-Stack stoppen

Container stoppen und Daten behalten:

```powershell
docker compose down
```

Container und Datenbankvolume löschen:

```powershell
docker compose down -v
```

> **Achtung:** Dieser Befehl löscht alle lokal gespeicherten Datenbankdaten.

