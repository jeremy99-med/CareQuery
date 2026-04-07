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

    try {
        while (true) {
            // ── Step 1: Prompt user for a patient name ──────────────────────────
            println("\nEnter patient name (or 'quit' to exit):")
            val input = readLine() ?: break
            if (input.isBlank()) {
                println("No input provided.")
                continue
            }
            val name = input.trim()
            if (name.equals("quit", ignoreCase = true)) {
                println("Goodbye!")
                break
            }

            // ── Step 2: Search for patients ─────────────────────────────────────
            val patients = patientService.searchPatients(name)
            if (patients.isEmpty()) {
                println("No patients found for '$name'.")
                continue
            }
            println("Found ${patients.size} patient(s):")
            patients.forEachIndexed { index, patient ->
                println("  ${index + 1}. ${patient.fullName}  (${patient.birthDate})  [${patient.gender}]")
            }

            // ── Step 3: Prompt user to select a patient ─────────────────────────
            println("Enter selection (1-${patients.size}):")
            val selection = readLine()?.toIntOrNull()
            if (selection == null || selection !in 1..patients.size) {
                println("Invalid selection.")
                continue
            }
            val selectedPatient = patients[selection - 1]
            println("Fetching medications for ${selectedPatient.fullName}...")

            // ── Step 4: Fetch and display medications ────────────────────────────
            val medications = medicationService.getMedicationsForPatient(selectedPatient.id)
            if (medications.isEmpty()) {
                println("No medications found for ${selectedPatient.fullName}.")
                continue
            }
            println("Medications for ${selectedPatient.fullName}:")
            medications.forEach { medication ->
                println("  - ${medicationService.formatMedicationDisplay(medication)}")
            }
        }
    } catch (e: Exception) {
        println("An unexpected error occurred: ${e.message}")
    }
}
