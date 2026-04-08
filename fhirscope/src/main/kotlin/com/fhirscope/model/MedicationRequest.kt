package com.fhirscope.model

data class MedicationRequest(
    val id: String,
    val medicationName: String?,    // from medicationCodeableConcept.text
    val status: String?,            // "active" | "completed" | "cancelled" | "stopped" etc.
    val authoredOn: String?,        // ISO-8601 date or datetime string
    val patientReference: String?   // format: "Patient/{id}"
)
