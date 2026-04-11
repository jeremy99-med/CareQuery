import { MedicationRequest } from "@/types/fhir";

interface Props {
  medications: MedicationRequest[];
}

interface StatusBadgeProps {
  status?: string;
}

// Maps FHIR medication status values to Bootstrap background classes
function StatusBadge({ status }: StatusBadgeProps) {
  let color = "bg-warning";
  switch (status) {
    case "active":    color = "bg-success";   break;
    case "completed": color = "bg-secondary"; break;
    case "cancelled": color = "bg-danger";    break;
  }
  return <span className={`badge ${color}`}>{status}</span>;
}

export default function MedicationList({ medications }: Props) {
  if (medications.length === 0) {
    return <p className="text-muted text-center mt-4">No medications found.</p>;
  }

  return (
    <ul className="medication-list">
      {medications.map((med) => {
        // authoredOn can be a full datetime or date-only string; slice to YYYY-MM-DD before parsing
        const dateStr = med.authoredOn?.slice(0, 10) ?? "Unknown";
        const formattedDate = dateStr !== "Unknown"
          ? new Date(dateStr).toLocaleDateString("en-US", { year: "numeric", month: "short", day: "numeric" })
          : "Unknown";

        return (
          <li key={med.id} className="medication-card">
            <StatusBadge status={med.status} />
            <span className="medication-name">{med.medicationName}</span>
            <span className="medication-date">{formattedDate}</span>
          </li>
        );
      })}
    </ul>
  );
}
