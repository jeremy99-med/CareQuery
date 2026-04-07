/**
 * types/fhir.ts — Shared TypeScript Types
 *
 * Mirror of the Kotlin model classes in fhirscope/src/main/kotlin/.../model/.
 * These types represent the JSON shape that the Kotlin backend will send to
 * the frontend in Phase 2.
 *
 * KEEP IN SYNC WITH:
 *   - model/Patient.kt
 *   - model/MedicationRequest.kt
 *
 * NOTE: Fields marked as optional (?) may be null/absent in real HAPI FHIR
 * sandbox data — always guard against undefined in your components.
 */

export interface Patient {
  // Logical FHIR Patient ID — used to fetch medications
  // Matches Patient.id in Kotlin
  id: string;

  // Full display name, e.g. "John Doe"
  // Matches Patient.fullName in Kotlin
  fullName: string;

  // "male" | "female" | "other" | "unknown" — may be null
  gender?: string;

  // ISO date string e.g. "1990-01-01" — may be null on some sandbox records
  birthDate?: string;
}

export interface MedicationRequest {
  // Logical FHIR MedicationRequest ID
  id: string;

  // Display name from medicationCodeableConcept.text
  // e.g. "Atorvastatin 20 MG Oral Tablet"
  // Will be "Unknown Medication" if not present
  medicationName: string;

  // FHIR status value: "active" | "cancelled" | "completed" | "stopped" etc.
  status?: string;

  // ISO date or datetime string, e.g. "2023-10-01" or "2026-02-12T17:30:36+05:30"
  // Always read only the first 10 characters if you need just the date
  authoredOn?: string;

  // Format: "Patient/{id}" — the patient this medication belongs to
  patientReference?: string;
}
