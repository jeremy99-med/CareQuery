# CareQuery — FHIRScope

A full-stack patient medication viewer powered by the FHIR R4 standard. Search for patients by name and view their full medication history, pulled live from the HAPI FHIR public sandbox.

---

## Table of Contents

- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Languages & Technologies](#languages--technologies)
- [Requirements](#requirements)
- [Running the App](#running-the-app)
- [FHIR Endpoints](#fhir-endpoints)
- [Backend HTTP API](#backend-http-api)
- [Medication Statuses](#medication-statuses)
- [Limitations](#limitations)
- [Testing](#testing)
- [Future Improvements](#future-improvements)

---

## Architecture

```
Browser
  │
  │  http://localhost:3000
  ▼
Next.js Frontend (fhirscope-web/)
  │
  │  /api/fhir/* proxied to http://localhost:8080/*
  ▼
Kotlin HTTP Server (fhirscope/)
  │
  │  HTTPS requests via OkHttp
  ▼
HAPI FHIR Public Sandbox (hapi.fhir.org/baseR4)
```

The Next.js frontend never calls the FHIR server directly. All FHIR requests are made server-side by the Kotlin backend, which exposes a simple REST API. The Next.js proxy (configured in `next.config.ts`) forwards `/api/fhir/*` requests to the Kotlin server, avoiding CORS issues entirely.

---

## Project Structure

```
CareQuery/
├── fhirscope/                          # Kotlin backend
│   ├── src/main/kotlin/com/fhirscope/
│   │   ├── Main.kt                     # Entry point — starts HTTP server
│   │   ├── Server.kt                   # Ktor HTTP server + route definitions
│   │   ├── client/
│   │   │   └── FhirClient.kt           # OkHttp calls to HAPI FHIR
│   │   ├── config/
│   │   │   └── ApiConfig.kt            # Base URL, headers, page size
│   │   ├── model/
│   │   │   ├── Patient.kt              # Patient data class
│   │   │   └── MedicationRequest.kt    # MedicationRequest data class
│   │   ├── service/
│   │   │   ├── PatientService.kt       # Patient search logic
│   │   │   └── MedicationService.kt    # Medication fetch + formatting
│   │   └── util/
│   │       └── JsonParser.kt           # FHIR Bundle JSON → data classes
│   ├── build.gradle.kts
│   ├── gradlew / gradlew.bat
│   └── FUTURE_IMPROVEMENTS.md
│
├── fhirscope-web/                      # Next.js frontend
│   ├── app/
│   │   ├── layout.tsx                  # Root layout — Bootstrap import, global styles
│   │   ├── page.tsx                    # Home page — patient search
│   │   └── patient/[id]/
│   │       └── page.tsx                # Dynamic route — patient medications
│   ├── components/
│   │   ├── PatientSearch.tsx           # Controlled search input + submit
│   │   ├── PatientList.tsx             # Search results list with navigation links
│   │   └── MedicationList.tsx          # Medication rows with status badges
│   ├── lib/
│   │   └── api.ts                      # All frontend HTTP calls (single source of truth)
│   ├── types/
│   │   └── fhir.ts                     # TypeScript types: Patient, MedicationRequest
│   ├── next.config.ts                  # Turbopack root + /api/fhir proxy rewrite
│   └── package.json
│
├── package.json                        # Root — concurrently dev script
├── start.bat                           # Windows double-click launcher
├── .gitignore
└── README.md
```

---

## Languages & Technologies

| Layer | Language / Framework | Purpose |
|---|---|---|
| Frontend | TypeScript / Next.js 16 (App Router) | UI, routing, API proxy |
| Frontend | React 18 | Component model, state |
| Frontend | Bootstrap 5 | Styling and status badges |
| Backend | Kotlin (JVM 17) | HTTP server, FHIR integration |
| Backend | Ktor (Netty) | Embedded HTTP server |
| Backend | OkHttp | HTTP client for FHIR requests |
| Backend | Jackson | JSON parsing and serialization |
| Build | Gradle (Kotlin DSL) | Kotlin build toolchain |
| Build | npm + concurrently | Running both processes together |

---

## Requirements

- **Node.js** 18+ and **npm**
- **JDK 17+** (e.g., Eclipse Temurin, Amazon Corretto)
- Internet connection (the app calls the live HAPI FHIR sandbox)

Verify your setup:
```bash
node --version    # v18+
npm --version
java --version    # 17+
```

---

## Running the App

### One command (recommended)

From the `CareQuery/` root directory:

```bash
npm run dev
```

This starts both the Kotlin backend and the Next.js frontend in parallel with color-coded output.

**Or double-click** `start.bat` on Windows.

### Manual (two terminals)

**Terminal 1 — Kotlin backend:**
```bash
cd fhirscope
./gradlew run          # macOS/Linux
gradlew.bat run        # Windows
```

**Terminal 2 — Next.js frontend:**
```bash
cd fhirscope-web
npm run dev
```

### URLs

| Service | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |

> **Note:** On first run, Gradle downloads dependencies and compiles Kotlin (~30 seconds). The frontend will be ready before the backend — any search attempted during that window will fail with a connection error. Wait for `[backend] Starting FHIRScope HTTP server on http://localhost:8080` before searching.

---

## FHIR Endpoints

The Kotlin backend calls the **HAPI FHIR R4 public sandbox** — no authentication required.

| Operation | FHIR Endpoint |
|---|---|
| Search patients by name | `GET https://hapi.fhir.org/baseR4/Patient?name={name}&_count=10` |
| Get medications for patient | `GET https://hapi.fhir.org/baseR4/MedicationRequest?patient={id}&_count=10` |

Requests include the header `Accept: application/fhir+json`.

---

## Backend HTTP API

The Kotlin server exposes two endpoints on `http://localhost:8080`:

### `GET /patients?name={query}`

Searches for patients by name. Returns an array of patient objects.

**Example:** `GET /patients?name=Ved`

```json
[
  {
    "id": "131264020",
    "fullName": "Ved Prakash",
    "gender": "male",
    "birthDate": "2026-02-03"
  }
]
```

### `GET /patients/{id}/medications`

Returns all medication requests for a patient, sorted most recent first.

**Example:** `GET /patients/131264020/medications`

```json
[
  {
    "id": "131264024",
    "medicationName": "Atorvastatin 20 MG Oral Tablet",
    "status": "active",
    "authoredOn": "2026-02-12",
    "patientReference": "Patient/131264020"
  }
]
```

---

## Medication Statuses

Medication statuses are displayed as color-coded Bootstrap badges in the UI.

| Status | Badge Color | Meaning |
|---|---|---|
| `active` | Green | Prescription currently in use |
| `completed` | Grey | Prescription course finished |
| `cancelled` | Red | Prescription was cancelled |
| `stopped` / other | Yellow | Other or unrecognized status |

---

## Limitations

### Name search
The HAPI FHIR sandbox does not reliably match full names. **Search by first or last name only.**

- `"Ved"` — works
- `"Núñez"` — works
- `"Ved Prakash"` — may return no results

### Pagination
Only the first 10 results are returned per query (`_count=10`). Patients or medications beyond the first page are not fetched.

### No authentication
The app uses the HAPI FHIR **public** sandbox which requires no credentials. It is not suitable for real patient data. Connecting to authenticated sandboxes (Epic, Cerner) requires OAuth 2.0 / SMART on FHIR implementation.

### Medication name parsing
Medication names are read from `medicationCodeableConcept.text`. Resources that use `medicationReference` (a reference to a separate Medication resource) will show as `"Unknown Medication"`.

### Startup delay
Gradle compilation on first run takes ~30 seconds. The frontend is ready before the backend during this window.

---

## Testing

There are currently no automated tests. Manual testing can be done using the following confirmed patients on the HAPI FHIR sandbox:

| Patient Name | Search Term | Patient ID |
|---|---|---|
| Ved Prakash | `Ved` | `131264020` |
| Núñez Karla | `Núñez` | `90629914` |

**Manual test flow:**
1. Start the app with `npm run dev`
2. Wait for `Starting FHIRScope HTTP server on http://localhost:8080`
3. Open http://localhost:3000
4. Search for `Ved` — confirm patient appears in the list
5. Click the patient row — confirm medications load on the `/patient/[id]` page
6. Click `← Back to search` — confirm navigation returns to home

**Backend endpoints can also be tested directly:**
```bash
curl http://localhost:8080/patients?name=Ved
curl http://localhost:8080/patients/131264020/medications
```

---

## Future Improvements

### Authentication — OAuth 2.0 / SMART on FHIR
Add OAuth token retrieval and inject `Authorization: Bearer {token}` via an OkHttp Interceptor. Required to connect to authenticated sandboxes such as Epic or Cerner. See [FUTURE_IMPROVEMENTS.md](FUTURE_IMPROVEMENTS.md) for implementation steps.

### Pagination
FHIR servers return paged results. Add support for following `Bundle.link[].relation = "next"` URLs to retrieve all pages beyond the first 10 results.

### Medication status filtering
Allow the UI to filter medications by status (e.g., show only `active`). Pass `?status=active` as a FHIR query parameter to reduce response size.

### Patient name in medication heading
Pass the patient's name as a URL query param (`?name=Ved+Prakash`) when navigating to `/patient/[id]` so it can be displayed in the heading without an extra fetch.

### Unit tests
Add JUnit 5 + MockK for backend unit tests. Priority cases: `JsonParser` with sample FHIR Bundle JSON, and `PatientService` with a mocked `FhirClient`.

### Environment-based configuration
Move `ApiConfig` constants (base URL, page size) to environment variables so the FHIR server can be swapped without recompiling.

### `getPatientById`
Implement `PatientService.getPatientById()` using `GET /Patient/{id}` — a direct read that returns a single resource rather than a Bundle.

### Generic Bundle parser
Extract a generic `parseBundleEntries<T>()` helper in `JsonParser` to reduce duplication when additional FHIR resource types are added (e.g., `AllergyIntolerance`, `Observation`).
