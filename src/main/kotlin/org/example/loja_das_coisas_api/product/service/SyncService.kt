package org.example.loja_das_coisas_api.product.service

import org.example.loja_das_coisas_api.product.dto.sync.LastUpdatedDtoRes
import org.example.loja_das_coisas_api.product.dto.sync.SyncMetadataDtoRes
import org.example.loja_das_coisas_api.product.model.toDto
import org.example.loja_das_coisas_api.product.repository.GenderCategoryRepository
import org.example.loja_das_coisas_api.product.repository.SyncRepository
import org.springframework.stereotype.Service

@Service
class SyncService(
    private val categoryService: CategoryService,
    private val genderService: GenderService,
    private val colorService: ColorService,
    private val sizeService: SizeService,
    private val genderCategoryRepository: GenderCategoryRepository,
    private val syncRepository: SyncRepository
) {

    fun getLastUpdated(): LastUpdatedDtoRes {
        return LastUpdatedDtoRes(syncRepository.getLastUpdated())
    }

    fun getSyncMetadata(): SyncMetadataDtoRes {
        return SyncMetadataDtoRes(
            categories = categoryService.getAllCategories(),
            genders = genderService.getAll(),
            genderCategoryRelations = genderCategoryRepository.findAll().map { it.toDto() },
            colors = colorService.getAllColors(),
            sizes = sizeService.getAllSizes()
        )
    }
}
