# Future Improvements

This document tracks planned but unimplemented enhancements for FHIRScope.

---

## Authentication — OAuth 2.0 / SMART on FHIR

**File:** `FhirClient.kt`

Add OAuth token retrieval and inject `Authorization: Bearer {token}` via an OkHttp Interceptor. Required to connect to authenticated sandboxes such as Epic or Cerner.

Steps:
1. Register an app at https://fhir.epic.com/
2. Obtain a `client_id`
3. Implement the SMART on FHIR token exchange flow
4. Add an OkHttp `Interceptor` that attaches the bearer token to every request
5. Store the base URL and credentials in environment variables rather than `ApiConfig`

---

## Pagination

**Files:** `FhirClient.kt`, `JsonParser.kt`

FHIR servers return results in pages. Currently only the first page (`_count=10`) is fetched.

- Add a `parsePaginationLinks()` function in `JsonParser` that reads `Bundle.link[]` to find the `"next"` URL:
  ```json
  { "relation": "next", "url": "https://hapi.fhir.org/baseR4?_getpages=..." }
  ```
- Follow the `next` link in `FhirClient` until all pages are retrieved, or until a page limit is reached.

---

## Medication Status Filter

**File:** `MedicationService.kt`

Add a `statusFilter` parameter to `getMedicationsForPatient()` so callers can request only specific statuses:

```kotlin
fun getMedicationsForPatient(patientId: String, status: String? = null): List<MedicationRequest>
```

Optionally pass `?status=active` as a query parameter to the server to reduce response size.

---

## CLI Argument Parsing

**File:** `Main.kt`

Allow the patient name to be passed directly on the command line instead of prompting interactively:

```bash
./gradlew run --args="John"
```

Consider using [kotlinx-cli](https://github.com/Kotlin/kotlinx-cli) or [picocli](https://picocli.info/) for argument parsing.

---

## Unit Tests

**File:** `build.gradle.kts`

Add JUnit 5 + MockK for unit testing. Because `FhirClient` is injected as a constructor parameter in `PatientService` and `MedicationService`, it can be easily swapped for a mock:

```kotlin
// build.gradle.kts
testImplementation("org.junit.jupiter:junit-jupiter:5.10.x")
testImplementation("io.mockk:mockk:1.13.x")
```

Priority test cases:
- `JsonParser.parsePatientBundle()` with sample Bundle JSON
- `JsonParser.parseMedicationRequestBundle()` with missing `medicationCodeableConcept`
- `PatientService.searchPatients()` with a mocked `FhirClient`

---

## Generic Bundle Parser

**File:** `JsonParser.kt`

Once additional resource types are added (e.g., `AllergyIntolerance`, `Observation`), extract a generic helper to reduce duplication:

```kotlin
fun <T> parseBundleEntries(json: String, transform: (JsonNode) -> T?): List<T>
```

---

## Direct Patient Read (`getPatientById`)

**File:** `PatientService.kt`

Implement `getPatientById(patientId: String): Patient?` using the direct read endpoint:

```
GET https://hapi.fhir.org/baseR4/Patient/{id}
```

Note: this returns a single `Patient` resource directly, not wrapped in a Bundle — a separate parser path is needed.
