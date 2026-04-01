package com.fhirscope.model

/**
 * MedicationRequest
 *
 * A simplified data class representing a FHIR R4 MedicationRequest resource.
 *
 * FHIR JSON structure for a MedicationRequest (abridged):
 * {
 *   "resourceType": "MedicationRequest",
 *   "id": "med1",
 *   "status": "active",
 *   "medicationCodeableConcept": {
 *     "text": "Atorvastatin 20 MG Oral Tablet"
 *   },
 *   "subject": {
 *     "reference": "Patient/12345"
 *   },
 *   "authoredOn": "2023-10-01"
 * }
 *
 * IMPORTANT – Key nested fields:
 *   • Medication name lives at: medicationCodeableConcept.text
 *     This is the human-readable drug description. Some resources may use
 *     medicationCodeableConcept.coding[0].display instead — check both.
 *   • Patient linkage lives at: subject.reference = "Patient/{id}"
 *     Strip the "Patient/" prefix if you need the bare numeric ID.
 *
 * TODO: Handle resources that use medicationReference (a reference to a
 *   separate Medication resource) instead of medicationCodeableConcept.
 *   For HAPI sandbox data, medicationCodeableConcept.text is usually present.
 */
data class MedicationRequest(

    /**
     * The server-assigned logical ID for this medication request.
     *
     * FHIR path: MedicationRequest.id
     */
    val id: String,

    /**
     * The human-readable name of the prescribed medication.
     * e.g., "Atorvastatin 20 MG Oral Tablet"
     *
     * FHIR path: MedicationRequest.medicationCodeableConcept.text
     *
     * TODO: Fall back to medicationCodeableConcept.coding[0].display
     *   if .text is null or blank.
     */
    val medicationName: String?,

    /**
     * Current status of the prescription.
     * FHIR values: "active" | "completed" | "cancelled" | "stopped" | etc.
     *
     * FHIR path: MedicationRequest.status
     *
     * TODO (Optional): Filter to only "active" medications in
     *   MedicationService before displaying results to the user.
     */
    val status: String?,

    /**
     * The date the prescription was written, in ISO-8601 format (YYYY-MM-DD).
     *
     * FHIR path: MedicationRequest.authoredOn
     *
     * TODO (Future): Parse to LocalDate and display in a user-friendly format.
     */
    val authoredOn: String?,

    /**
     * Reference string linking this request back to a Patient.
     * Format: "Patient/{patientId}"
     *
     * FHIR path: MedicationRequest.subject.reference
     *
     * TODO: If you need the bare patient ID, split on "/" and take the last segment:
     *   val patientId = patientReference?.substringAfterLast("/")
     */
    val patientReference: String?
)
