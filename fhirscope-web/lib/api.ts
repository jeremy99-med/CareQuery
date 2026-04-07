/**
 * lib/api.ts — API Client
 *
 * The ONLY place in the frontend that makes HTTP calls.
 * Components never fetch directly — they call functions here.
 *
 * CURRENT STATE (Phase 1):
 *   Returns hardcoded mock data so the UI can be built and tested
 *   without a running backend. Replace each function body in Phase 2.
 *
 * PHASE 2 — Connecting to the Kotlin Backend:
 *   The Kotlin backend will need to be converted from a CLI app to an
 *   HTTP server (e.g., using Ktor or Spring Boot) before this layer can
 *   make real requests. See FUTURE_IMPROVEMENTS.md in fhirscope/.
 *
 *   Once the backend is running, replace the mock returns with:
 *     const res = await fetch(`/api/fhir/patients?name=${name}`)
 *     if (!res.ok) throw new Error("Failed to fetch patients")
 *     return res.json()
 *
 *   The /api/fhir/* prefix will be proxied to the Kotlin server via
 *   the rewrites() rule in next.config.ts.
 *
 * ERROR HANDLING:
 *   TODO: Throw a typed error (or return a Result<T>) so components can
 *   distinguish network errors from empty results.
 */

import { MedicationRequest, Patient } from "@/types/fhir";

// ── Mock data for Phase 1 UI development ────────────────────────────────────
// These mirror the test patients confirmed on the HAPI FHIR sandbox.
// Remove once Phase 2 is wired up.

const MOCK_PATIENTS: Patient[] = [
  { id: "131264020", fullName: "Ved Prakash", gender: "male", birthDate: "2026-02-03" },
  { id: "90629914", fullName: "Núñez Karla", gender: "female", birthDate: null ?? undefined },
];

const MOCK_MEDICATIONS: MedicationRequest[] = [
  {
    id: "131264024",
    medicationName: "yty",
    status: "cancelled",
    authoredOn: "2026-02-12",
    patientReference: "Patient/131264020",
  },
];

// ── API Functions ────────────────────────────────────────────────────────────

/**
 * searchPatients
 *
 * Searches for patients by name.
 *
 * PHASE 1: Returns mock data filtered by the name string.
 *
 * PHASE 2: Replace with:
 *   const res = await fetch(`/api/fhir/patients?name=${encodeURIComponent(name)}`)
 *   return res.json() as Promise<Patient[]>
 *
 * IMPORTANT — name search limitation (same as Kotlin backend):
 *   The HAPI FHIR sandbox does not match full names reliably.
 *   Search by first or last name only for best results.
 *
 * @param name Search string entered by the user
 * @returns Array of matching patients
 */
export async function searchPatients(name: string): Promise<Patient[]> {
  // TODO (Phase 2): Replace mock return with real fetch call
  return MOCK_PATIENTS.filter((p) =>
    p.fullName.toLowerCase().includes(name.toLowerCase())
  );
}

/**
 * getMedications
 *
 * Fetches all medication requests for a given patient ID.
 *
 * PHASE 1: Returns mock data filtered by patient ID.
 *
 * PHASE 2: Replace with:
 *   const res = await fetch(`/api/fhir/patients/${patientId}/medications`)
 *   return res.json() as Promise<MedicationRequest[]>
 *
 * @param patientId The logical FHIR Patient ID
 * @returns Array of medication requests, sorted most recent first
 */
export async function getMedications(patientId: string): Promise<MedicationRequest[]> {
  // TODO (Phase 2): Replace mock return with real fetch call
  return MOCK_MEDICATIONS.filter((m) =>
    m.patientReference === `Patient/${patientId}`
  );
}
