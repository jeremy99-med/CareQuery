"use client";

import { useEffect, useState } from "react";
import { useParams, useSearchParams } from "next/navigation";
import { MedicationRequest } from "@/types/fhir";
import { getMedications, createMedication } from "@/lib/api";
import MedicationList from "@/components/MedicationList";
import MedicationSearch, { RxNormDrug } from "@/components/MedicationSearch";
import toast from "react-hot-toast";

export default function PatientPage() {
  const { id } = useParams() as { id: string };
  // name is passed as a query param by PatientList so we can display it
  // immediately without a second patient-lookup API call.
  const name = useSearchParams().get("name") ?? id;
  const [medications, setMedications] = useState<MedicationRequest[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Add medication modal state
  const [showModal, setShowModal] = useState(false);
  const [selectedDrug, setSelectedDrug] = useState<RxNormDrug | null>(null);
  const [status, setStatus] = useState("active");
  const [submitting, setSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  // Extracted from useEffect so handleAddMedication can call it to refresh
  // the list after a successful POST without duplicating the fetch logic.
  function fetchMedications() {
    setLoading(true);
    setError(null);
    getMedications(id)
      .then((meds) => setMedications(meds))
      .catch(() => setError("Failed to fetch medications. Please try again."))
      .finally(() => setLoading(false));
  }

  useEffect(() => {
    fetchMedications();
  }, [id]);

  async function handleAddMedication() {
    if (!selectedDrug) return;
    setSubmitting(true);
    setSubmitError(null);
    try {
      await createMedication(id, selectedDrug.name, selectedDrug.rxNormCode, status);
      toast.success("Medication added!");
      setShowModal(false);
      setSelectedDrug(null);
      setStatus("active");
      fetchMedications(); // refresh the list
    } catch {
      setSubmitError("Failed to add medication. Please try again.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <>
      <div className="page-header d-flex justify-content-between align-items-center">
        <div>
          <a href="/" className="back-link">← Back to search</a>
          <h1>Medications for {name}</h1>
        </div>
        <button className="btn-add" onClick={() => setShowModal(true)}>
          + Add Medication
        </button>
      </div>

      <div className="results-section">
        {loading && <p className="text-center text-muted">Loading medications...</p>}
        {error && <p className="text-danger">{error}</p>}
        {!loading && !error && <MedicationList medications={medications} />}
      </div>

      
      {showModal && (
        <div className="modal d-block" style={{ backgroundColor: "rgba(0,0,0,0.5)" }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Add Medication</h5>
                <button className="btn-close" onClick={() => setShowModal(false)} />
              </div>
              <div className="modal-body">
                <div className="mb-3">
                  <label className="form-label">Medication</label>
                  <MedicationSearch onSelect={(drug) => setSelectedDrug(drug)} />
                  {selectedDrug && (
                    <small className="text-muted">
                      Selected: {selectedDrug.name} (RxNorm: {selectedDrug.rxNormCode})
                    </small>
                  )}
                </div>
                <div className="mb-3">
                  <label className="form-label">Status</label>
                  <select
                    className="form-select"
                    value={status}
                    onChange={(e) => setStatus(e.target.value)}
                  >
                    <option value="active">Active</option>
                    <option value="completed">Completed</option>
                    <option value="cancelled">Cancelled</option>
                  </select>
                </div>
                {submitError && <p className="text-danger">{submitError}</p>}
              </div>
              <div className="modal-footer">
                <button className="btn btn-secondary" onClick={() => setShowModal(false)}>
                  Cancel
                </button>
                <button
                  className="btn btn-primary"
                  onClick={handleAddMedication}
                  disabled={!selectedDrug || submitting}
                >
                  {submitting ? "Saving..." : "Save"}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
