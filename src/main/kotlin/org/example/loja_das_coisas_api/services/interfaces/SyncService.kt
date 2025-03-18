package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.sync.LastUpdatedDtoRes
import org.example.loja_das_coisas_api.dtos.sync.SyncMetadataDtoRes

interface SyncService {

    fun getLastUpdated(): LastUpdatedDtoRes

    fun getSyncMetadata(): SyncMetadataDtoRes
}