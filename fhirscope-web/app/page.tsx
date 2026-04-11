"use client";

import { useState } from "react";
import PatientSearch from "@/components/PatientSearch";
import PatientList from "@/components/PatientList";
import { searchPatients } from "@/lib/api";
import { Patient } from "@/types/fhir";

export default function HomePage() {
  // State lives here (not inside PatientSearch) so both PatientSearch and
  // PatientList can read the same result without prop-drilling or a context.
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
    <>
      <div className="hero">
        <h1 className="hero-title">CareQuery</h1>
        <p className="hero-subtitle">Find a patient&apos;s medication history</p>
        <PatientSearch onSearch={handleSearch} loading={loading} />
      </div>

      <div className="results-section">
        {error && <p className="text-danger">{error}</p>}
        {loading && <p className="text-center text-muted">Searching...</p>}
        <PatientList patients={patients} />
      </div>
    </>
  );
}
