package com.fhirscope

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.fhirscope.service.MedicationService
import com.fhirscope.service.PatientService
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Request body shape for POST /patients/{id}/medications
data class CreateMedicationRequest(
    val medicationName: String,
    val rxNormCode: String?,
    val status: String
)

fun startServer(patientService: PatientService, medicationService: MedicationService) {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson {
                registerKotlinModule()
            }
        }
        routing {
            get("/patients") {
                val name = call.request.queryParameters["name"] ?: ""
                val patients = patientService.searchPatients(name)
                call.respond(patients)
            }
            get("/patients/{id}/medications") {
                val id = call.parameters["id"] ?: ""
                val medications = medicationService.getMedicationsForPatient(id)
                call.respond(medications)
            }
            post("/patients/{id}/medications") {
                val patientId = call.parameters["id"] ?: ""
                val body = call.receive<CreateMedicationRequest>()
                val result = medicationService.createMedication(
                    patientId = patientId,
                    medicationName = body.medicationName,
                    rxNormCode = body.rxNormCode,
                    status = body.status
                )
                if (result != null) {
                    call.respond(HttpStatusCode.Created, result)
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Failed to create medication")
                }
            }
        }
    }.start(wait = true)
}
