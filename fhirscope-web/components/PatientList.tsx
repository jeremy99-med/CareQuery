import { Patient } from "@/types/fhir";
import Link from "next/link";

interface Props {
  patients: Patient[];
}

export default function PatientList({ patients }: Props) {
  if (patients.length === 0) {
    return <p>No patients found.</p>;
  }

  return (
    <ul>
      {patients.map((patient) => (
        <li key={patient.id}>
          <Link href={`/patient/${patient.id}?name=${encodeURIComponent(patient.fullName)}`}>
            {patient.fullName} — {patient.birthDate ?? "Unknown"} — {patient.gender}
          </Link>
        </li>
      ))}
    </ul>
  );
}
