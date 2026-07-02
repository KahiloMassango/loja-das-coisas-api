package org.example.loja_das_coisas_api.product.controller

import org.example.loja_das_coisas_api.shared.dto.APIResponse
import org.example.loja_das_coisas_api.shared.dto.Response
import org.example.loja_das_coisas_api.product.dto.sync.LastUpdatedDtoRes
import org.example.loja_das_coisas_api.product.dto.sync.SyncMetadataDtoRes
import org.example.loja_das_coisas_api.product.service.SyncService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/sync")
class SyncController(
    private val syncService: SyncService
) {

    @GetMapping("/last-updated")
    fun getLastUpdated(): ResponseEntity<Response<LastUpdatedDtoRes>> {
        return ResponseEntity.ok(APIResponse.success(syncService.getLastUpdated()))
    }

    @GetMapping
    fun syncMetadata(): ResponseEntity<Response<SyncMetadataDtoRes>> {
        return ResponseEntity.ok(APIResponse.success(syncService.getSyncMetadata()))
    }
}
