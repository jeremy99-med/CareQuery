/**
 * components/PatientList.tsx — Patient Results List
 *
 * Renders a list of patients returned from a search. Each row is clickable
 * and navigates to /patient/[id] to show that patient's medications.
 *
 * PROPS:
 *   patients: Patient[]
 *     The list of patients to display. May be empty.
 *
 * COMPONENT BEHAVIOUR:
 *   - If patients is empty, render a "No patients found" message.
 *   - For each patient, render a row showing:
 *       Full name | Date of birth | Gender
 *   - Wrap each row in a Next.js <Link> pointing to /patient/[id].
 *   - Pass the patient name as a query param so the medications page can
 *     display it in the heading without an extra fetch:
 *       href={`/patient/${patient.id}?name=${encodeURIComponent(patient.fullName)}`}
 *
 * HINTS:
 *   - Use Next.js <Link> from "next/link" — not a plain <a> tag — so
 *     navigation is client-side and does not trigger a full page reload.
 *   - Guard against null birthDate: patient.birthDate ?? "Unknown"
 *   - This component does not need "use client" — it receives data as props
 *     and has no interactivity of its own.
 *
 * TODO: Import Patient from @/types/fhir.
 * TODO: Import Link from next/link.
 * TODO: Define Props: { patients: Patient[] }
 * TODO: Render empty state if patients.length === 0.
 * TODO: Map over patients and render a <Link> row for each.
 * TODO: Display fullName, birthDate (with null fallback), and gender.
 */

import { Patient } from "@/types/fhir";

interface Props {
  patients: Patient[];
}

export default function PatientList({ patients }: Props) {
  // TODO: Replace placeholder with real implementation
  if (patients.length === 0) {
    return <p>No patients found.</p>;
  }

  return (
    <ul>
      {patients.map((patient) => (
        <li key={patient.id}>
          {/* TODO: Wrap in <Link href={`/patient/${patient.id}`}> */}
          {patient.fullName} — {patient.birthDate ?? "Unknown"} — {patient.gender}
        </li>
      ))}
    </ul>
  );
}
