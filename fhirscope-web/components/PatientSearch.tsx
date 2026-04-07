/**
 * components/PatientSearch.tsx — Search Input Component
 *
 * A controlled input field with a search button. Calls the parent's
 * onSearch handler when the user submits.
 *
 * PROPS:
 *   onSearch: (query: string) => void
 *     Called when the user submits the form. The parent (page.tsx) owns
 *     the actual API call — this component only handles the input UI.
 *
 *   loading?: boolean
 *     When true, disable the button and show a visual indicator so the
 *     user knows a search is in progress.
 *
 * COMPONENT BEHAVIOUR:
 *   - Controlled input: track the value in local state with useState.
 *   - On form submit, call onSearch(value.trim()) and prevent page reload
 *     with event.preventDefault().
 *   - Disable the submit button when loading=true or the input is blank.
 *
 * IMPORTANT — Name Search Limitation:
 *   The HAPI FHIR sandbox does not match full names reliably.
 *   Display a hint below the input:
 *     "Tip: search by first or last name only (e.g. 'Ved' not 'Ved Prakash')"
 *
 * TODO: Add "use client" — this component uses useState.
 * TODO: Define the Props interface with onSearch and optional loading.
 * TODO: Add local state: const [value, setValue] = useState("")
 * TODO: Render a <form> with onSubmit={handleSubmit}.
 * TODO: Render an <input> bound to value/onChange.
 * TODO: Render a <button type="submit"> that is disabled when loading or blank.
 * TODO: Render the name search tip below the input.
 */

export default function PatientSearch() {
  // TODO: Replace placeholder with real implementation
  return (
    <form>
      <input type="text" placeholder="Search patients..." />
      <button type="submit">Search</button>
    </form>
  );
}
