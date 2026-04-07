package com.fhirscope.util

import com.fhirscope.model.MedicationRequest
import com.fhirscope.model.Patient
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object JsonParser {

    private val mapper = ObjectMapper().registerKotlinModule()

    /**
     * parsePatientBundle
     *
     * Parses a FHIR Bundle JSON string and returns a list of [Patient] objects.
     *
     * @param json Raw JSON string from the /Patient search endpoint
     * @return List of parsed [Patient] objects (empty if no results)
     */
    fun parsePatientBundle(json: String): List<Patient> {
        val root = mapper.readTree(json)
        if (root["resourceType"]?.asText() != "Bundle") {
            return emptyList()
        }
        val entries = root["entry"] ?: return emptyList()

        return entries.mapNotNull { entry ->
            val resource = entry["resource"] ?: return@mapNotNull null
            val id = resource["id"]?.asText() ?: return@mapNotNull null
            val nameNode = resource["name"]?.get(0) ?: return@mapNotNull null
            val family = nameNode["family"]?.asText() ?: ""
            val given = nameNode["given"]?.map { it.asText() }?.joinToString(" ") ?: ""
            val fullName = if (given.isNotBlank() && family.isNotBlank()) {
                "$given $family"
            } else if (given.isNotBlank()) {
                given
            } else {
                family
            }
            val gender = resource["gender"]?.asText()
            val birthDate = resource["birthDate"]?.asText()

            Patient(id, fullName, gender, birthDate)
        }
    }

    /**
     * parseMedicationRequestBundle
     *
     * Parses a FHIR Bundle JSON string and returns a list of [MedicationRequest] objects.
     *
     * @param json Raw JSON string from the /MedicationRequest search endpoint
     * @return List of parsed [MedicationRequest] objects (empty if none found)
     */
    fun parseMedicationRequestBundle(json: String): List<MedicationRequest> {
        val root = mapper.readTree(json)
        if (root["resourceType"]?.asText() != "Bundle") {
            return emptyList()
        }
        val entries = root["entry"] ?: return emptyList()

        return entries.mapNotNull { entry ->
            val resource = entry["resource"] ?: return@mapNotNull null
            val id = resource["id"]?.asText() ?: return@mapNotNull null
            val medicationName = resource["medicationCodeableConcept"]?.get("text")?.asText()
                ?: "Unknown Medication"
            val status = resource["status"]?.asText()
            val authoredOn = resource["authoredOn"]?.asText()
            val patientReference = resource["subject"]?.get("reference")?.asText()

            MedicationRequest(id, medicationName, status, authoredOn, patientReference)
        }
    }
}
