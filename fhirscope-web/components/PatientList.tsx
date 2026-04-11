import { Patient } from "@/types/fhir";
import Link from "next/link";

interface Props {
  patients: Patient[];
}

export default function PatientList({ patients }: Props) {
  if (patients.length === 0) {
    return null;
  }

  return (
    <div>
      {patients.map((patient) => (
        <Link
          key={patient.id}
          // Append ?name= so the patient detail page can show the name
          // instantly without fetching the patient record a second time.
          href={`/patient/${patient.id}?name=${encodeURIComponent(patient.fullName)}`}
          className="patient-card"
        >
          <div className="patient-name">{patient.fullName}</div>
          <div className="patient-meta">
            {patient.birthDate ?? "Unknown DOB"} &nbsp;·&nbsp; {patient.gender ?? "Unknown gender"}
          </div>
        </Link>
      ))}
    </div>
  );
}
