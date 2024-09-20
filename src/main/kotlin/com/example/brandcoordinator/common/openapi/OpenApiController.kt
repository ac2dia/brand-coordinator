package com.example.brandcoordinator.common.openapi

import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files

private const val OPEN_API_YAML_FILE = "openapi.yaml"

@RestController
class OpenApiController {

    @GetMapping(path = ["/docs/opeapi.yaml"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun getOpenApiYaml(): ResponseEntity<String> {
        val resource = ClassPathResource(OPEN_API_YAML_FILE)
        return ResponseEntity.ok(String(Files.readAllBytes(resource.file.toPath())))
    }
}