package org.example.loja_das_coisas_api.product.service

import org.example.loja_das_coisas_api.product.dto.sync.GenderCategoryDtoRes
import org.example.loja_das_coisas_api.product.dto.sync.LastUpdatedDtoRes
import org.example.loja_das_coisas_api.product.dto.sync.SyncMetadataDtoRes
import org.example.loja_das_coisas_api.product.repository.SyncRepository
import org.springframework.stereotype.Service

@Service
class SyncService(
    private val categoryService: CategoryService,
    private val genderService: GenderService,
    private val colorService: ColorService,
    private val sizeService: SizeService,
    private val syncRepository: SyncRepository
) {

    fun getLastUpdated(): LastUpdatedDtoRes {
        return LastUpdatedDtoRes(syncRepository.getLastUpdated())
    }

    fun getSyncMetadata(): SyncMetadataDtoRes {
        val categories = categoryService.getAllCategories()
        val allCategoryEntities = categoryService.getAllCategoryEntities()
        
        val genderCategoryRelations = allCategoryEntities.flatMap { category ->
            category.genders.map { gender ->
                GenderCategoryDtoRes(
                    genderId = gender.id!!,
                    categoryId = category.id!!
                )
            }
        }

        return SyncMetadataDtoRes(
            categories = categories,
            genders = genderService.getAll(),
            genderCategoryRelations = genderCategoryRelations,
            colors = colorService.getAllColors(),
            sizes = sizeService.getAllSizes()
        )
    }
}
