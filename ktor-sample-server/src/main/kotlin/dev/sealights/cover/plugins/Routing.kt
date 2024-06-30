package dev.sealights.cover.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.net.URL

fun Application.configureRouting() {
    val resourcesPath = "C:\\projekty\\SL.OnPremise.LineCoverageTool\\ktor-sample-server\\src\\main\\resources"
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

//        http://host.docker.internal:8080/api/v5/agents/builds/applicationName2/branchName2/buildName2/queryModifiedMethods

        post("/api/v5/agents/builds/{...}") {
            Application::class.java.getResource("/buildmap/buildmap001.json")?.let { url ->
                call.respondText(url.readText(), ContentType.Application.Json, HttpStatusCode.OK)
            }
        }
        
        get("/sl-api/v1/builds/{buildSessionId}") {
            val bsid = call.parameters["buildSessionId"]
            
            val fileUrl = URL("file:///$resourcesPath/builddata/${bsid}.json")
            fileUrl.let { url ->
                val readText = url.readText()
                call.respondText(url.readText(), ContentType.Application.Json, HttpStatusCode.OK)
            }
//            Application::class.java.getResource("/builddata/${bsid}.json")?.let { url ->
//                call.respondText(url.readText(), ContentType.Application.Json, HttpStatusCode.OK)
//            }
        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}
