package com.fhirscope.client

import com.fhirscope.config.ApiConfig
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class FhirClient {

    private val httpClient = OkHttpClient()

    /**
     * searchPatientByName
     *
     * Searches the FHIR server for patients whose name matches the given string.
     *
     * ENDPOINT:
     *   GET https://hapi.fhir.org/baseR4/Patient?name={name}
     *
     * NOTE: The HAPI FHIR sandbox does not reliably match across both given and
     * family name when combined (e.g., "Ved Prakash"). Search by a single name
     * token (first or last name only) for best results.
     *
     * @param name The patient name to search for (partial match supported)
     * @return Raw JSON string of the FHIR Bundle response, or null on error
     */
    fun searchPatientByName(name: String): String? {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(ApiConfig.BASE_URL)
            .addPathSegments("baseR4/Patient")
            .addQueryParameter("name", name)
            .addQueryParameter("_count", ApiConfig.DEFAULT_PAGE_SIZE.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("Accept", ApiConfig.ACCEPT_HEADER)
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("Error: ${response.code}")
                return null
            }
            return response.body?.string()
        }
    }

    /**
     * getMedicationsByPatientId
     *
     * Retrieves all MedicationRequest resources associated with a given patient ID.
     *
     * ENDPOINT:
     *   GET https://hapi.fhir.org/baseR4/MedicationRequest?patient={patientId}
     *
     * @param patientId The logical FHIR Patient ID (from Patient.id)
     * @return Raw JSON string of the FHIR Bundle response, or null on error
     */
    fun getMedicationsByPatientId(patientId: String): String? {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(ApiConfig.BASE_URL)
            .addPathSegments("baseR4/MedicationRequest")
            .addQueryParameter("patient", patientId)
            .addQueryParameter("_count", ApiConfig.DEFAULT_PAGE_SIZE.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .addHeader("Accept", ApiConfig.ACCEPT_HEADER)
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("Error: ${response.code}")
                return null
            }
            return response.body?.string()
        }
    }
}
