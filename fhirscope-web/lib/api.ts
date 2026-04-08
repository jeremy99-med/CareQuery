import { MedicationRequest, Patient } from "@/types/fhir";

// All frontend HTTP calls go through this module.
// The /api/fhir/* prefix is proxied to the Kotlin backend via next.config.ts.

export async function searchPatients(name: string): Promise<Patient[]> {
  const res = await fetch(`/api/fhir/patients?name=${encodeURIComponent(name)}`);
  if (!res.ok) throw new Error("Failed to fetch patients");
  return res.json() as Promise<Patient[]>;
}

export async function getMedications(patientId: string): Promise<MedicationRequest[]> {
  const res = await fetch(`/api/fhir/patients/${patientId}/medications`);
  if (!res.ok) throw new Error("Failed to fetch medications");
  return res.json() as Promise<MedicationRequest[]>;
}

export async function createMedication(
  patientId: string,
  medicationName: string,
  rxNormCode: string | null,
  status: string
): Promise<MedicationRequest> {
  const res = await fetch(`/api/fhir/patients/${patientId}/medications`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ medicationName, rxNormCode, status }),
  });
  if (!res.ok) throw new Error("Failed to create medication");
  return res.json() as Promise<MedicationRequest>;
}
