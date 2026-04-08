package com.fhirscope.util

import com.fhirscope.model.MedicationRequest
import com.fhirscope.model.Patient
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object JsonParser {

    private val mapper = ObjectMapper().registerKotlinModule()

    // Parses a FHIR Bundle response from the /Patient endpoint
    fun parsePatientBundle(json: String): List<Patient> {
        val root = mapper.readTree(json)
        if (root["resourceType"]?.asText() != "Bundle") return emptyList()
        val entries = root["entry"] ?: return emptyList()

        return entries.mapNotNull { entry ->
            val resource = entry["resource"] ?: return@mapNotNull null
            val id = resource["id"]?.asText() ?: return@mapNotNull null
            val nameNode = resource["name"]?.get(0) ?: return@mapNotNull null
            val family = nameNode["family"]?.asText() ?: ""
            val given = nameNode["given"]?.map { it.asText() }?.joinToString(" ") ?: ""
            val fullName = when {
                given.isNotBlank() && family.isNotBlank() -> "$given $family"
                given.isNotBlank() -> given
                else -> family
            }
            Patient(
                id = id,
                fullName = fullName,
                gender = resource["gender"]?.asText(),
                birthDate = resource["birthDate"]?.asText()
            )
        }
    }

    fun parseSingleMedicationRequest(json: String): MedicationRequest? {
        val resource = mapper.readTree(json)
        val id = resource["id"]?.asText() ?: return null
        return MedicationRequest(
            id = id,
            medicationName = resource["medicationCodeableConcept"]?.get("text")?.asText() ?: "Unknown Medication",
            status = resource["status"]?.asText(),
            authoredOn = resource["authoredOn"]?.asText(),
            patientReference = resource["subject"]?.get("reference")?.asText()
        )
    }


    // Parses a FHIR Bundle response from the /MedicationRequest endpoint
    fun parseMedicationRequestBundle(json: String): List<MedicationRequest> {
        val root = mapper.readTree(json)
        if (root["resourceType"]?.asText() != "Bundle") return emptyList()
        val entries = root["entry"] ?: return emptyList()

        return entries.mapNotNull { entry ->
            val resource = entry["resource"] ?: return@mapNotNull null
            val id = resource["id"]?.asText() ?: return@mapNotNull null
            MedicationRequest(
                id = id,
                medicationName = resource["medicationCodeableConcept"]?.get("text")?.asText() ?: "Unknown Medication",
                status = resource["status"]?.asText(),
                authoredOn = resource["authoredOn"]?.asText(),
                patientReference = resource["subject"]?.get("reference")?.asText()
            )
        }
    }
}
