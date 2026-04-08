package com.fhirscope.client

import com.fhirscope.config.ApiConfig
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class FhirClient {

    private val httpClient = OkHttpClient()

    // GET /baseR4/Patient?name={name}&_count=10
    fun searchPatientByName(name: String): String? {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(ApiConfig.BASE_URL)
            .addPathSegments("baseR4/Patient")
            .addQueryParameter("name", name)
            .addQueryParameter("_count", ApiConfig.DEFAULT_PAGE_SIZE.toString())
            .build()

        return execute(url)
    }

    // GET /baseR4/MedicationRequest?patient={patientId}&_count=10
    fun getMedicationsByPatientId(patientId: String): String? {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(ApiConfig.BASE_URL)
            .addPathSegments("baseR4/MedicationRequest")
            .addQueryParameter("patient", patientId)
            .addQueryParameter("_count", ApiConfig.DEFAULT_PAGE_SIZE.toString())
            .build()

        return execute(url)
    }

    // POST /baseR4/MedicationRequest with JSON body (not implemented yet)
    fun postMedicationRequest(body: String): String? {
        val url = HttpUrl.Builder()
            .scheme("https")
            .host(ApiConfig.BASE_URL)
            .addPathSegments("baseR4/MedicationRequest")
            .build()

        val requestBody = body.toRequestBody("application/fhir+json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Accept", ApiConfig.ACCEPT_HEADER)
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return null
            return response.body?.string()
        }
    }


    private fun execute(url: okhttp3.HttpUrl): String? {
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
