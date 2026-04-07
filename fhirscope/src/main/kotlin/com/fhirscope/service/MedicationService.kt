package com.fhirscope.service

import com.fhirscope.client.FhirClient
import com.fhirscope.model.MedicationRequest
import com.fhirscope.util.JsonParser
import java.time.format.DateTimeFormatter

class MedicationService(
    private val fhirClient: FhirClient = FhirClient()
) {

    /**
     * getMedicationsForPatient
     *
     * Retrieves all medication requests for a given patient ID, sorted most recent first.
     * All statuses are included (active, cancelled, completed, etc.).
     *
     * @param patientId The logical FHIR Patient ID (from Patient.id)
     * @return List of [MedicationRequest] objects, or empty list if none found
     */
    fun getMedicationsForPatient(patientId: String): List<MedicationRequest> {
        val json = fhirClient.getMedicationsByPatientId(patientId)
        if (json.isNullOrBlank()) {
            return emptyList()
        }
        val medications = JsonParser.parseMedicationRequestBundle(json)
        return medications.sortedByDescending { it.authoredOn }
    }

    /**
     * formatMedicationDisplay
     *
     * Returns a formatted string for CLI display of one medication.
     *
     * Example output:
     *   "Metformin 500 MG  [active]  (prescribed: Jan 10, 2024)"
     *
     * Handles both date-only (`2023-10-01`) and full datetime (`2026-02-12T17:30:36+05:30`)
     * values in authoredOn by taking only the first 10 characters before parsing.
     *
     * @param medication A single [MedicationRequest] to format
     * @return A display-ready string
     */
    fun formatMedicationDisplay(medication: MedicationRequest): String {
        val formattedDate = medication.authoredOn?.let {
            val parsed = DateTimeFormatter.ISO_DATE.parse(it.take(10))
            DateTimeFormatter.ofPattern("MMM d, yyyy").format(parsed)
        } ?: "Unknown"

        return "${medication.medicationName ?: "Unknown Medication"}  [${medication.status}]  (prescribed: $formattedDate)"
    }
}
