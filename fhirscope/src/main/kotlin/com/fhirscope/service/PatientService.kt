package com.fhirscope.service

import com.fhirscope.client.FhirClient
import com.fhirscope.model.Patient
import com.fhirscope.util.JsonParser

class PatientService(
    private val fhirClient: FhirClient = FhirClient()
) {
    fun searchPatients(name: String): List<Patient> {
        val json = fhirClient.searchPatientByName(name) ?: return emptyList()
        return JsonParser.parsePatientBundle(json).filter { it.fullName.isNotBlank() }
    }

    fun getPatientById(patientId: String): Patient? {
        throw NotImplementedError("getPatientById is not yet implemented")
    }
}
