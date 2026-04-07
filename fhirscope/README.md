# FHIRScope

A Kotlin command-line application for querying patient and medication data from a FHIR R4 server.

FHIRScope connects to the public [HAPI FHIR sandbox](https://hapi.fhir.org/) — no authentication required — and walks the user through searching for a patient by name and viewing their medication history.

---

## What It Does

```
Enter patient name (or 'quit' to exit):
> Ved

Found 2 patient(s):
  1. Ved Prakash  (2026-02-03)  [male]
  2. Vedat Da?da? Tabian  (1988-09-13)  [male]

Enter selection (1-2):
> 1

Fetching medications for Ved Prakash...
Medications for Ved Prakash:
  - yty  [cancelled]  (prescribed: Feb 12, 2026)
```

---

## Project Structure

```
fhirscope/
├── src/main/kotlin/com/fhirscope/
│   ├── Main.kt                        # CLI entry point and user interaction loop
│   ├── config/
│   │   └── ApiConfig.kt               # Base URL, headers, and pagination constants
│   ├── client/
│   │   └── FhirClient.kt              # HTTP layer — all network calls live here
│   ├── model/
│   │   ├── Patient.kt                 # Data class for FHIR Patient resources
│   │   └── MedicationRequest.kt       # Data class for FHIR MedicationRequest resources
│   ├── service/
│   │   ├── PatientService.kt          # Business logic for patient search
│   │   └── MedicationService.kt       # Business logic for medication retrieval and display
│   └── util/
│       └── JsonParser.kt              # Deserializes raw FHIR Bundle JSON into model objects
├── build.gradle.kts
├── settings.gradle.kts
└── FUTURE_IMPROVEMENTS.md             # Planned but unimplemented features
```

---

## Architecture

The app follows a layered design with a single responsibility per layer:

| Layer | Class | Role |
|---|---|---|
| Entry point | `Main.kt` | Reads user input, drives the CLI flow |
| HTTP | `FhirClient` | Issues GET requests; returns raw JSON strings |
| Parsing | `JsonParser` | Unwraps FHIR Bundles into typed Kotlin objects |
| Business logic | `PatientService`, `MedicationService` | Coordinates HTTP + parsing, applies filtering |
| Models | `Patient`, `MedicationRequest` | Typed representations of FHIR resources |
| Config | `ApiConfig` | Centralizes server URL, headers, and page size |

---

## Tech Stack

- **Language:** Kotlin 1.9 (JVM 17)
- **Build:** Gradle (Kotlin DSL)
- **HTTP client:** OkHttp 4.12
- **JSON parsing:** Jackson (jackson-module-kotlin)
- **FHIR server:** [HAPI FHIR R4 public sandbox](https://hapi.fhir.org/baseR4)

---

## Prerequisites

- Java 17 or newer
- Gradle (or use the included `gradlew` wrapper)

---

## Known Limitations

### Patient Name Search
> **Important:** The HAPI FHIR sandbox's `?name=` parameter does not reliably match across both given and family name when provided together. Searching `"Ved Prakash"` may return no results, while searching `"Ved"` will find him.
>
> **Workaround:** Search by first name or last name only, then select the correct patient from the results list.

### Medication Status
All medication statuses are shown (`active`, `cancelled`, `completed`, etc.). The HAPI FHIR public sandbox contains a mix of real test data and synthetic records — some patients may have no medications or only cancelled ones.

---

## Running the App

```bash
./gradlew run --console=plain
```

The `--console=plain` flag suppresses Gradle's progress bar, which otherwise appears interleaved with the app's output during interactive use.

On Windows, use `.\gradlew run --console=plain`.

---

## Test Patients

The following patients are confirmed to exist on the HAPI FHIR sandbox and have medication records:

| Search term | Patient name | Notes |
|---|---|---|
| `ved` | Ved Prakash | Has 1 cancelled medication |
| `Nunez` | NuÃ±ez Karla | Has 1 cancelled medication |

> These are live records on a public sandbox — they may change or be deleted at any time.

---

## FHIR Endpoints Used

| Resource | Endpoint |
|---|---|
| Patient search | `GET /Patient?name={name}&_count=10` |
| Medication lookup | `GET /MedicationRequest?patient={patientId}` |

Both endpoints return a **FHIR Bundle** — a JSON wrapper with an `entry[]` array of individual resources.

---

## Future Improvements

See [FUTURE_IMPROVEMENTS.md](FUTURE_IMPROVEMENTS.md) for planned features including:

- OAuth 2.0 / SMART on FHIR authentication
- Pagination support
- Medication status filtering
- CLI argument parsing
- Unit tests (JUnit 5 + MockK)
