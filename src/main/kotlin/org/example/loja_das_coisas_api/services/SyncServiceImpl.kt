package org.example.loja_das_coisas_api.services

import org.example.loja_das_coisas_api.dtos.sync.LastUpdatedDtoRes
import org.example.loja_das_coisas_api.dtos.sync.SyncMetadataDtoRes
import org.example.loja_das_coisas_api.models.toDto
import org.example.loja_das_coisas_api.repositories.GenderCategoryRepository
import org.example.loja_das_coisas_api.repositories.SyncRepository
import org.example.loja_das_coisas_api.services.interfaces.*
import org.springframework.stereotype.Service

@Service
class SyncServiceImpl(
    private val categoryService: CategoryService,
    private val genderService: GenderService,
    private val colorService: ColorService,
    private val sizeService: SizeService,
    private val genderCategoryRepository: GenderCategoryRepository,
    private val syncRepository: SyncRepository
): SyncService {

    override fun getLastUpdated(): LastUpdatedDtoRes {
        return LastUpdatedDtoRes(syncRepository.getLastUpdated())
    }

    override fun getSyncMetadata(): SyncMetadataDtoRes {
        return SyncMetadataDtoRes(
            categories = categoryService.getAllCategories(),
            genders = genderService.getAll(),
            genderCategoryRelations = genderCategoryRepository.findAll().map { it.toDto() },
            colors = colorService.getAllColors(),
            sizes = sizeService.getAllSizes()
        )
    }
}