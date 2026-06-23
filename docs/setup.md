# Setup

Dieses Dokument beschreibt die lokale Installation und den Start von **EnergyFlow Dashboard**.

---

## Voraussetzungen

Installiert sein müssen:

```text
Java 21
Node.js
npm
Docker
Docker Compose
Git
VS Code
```

Prüfen:

```powershell
java -version
node -v
npm -v
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

## PostgreSQL starten

Die Datenbank wird lokal über Docker Compose gestartet.

```powershell
docker compose up -d
```

Prüfen:

```powershell
docker ps
```

Erwarteter Container:

```text
energyflow-postgres
```

---

## Docker Compose

Datei:

```text
docker-compose.yml
```

Inhalt:

```yaml
services:
  postgres:
    image: postgres:16
    container_name: energyflow-postgres
    environment:
      POSTGRES_DB: energyflow
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - energyflow_postgres_data:/var/lib/postgresql/data

volumes:
  energyflow_postgres_data:
```

---

## Backend konfigurieren

Datei:

```text
backend/src/main/resources/application.yaml
```

Inhalt für lokale Entwicklung:

```yaml
spring:
  application:
    name: energyflow

  datasource:
    url: jdbc:postgresql://localhost:5432/energyflow
    username: postgres
    password: postgres

  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8080
```

---

## Backend starten

Im Projektordner:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Erfolgreicher Start:

```text
Tomcat started on port 8080
Started EnergyflowApplication
```

Backend läuft unter:

```text
http://localhost:8080
```

---

## Backend stoppen

```powershell
Ctrl + C
```

Falls PowerShell fragt:

```text
Terminate batch job (Y/N)?
```

Dann:

```powershell
y
```

---

## Frontend installieren

In einem zweiten Terminal:

```powershell
cd frontend
npm install
```

---

## Frontend starten

```powershell
npm run dev
```

Frontend läuft unter:

```text
http://localhost:5173
```

---

## Frontend stoppen

```powershell
Ctrl + C
```

Falls gefragt wird:

```text
Terminate batch job (Y/N)?
```

Dann:

```powershell
y
```

---

## API testen

Voraussetzungen:

```text
PostgreSQL läuft
Spring-Boot-Backend läuft
```

### Alle Standorte abrufen

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/sites"
```

### Standort anlegen

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

### Standort nach ID abrufen

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/sites/1"
```

---

## Tests starten

### Backend-Tests

```powershell
cd backend
.\mvnw.cmd test
```

### Frontend Build

```powershell
cd frontend
npm run build
```

### Frontend Lint

```powershell
cd frontend
npm run lint
```

---

## Datenbank stoppen

Container stoppen:

```powershell
docker compose down
```

Container und Daten löschen:

```powershell
docker compose down -v
```

