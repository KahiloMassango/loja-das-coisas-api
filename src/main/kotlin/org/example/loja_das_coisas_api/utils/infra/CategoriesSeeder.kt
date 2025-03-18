package api.store.Utils.infra

import jakarta.annotation.PostConstruct
import org.example.loja_das_coisas_api.repositories.CategoryRepository
import org.springframework.stereotype.Component

@Component
class CategoriesSeeder(
    private val categoryRepository: CategoryRepository
) {
    @PostConstruct
    fun seedCategories() {
        /*val categories = listOf(
            Category(name = "Roupas"),
            Category(name = "Calçados"),
            Category(name = "Acessórios",),
            Category(name = "Skincare"),
            Category(name = "Perfumes")

        )

        categoryRepository.saveAll(categories)*/
    }
} /*

@PostConstruct
    public void seedCategories() {
        List<Category> categories = Arrays.asList(
            new Category("Eletrônicos", "Produtos eletrônicos como smartphones, laptops, etc."),
            new Category("Moda", "Roupas, calçados e acessórios de moda."),
            new Category("Livros", "Livros de diversos gêneros e autores."),
            new Category("Esportes", "Equipamentos e acessórios esportivos."),
            new Category("Casa e Jardim", "Produtos para casa e jardim."),
            new Category("Saúde e Beleza", "Produtos de saúde e beleza."),
            new Category("Brinquedos", "Brinquedos para crianças de todas as idades."),
            new Category("Automotivo", "Acessórios e peças para automóveis."),
            new Category("Alimentos e Bebidas", "Produtos alimentícios e bebidas."),
            new Category("Informática", "Produtos de informática e tecnologia.")
        );

        categoriesRepo.saveAll(categories);
    }
 */


