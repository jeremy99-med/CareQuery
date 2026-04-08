package com.fhirscope

import com.fhirscope.service.MedicationService
import com.fhirscope.service.PatientService

/**
 * Main.kt — Application Entry Point
 *
 * FHIRScope is a command-line application that allows a user to:
 *   1. Search for a patient by name
 *   2. Select a patient from the results list
 *   3. View that patient's medications
 *
 * HOW TO RUN:
 *   ./gradlew run --console=plain
 */
fun main() {
    val patientService = PatientService()
    val medicationService = MedicationService()

    println("Starting FHIRScope HTTP server on http://localhost:8080")
    startServer(patientService, medicationService)
}
