"use client";

import { useState } from "react";
import PatientSearch from "@/components/PatientSearch";
import PatientList from "@/components/PatientList";
import { searchPatients } from "@/lib/api";
import { Patient } from "@/types/fhir";

export default function HomePage() {
  const [patients, setPatients] = useState<Patient[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSearch(query: string) {
    setLoading(true);
    setError(null);
    try {
      const results = await searchPatients(query);
      setPatients(results);
    } catch (e) {
      setError("Failed to fetch patients. Please try again.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="container mt-4">
      <h1>CareQuery</h1>
      <p className="text-muted">Find a patient&apos;s medication history</p>
      <PatientSearch onSearch={handleSearch} loading={loading} />
      {error && <p className="text-danger">{error}</p>}
      {loading && <p>Searching...</p>}
      <PatientList patients={patients} />
    </div>
  );
}
