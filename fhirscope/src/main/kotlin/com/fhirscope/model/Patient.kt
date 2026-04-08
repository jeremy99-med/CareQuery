package com.fhirscope.model

data class Patient(
    val id: String,
    val fullName: String,   // assembled from name[0].given + name[0].family
    val gender: String?,    // "male" | "female" | "other" | "unknown"
    val birthDate: String?  // ISO-8601 format: "YYYY-MM-DD"
)
