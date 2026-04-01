# FHIRScope

A Kotlin command-line application for querying patient and medication data from a FHIR R4 server.

FHIRScope connects to the public [HAPI FHIR sandbox](https://hapi.fhir.org/) — no authentication required — and walks the user through searching for a patient by name and viewing their medication history.

---

## What It Does

```
Enter patient name:
> John

Found 2 patient(s):
  1. John Doe    (1990-01-01)  [male]
  2. John Smith  (1985-05-12)  [male]

Enter selection (1-2):
> 1

Medications for John Doe:
  - Atorvastatin 20 MG Oral Tablet  [active]      (2023-10-01)
  - Lisinopril 10 MG Oral Tablet    [completed]   (2023-08-15)
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
└── settings.gradle.kts
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

## Running the App

```bash
./gradlew run
```

---

## Current Status

This project is a **starter scaffold**. The structure, models, and documentation are in place; the implementation bodies contain `TODO` comments and `throw NotImplementedError(...)` placeholders.

### What needs to be implemented

| File | What to implement |
|---|---|
| `FhirClient.kt` | `searchPatientByName()` and `getMedicationsByPatientId()` using OkHttp |
| `JsonParser.kt` | `parsePatientBundle()` and `parseMedicationRequestBundle()` using Jackson |
| `PatientService.kt` | `searchPatients()` — wire client → parser → return list |
| `MedicationService.kt` | `getMedicationsForPatient()` and `formatMedicationDisplay()` |
| `Main.kt` | Full CLI flow: prompt, search, select, display medications |

---

## FHIR Endpoints Used

| Resource | Endpoint |
|---|---|
| Patient search | `GET /Patient?name={name}&_count=10` |
| Medication lookup | `GET /MedicationRequest?patient={patientId}` |

Both endpoints return a **FHIR Bundle** — a JSON wrapper with an `entry[]` array of individual resources.

---

## Future Improvements

- **OAuth 2.0 / SMART on FHIR** — swap HAPI sandbox for an Epic or Cerner sandbox with token-based auth
- **Pagination** — follow `Bundle.link[rel="next"]` for large result sets
- **Active-only filter** — add `?status=active` to the MedicationRequest query
- **Unit tests** — JUnit 5 + MockK, with `FhirClient` injected as a constructor parameter for easy mocking
- **CLI argument parsing** — accept the patient name directly: `./gradlew run --args="John Doe"`
