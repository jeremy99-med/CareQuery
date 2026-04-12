# Future Improvements

Planned but unimplemented enhancements across the full FHIRScope stack.

---

## Backend (Kotlin)

### Authentication — OAuth 2.0 / SMART on FHIR

Add OAuth token retrieval and inject `Authorization: Bearer {token}` via an OkHttp Interceptor. Required to connect to authenticated sandboxes such as Epic or Cerner.

Steps:
1. Register an app at https://fhir.epic.com/
2. Obtain a `client_id`
3. Implement the SMART on FHIR token exchange flow
4. Add an OkHttp `Interceptor` that attaches the bearer token to every request
5. Store the base URL and credentials in environment variables rather than `ApiConfig`

---

### Pagination

FHIR servers return results in pages. Currently only the first page (`_count=10`) is fetched.

- Add a `parsePaginationLinks()` function in `JsonParser` that reads `Bundle.link[]` to find the `"next"` URL:
  ```json
  { "relation": "next", "url": "https://hapi.fhir.org/baseR4?_getpages=..." }
  ```
- Follow the `next` link in `FhirClient` until all pages are retrieved, or until a page limit is reached.

---

### Medication Status Filter

Add a `statusFilter` parameter to `getMedicationsForPatient()` so callers can request only specific statuses:

```kotlin
fun getMedicationsForPatient(patientId: String, status: String? = null): List<MedicationRequest>
```

Optionally pass `?status=active` as a query parameter to the FHIR server to reduce response size.

---

### Environment-Based Configuration

Move `ApiConfig` constants (base URL, page size) to environment variables so the FHIR server can be swapped without recompiling:

```kotlin
val baseUrl = System.getenv("FHIR_BASE_URL") ?: "hapi.fhir.org"
```

---

### Direct Patient Read (`getPatientById`)

Implement `PatientService.getPatientById(patientId: String): Patient?` using the direct read endpoint:

```
GET https://hapi.fhir.org/baseR4/Patient/{id}
```

Note: this returns a single `Patient` resource directly, not wrapped in a Bundle — a separate parser path is needed.

---

### Generic Bundle Parser

Once additional resource types are added (e.g., `AllergyIntolerance`, `Observation`), extract a generic helper in `JsonParser` to reduce duplication:

```kotlin
fun <T> parseBundleEntries(json: String, transform: (JsonNode) -> T?): List<T>
```

---

### Unit Tests

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

## Frontend (Next.js)

### Site-Wide Navigation Bar

Add a persistent `<nav>` in `app/layout.tsx` with the FHIRScope logo/title and a link back to the home page, so users always have a way to start a new search.

---

### Medication Status Filtering in the UI

Add a dropdown or tab bar on the medications page to filter by status (active, completed, cancelled). Can be done client-side against the already-fetched list, or passed as a query parameter to the backend.

---

### Error Boundary

Wrap pages in a React Error Boundary so unexpected rendering errors show a friendly message instead of a blank screen.

---

### Loading Skeleton

Replace the plain `Loading medications...` text with a skeleton screen (e.g., Bootstrap placeholder rows) for a better perceived performance experience.

---

### Debounced Search

Debounce the search input in `PatientSearch.tsx` (~300ms) so the API is not called on every keystroke if auto-search is added in the future.

---

### `medicationReference` Fallback

Some FHIR resources use `medicationReference` instead of `medicationCodeableConcept`. Add a fallback in the Kotlin `JsonParser` to resolve the referenced `Medication` resource and extract its display name.
