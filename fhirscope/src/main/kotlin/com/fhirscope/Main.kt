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
 * CLI FLOW:
 * ─────────────────────────────────────────
 * Enter patient name:
 * > John
 *
 * Found 2 patient(s):
 *   1. John Doe          (1990-01-01)  [male]
 *   2. John Smith        (1985-05-12)  [male]
 *
 * Enter selection (1-2):
 * > 1
 *
 * Medications for John Doe:
 *   - Atorvastatin 20 MG Oral Tablet  [active]  (2023-10-01)
 *   - Lisinopril 10 MG Oral Tablet    [completed] (2023-08-15)
 * ─────────────────────────────────────────
 *
 * HOW TO RUN:
 *   ./gradlew run
 *   or: ./gradlew run --args="John"  (if you add argument parsing later)
 *
 * TODO (Future): Add command-line argument parsing with kotlinx-cli or picocli
 *   so the user can pass the name directly: ./gradlew run --args="John"
 */
fun main() {

    val patientService = PatientService()
    val medicationService = MedicationService()

    // ── Step 1: Prompt user for a patient name ──────────────────────────────
    //
    // TODO: Print a prompt message to the console, e.g., "Enter patient name:"
    // TODO: Read user input using readLine() — this blocks until the user presses Enter
    // TODO: Validate that the input is not null or blank; if it is, print an error and exit
    //
    // HINT: val name = readLine()?.trim() ?: run { println("No input provided."); return }

    val name = "" // TODO: Replace with readLine()?.trim() and validate

    // ── Step 2: Search for patients ─────────────────────────────────────────
    //
    // TODO: Call patientService.searchPatients(name)
    // TODO: If the returned list is empty, print "No patients found for '$name'" and return
    // TODO: Display the list with 1-based indices:
    //         println("Found ${patients.size} patient(s):")
    //         patients.forEachIndexed { index, patient ->
    //             println("  ${index + 1}. ${patient.fullName}  (${patient.birthDate})  [${patient.gender}]")
    //         }

    // ── Step 3: Prompt user to select a patient ─────────────────────────────
    //
    // TODO: Print "Enter selection (1-${patients.size}):"
    // TODO: Read user input with readLine()
    // TODO: Parse input as Int with toIntOrNull()
    // TODO: Validate selection is within range (1..patients.size)
    //         If invalid, print "Invalid selection." and return
    // TODO: Convert 1-based user selection to 0-based index:
    //         val selectedPatient = patients[selection - 1]

    // ── Step 4: Fetch and display medications ───────────────────────────────
    //
    // TODO: Print "Fetching medications for ${selectedPatient.fullName}..."
    // TODO: Call medicationService.getMedicationsForPatient(selectedPatient.id)
    // TODO: If the returned list is empty:
    //         println("No medications found for ${selectedPatient.fullName}.")
    //         return
    // TODO: Print "Medications for ${selectedPatient.fullName}:"
    // TODO: Iterate over medications and print each one using
    //         medicationService.formatMedicationDisplay(medication)

    // ── Placeholder output (remove when implementing the above) ─────────────
    println("FHIRScope — scaffold loaded. Implement the steps above to run the app.")

    // TODO (Future): Wrap the entire flow in a loop so the user can search again
    //   without restarting the application.

    // TODO (Future): Add a top-level try/catch to handle unexpected runtime errors
    //   gracefully rather than crashing with a stack trace.
}
