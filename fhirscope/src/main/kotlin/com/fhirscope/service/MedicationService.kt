package com.fhirscope.service

import com.fhirscope.client.FhirClient
import com.fhirscope.model.MedicationRequest
import com.fhirscope.util.JsonParser
import java.time.format.DateTimeFormatter
import java.time.LocalDate


class MedicationService(private val fhirClient: FhirClient = FhirClient()) {
    // Returns all medications for a patient sorted most recent first
    fun getMedicationsForPatient(patientId: String): List<MedicationRequest> {
        val json = fhirClient.getMedicationsByPatientId(patientId) ?: return emptyList()
        return JsonParser.parseMedicationRequestBundle(json).sortedByDescending { it.authoredOn }
    }

    fun formatMedicationDisplay(medication: MedicationRequest): String {
        // authoredOn can be a full datetime string — take only the first 10 chars (YYYY-MM-DD)
        val formattedDate =
                medication.authoredOn?.let {
                    val parsed = DateTimeFormatter.ISO_DATE.parse(it.take(10))
                    DateTimeFormatter.ofPattern("MMM d, yyyy").format(parsed)
                }
                        ?: "Unknown"

        return "${medication.medicationName ?: "Unknown Medication"}  [${medication.status}]  (prescribed: $formattedDate)"
    }

    //
    fun createMedication(
        patientId: String,
        medicationName: String,
        rxNormCode: String?,
        status: String,
        authoredOn: String = LocalDate.now().toString()
    ): MedicationRequest? {
        val codingBlock = if (rxNormCode != null) """
            "coding": [
                { "system": "http://www.nlm.nih.gov/research/umls/rxnorm", 
                 "code": "$rxNormCode", 
                 "display": "$medicationName" 
                }
            ],
            """ else ""

        val body = """
            {
              "resourceType": "MedicationRequest",
              "status": "$status",
              "intent": "order",
              "medicationCodeableConcept": {
                $codingBlock
                "text": "$medicationName"
              },
              "subject": {
                "reference": "Patient/$patientId"
              },
              "authoredOn": "$authoredOn"
            }
        """.trimIndent()
        val json = fhirClient.postMedicationRequest(body) ?: return null
        return JsonParser.parseSingleMedicationRequest(json)
    }
}
