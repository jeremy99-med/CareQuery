"use client";

import { useState, useEffect, useRef } from "react";

export interface RxNormDrug {
  name: string;
  rxNormCode: string;
}

interface Props {
  onSelect: (drug: RxNormDrug) => void;
}

export default function MedicationSearch({ onSelect }: Props) {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState<RxNormDrug[]>([]);
  const [loading, setLoading] = useState(false);
  const [open, setOpen] = useState(false);
  const debounceTimer = useRef<ReturnType<typeof setTimeout> | null>(null);

  useEffect(() => {
    if (query.trim().length < 2) {
      setResults([]);
      setOpen(false);
      return;
    }

    // Debounce — wait 300ms after the user stops typing before calling RxNorm
    if (debounceTimer.current) clearTimeout(debounceTimer.current);
    debounceTimer.current = setTimeout(async () => {
      setLoading(true);
      try {
        const res = await fetch(
          `https://rxnav.nlm.nih.gov/REST/drugs.json?name=${encodeURIComponent(query.trim())}`
        );
        const data = await res.json();

        // RxNorm returns conceptGroup[] — flatten to a deduplicated name+code list
        const drugs: RxNormDrug[] = [];
        const seen = new Set<string>();
        const groups = data?.drugGroup?.conceptGroup ?? [];
        for (const group of groups) {
          for (const concept of group.conceptProperties ?? []) {
            if (!seen.has(concept.rxcui)) {
              seen.add(concept.rxcui);
              drugs.push({ name: concept.name, rxNormCode: concept.rxcui });
            }
            if (drugs.length >= 10) break;
          }
          if (drugs.length >= 10) break;
        }

        setResults(drugs);
        setOpen(drugs.length > 0);
      } catch {
        setResults([]);
      } finally {
        setLoading(false);
      }
    }, 300);
  }, [query]);

  function handleSelect(drug: RxNormDrug) {
    setQuery(drug.name);
    setOpen(false);
    onSelect(drug);
  }

  return (
    <div className="position-relative">
      <input
        type="text"
        className="form-control"
        placeholder="Search medications..."
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        onBlur={() => setTimeout(() => setOpen(false), 150)}
        onFocus={() => results.length > 0 && setOpen(true)}
      />
      {loading && <small className="text-muted">Searching...</small>}
      {open && (
        <ul className="list-group position-absolute w-100 shadow" style={{ zIndex: 1000 }}>
          {results.map((drug) => (
            <li
              key={drug.rxNormCode}
              className="list-group-item list-group-item-action"
              style={{ cursor: "pointer" }}
              onMouseDown={() => handleSelect(drug)}
            >
              {drug.name}
              <small className="text-muted ms-2">RxNorm: {drug.rxNormCode}</small>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
