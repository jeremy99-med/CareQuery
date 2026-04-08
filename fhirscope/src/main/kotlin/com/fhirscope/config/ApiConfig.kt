package com.fhirscope.config

// Targets the HAPI FHIR R4 public test server — no authentication required.
object ApiConfig {
    const val BASE_URL = "hapi.fhir.org"
    const val ACCEPT_HEADER = "application/fhir+json"
    const val DEFAULT_PAGE_SIZE = 10
}
