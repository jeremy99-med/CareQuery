/**
 * app/page.tsx — Home Page (Patient Search)
 *
 * This is the entry point of the app. It renders the patient search UI.
 *
 * PAGE FLOW:
 *   1. User types a name into <PatientSearch />
 *   2. The search triggers searchPatients() from lib/api.ts
 *   3. Results are passed to <PatientList />
 *   4. User clicks a patient → navigates to /patient/[id]
 *
 * STATE TO MANAGE:
 *   - query: string          → the current search input value
 *   - patients: Patient[]    → results returned from the API
 *   - loading: boolean       → true while the fetch is in progress
 *   - error: string | null   → error message if the fetch fails
 *
 * COMPONENT STRUCTURE:
 *   <PatientSearch />   → controlled input + search button
 *   <PatientList />     → renders the results; each row links to /patient/[id]
 *
 * HINTS:
 *   - This must be a Client Component ("use client") because it uses useState
 *     and handles user interaction.
 *   - Use the useCallback hook to avoid re-creating the search handler on
 *     every render.
 *   - Debounce the search input (e.g., 300ms) to avoid firing on every keystroke.
 *
 * TODO: Add "use client" directive at the top of this file.
 * TODO: Import useState from React and Patient from @/types/fhir.
 * TODO: Import searchPatients from @/lib/api.
 * TODO: Import and render <PatientSearch /> and <PatientList />.
 * TODO: Wire up the search handler: on submit, set loading=true, call
 *       searchPatients(query), update patients state, set loading=false.
 * TODO: Show a loading indicator while loading=true.
 * TODO: Show the error message if error is not null.
 */

export default function HomePage() {
  // TODO: Replace this placeholder with the real implemented page
  return (
    <div>
      <h1>FHIRScope</h1>
      <p>Patient search goes here.</p>
      {/* TODO: <PatientSearch /> */}
      {/* TODO: <PatientList /> */}
    </div>
  );
}
