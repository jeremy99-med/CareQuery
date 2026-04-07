/**
 * app/patient/[id]/page.tsx — Patient Medications Page
 *
 * Displays the medication history for a single patient.
 * The [id] segment in the folder name is a dynamic route parameter —
 * Next.js automatically passes it as props.params.id.
 *
 * PAGE FLOW:
 *   1. Extract patientId from params.id
 *   2. Call getMedications(patientId) from lib/api.ts
 *   3. Pass results to <MedicationList />
 *   4. Show the patient's name in the heading
 *
 * HOW DYNAMIC ROUTES WORK IN NEXT.JS (App Router):
 *   The folder name [id] tells Next.js this segment is variable.
 *   Visiting /patient/131264020 sets params.id = "131264020".
 *   See: https://nextjs.org/docs/app/building-your-application/routing/dynamic-routes
 *
 * STATE TO MANAGE:
 *   - medications: MedicationRequest[]   → results from the API
 *   - loading: boolean                   → true while fetching
 *   - error: string | null               → error message if fetch fails
 *
 * HINTS:
 *   - This must be a Client Component ("use client") if you use useState/useEffect.
 *     Alternatively, make it a Server Component and fetch directly — no useState needed.
 *   - If Server Component: async function PatientPage({ params }) { const meds = await getMedications(params.id) }
 *   - Pass the patient name through the URL as a query param (?name=Ved+Prakash)
 *     so you can display it in the heading without an extra fetch.
 *
 * TODO: Decide: Server Component (simpler, no loading state) or Client Component.
 * TODO: Extract params.id and fetch medications on mount (or at render if server).
 * TODO: Render a back button linking to / (home).
 * TODO: Display the patient name in an <h1> — read it from searchParams.name if passed.
 * TODO: Render <MedicationList medications={medications} />.
 * TODO: Handle the empty state: "No medications found for this patient."
 */

export default function PatientPage({ params }: { params: { id: string } }) {
  // TODO: Replace this placeholder with the real implemented page
  return (
    <div>
      <a href="/">← Back to search</a>
      <h1>Medications for patient {params.id}</h1>
      <p>Medication list goes here.</p>
      {/* TODO: <MedicationList /> */}
    </div>
  );
}
