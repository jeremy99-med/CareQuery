/**
 * components/MedicationList.tsx — Medication Display Component
 *
 * Renders a list of medication requests for a patient.
 *
 * PROPS:
 *   medications: MedicationRequest[]
 *     The list of medications to display. May be empty.
 *
 * COMPONENT BEHAVIOUR:
 *   - If medications is empty, render a "No medications found" message.
 *   - For each medication, display:
 *       Medication name | Status badge | Prescribed date
 *   - The status should be styled as a badge with a colour indicating severity:
 *       "active"    → green
 *       "completed" → grey
 *       "cancelled" → red
 *       (other)     → yellow / default
 *
 * DATE FORMATTING:
 *   authoredOn may be a full datetime string ("2026-02-12T17:30:36+05:30")
 *   or a date-only string ("2023-10-01"). In both cases, read only the first
 *   10 characters before formatting:
 *     const dateStr = medication.authoredOn?.slice(0, 10) ?? "Unknown"
 *   Then format it for display:
 *     new Date(dateStr).toLocaleDateString("en-US", { year: "numeric", month: "short", day: "numeric" })
 *
 * HINTS:
 *   - This component does not need "use client" — it only renders props.
 *   - Separate the status colour logic into a small helper function to keep
 *     the JSX readable:
 *       function statusColor(status?: string): string { ... }
 *
 * TODO: Import MedicationRequest from @/types/fhir.
 * TODO: Define Props: { medications: MedicationRequest[] }
 * TODO: Render empty state if medications.length === 0.
 * TODO: Map over medications and render a row for each.
 * TODO: Implement status badge with colour based on status value.
 * TODO: Format authoredOn using .slice(0, 10) and toLocaleDateString.
 */

import { MedicationRequest } from "@/types/fhir";

interface Props {
  medications: MedicationRequest[];
}

export default function MedicationList({ medications }: Props) {
  // TODO: Replace placeholder with real implementation
  if (medications.length === 0) {
    return <p>No medications found.</p>;
  }

  return (
    <ul>
      {medications.map((med) => (
        <li key={med.id}>
          {/* TODO: Add status badge with colour */}
          {med.medicationName} — [{med.status}] — {med.authoredOn?.slice(0, 10) ?? "Unknown"}
        </li>
      ))}
    </ul>
  );
}
