package com.fhirscope.config

/**
 * ApiConfig
 *
 * Central location for all API configuration constants.
 *
 * This project targets the HAPI FHIR R4 public test server, which requires
 * NO authentication — ideal for learning and sandbox development.
 *
 * TODO (Future – Epic Sandbox): If you migrate to the Epic sandbox you will need:
 *   1. Register an app at https://fhir.epic.com/
 *   2. Obtain a client_id
 *   3. Implement OAuth 2.0 (SMART on FHIR) token exchange
 *   4. Add an "Authorization: Bearer {token}" header to every request
 *   See: https://fhir.epic.com/Documentation?docId=oauth2
 */
object ApiConfig {

    /**
     * Base URL for the HAPI FHIR R4 public test server.
     *
     * All resource endpoints are appended to this base, e.g.:
     *   Patient search  → $BASE_URL/Patient?name=John
     *   Medications     → $BASE_URL/MedicationRequest?patient=12345
     *
     * TODO (Future): Move this to an environment variable or config file
     *   so you can switch between HAPI, Epic, and other sandboxes without
     *   recompiling. e.g., System.getenv("FHIR_BASE_URL") ?: DEFAULT_URL
     */
    const val BASE_URL = "https://hapi.fhir.org/baseR4"

    /**
     * Request headers sent with every FHIR API call.
     *
     * "Accept: application/fhir+json" tells the server to respond with
     * FHIR-flavored JSON (as opposed to XML, which FHIR also supports).
     *
     * TODO: Add "Authorization" header here when implementing OAuth.
     */
    const val ACCEPT_HEADER = "application/fhir+json"

    /**
     * Optional: limit the number of results returned per search request.
     *
     * FHIR servers support the _count parameter to page results.
     * e.g., /Patient?name=John&_count=10
     *
     * TODO (Future): Wire this into FhirClient.searchPatientByName() to
     *   avoid very large responses during development/testing.
     */
    const val DEFAULT_PAGE_SIZE = 10

    // TODO (Future): Add API_KEY or BEARER_TOKEN constants here
    //   when integrating with an authenticated sandbox like Epic.
}
