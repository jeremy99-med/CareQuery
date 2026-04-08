"use client";

import { useState } from "react";

interface Props {
  onSearch: (query: string) => void;
  loading?: boolean;
}

export default function PatientSearch({ onSearch, loading }: Props) {
  const [value, setValue] = useState("");

  function handleSubmit(event: React.FormEvent) {
    event.preventDefault();
    onSearch(value.trim());
  }

  return (
    <form onSubmit={handleSubmit} className="flex flex-col items-start gap-2">
      <input
        type="text"
        value={value}
        onChange={(e) => setValue(e.target.value)}
        placeholder="Search patients..."
      />
      <button type="submit" disabled={loading || !value.trim()}>
        {loading ? "Searching..." : "Search"}
      </button>
      {/* The HAPI FHIR sandbox matches single name tokens more reliably than full names */}
      <p>Tip: search by first or last name only (e.g. &apos;Ved&apos; not &apos;Ved Prakash&apos;)</p>
    </form>
  );
}
