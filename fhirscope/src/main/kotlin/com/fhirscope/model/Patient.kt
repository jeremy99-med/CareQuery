package com.fhirscope.model

/**
 * Patient
 *
 * A simplified data class representing a FHIR R4 Patient resource.
 *
 * FHIR JSON structure for a Patient (abridged):
 * {
 *   "resourceType": "Patient",
 *   "id": "12345",
 *   "name": [
 *     {
 *       "family": "Doe",
 *       "given": ["John"]
 *     }
 *   ],
 *   "gender": "male",
 *   "birthDate": "1990-01-01"
 * }
 *
 * IMPORTANT – Nested / array fields:
 *   • "name" is an ARRAY — a patient may have multiple name entries
 *     (e.g., official name, nickname). Always access name[0] for the
 *     primary name, and guard against an empty list.
 *   • "given" inside each name entry is also an ARRAY of strings.
 *     A full first name is assembled from given[0] (or all elements joined).
 *
 * TODO: Map additional FHIR fields as needed, e.g.:
 *   - address (array of Address objects)
 *   - telecom (phone / email)
 *   - identifier (MRN, SSN, etc.)
 */
data class Patient(

    /**
     * The server-assigned logical ID for this patient.
     * Used as the subject reference in MedicationRequest queries:
     *   GET /MedicationRequest?patient={id}
     *
     * FHIR path: Patient.id
     */
    val id: String,

    /**
     * Display-friendly full name assembled from the FHIR name structure.
     * Suggested format: "{given[0]} {family}"
     *
     * FHIR path: Patient.name[0].given[0] + Patient.name[0].family
     *
     * TODO: Build this in JsonParser or PatientService by combining
     *   the given and family fields from the raw JSON.
     */
    val fullName: String,

    /**
     * Administrative gender of the patient.
     * FHIR values: "male" | "female" | "other" | "unknown"
     *
     * FHIR path: Patient.gender
     */
    val gender: String?,

    /**
     * Patient's date of birth in ISO-8601 format (YYYY-MM-DD).
     *
     * FHIR path: Patient.birthDate
     *
     * TODO (Future): Parse this into a LocalDate for age calculation
     *   or formatted display (e.g., "Jan 1, 1990").
     */
    val birthDate: String?
)
