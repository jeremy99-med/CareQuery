package com.fhirscope.service

import com.fhirscope.client.FhirClient
import com.fhirscope.model.Patient
import com.fhirscope.util.JsonParser

class PatientService(
    private val fhirClient: FhirClient = FhirClient()
) {

    /**
     * searchPatients
     *
     * Searches for patients by name and returns a list of matching [Patient] objects.
     * Filters out patients with blank names before returning.
     *
     * NOTE: The HAPI FHIR sandbox works best with a single name token (first or last
     * name only). See FUTURE_IMPROVEMENTS.md for the full name search limitation.
     *
     * @param name The search string entered by the user
     * @return List of matching [Patient] objects, or empty list if none found
     */
    fun searchPatients(name: String): List<Patient> {
        val json = fhirClient.searchPatientByName(name)
        if (json.isNullOrBlank()) {
            return emptyList()
        }
        val patients = JsonParser.parsePatientBundle(json)
        return patients.filter { it.fullName.isNotBlank() }
    }

    /**
     * getPatientById
     *
     * Fetches a single patient by their logical FHIR ID.
     * See FUTURE_IMPROVEMENTS.md for implementation details.
     */
    fun getPatientById(patientId: String): Patient? {
        throw NotImplementedError("getPatientById is not yet implemented")
    }
}
