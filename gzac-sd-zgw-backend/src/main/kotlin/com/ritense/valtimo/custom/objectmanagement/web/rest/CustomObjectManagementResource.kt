package com.ritense.valtimo.custom.objectmanagement.web.rest

import com.ritense.objectenapi.client.ObjectWrapper
import com.ritense.objectmanagement.service.ObjectManagementFacade
import com.ritense.valtimo.custom.objectmanagement.dto.ObjectManagementTitle
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/object/management/configuration"])
class CustomObjectManagementResource(
    private val objectManagementFacade: ObjectManagementFacade
) {
    @GetMapping(value = ["/{objectName}/objects"])
    fun getObjectsByName(
        @PathVariable objectName: String,
        @RequestParam searchString: String?
    ): ResponseEntity<List<ObjectWrapper>> {
        val exists = ObjectManagementTitle.entries.any { it.value == objectName }
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }

        return ResponseEntity.ok(objectManagementFacade.getObjectsUnpaged(objectName, searchString, "").results)
    }
}