package org.example.loja_das_coisas_api.shared.infra.seeder

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.product.model.*
import org.example.loja_das_coisas_api.product.repository.*
import org.example.loja_das_coisas_api.store.repository.StoreRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
@Order(3)
class ProductSeeder(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val genderRepository: GenderRepository,
    private val storeRepository: StoreRepository,
    private val colorRepository: ColorRepository,
    private val sizeRepository: SizeRepository,
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String?) {
        if (productRepository.count() > 0) return

        val store = storeRepository.findAll().find { it.storeName == "Loja das Coisas" } ?: return
        val categoryRoupas = categoryRepository.findAll().find { it.name == "Roupas" } ?: return
        val categoryCalcados = categoryRepository.findAll().find { it.name == "Calçados" } ?: return
        val categoryAcessorios = categoryRepository.findAll().find { it.name == "Acessórios" } ?: return
        val categoryEletronicos = categoryRepository.findAll().find { it.name == "Eletrônicos" } ?: return
        val genderHomem = genderRepository.findAll().find { it.name == "Homem" } ?: return
        val genderMulher = genderRepository.findAll().find { it.name == "Mulher" } ?: return
        val genderUnissex = genderRepository.findAll().find { it.name == "Unissex" } ?: return

        // Seed Colors
        val colors = listOf(
            Color(name = "Preto"),
            Color(name = "Branco"),
            Color(name = "Azul"),
            Color(name = "Vermelho"),
            Color(name = "Verde"),
            Color(name = "Cinza"),
            Color(name = "Amarelo"),
            Color(name = "Rosa")
        )
        val savedColors = colorRepository.saveAllAndFlush(colors)

        // Seed Sizes for Roupas
        val sizesRoupas = listOf(
            Size(value = "P", category = categoryRoupas),
            Size(value = "M", category = categoryRoupas),
            Size(value = "G", category = categoryRoupas),
            Size(value = "GG", category = categoryRoupas),
            Size(value = "XG", category = categoryRoupas)
        )
        val savedSizesRoupas = sizeRepository.saveAllAndFlush(sizesRoupas)

        // Seed Sizes for Calçados
        val sizesCalcados = (35..45).map {
            Size(value = it.toString(), category = categoryCalcados)
        }
        val savedSizesCalcados = sizeRepository.saveAllAndFlush(sizesCalcados)

        val products = listOf(
            Product(
                name = "T-Shirt Slim Fit Cotton",
                imageUrl = "https://images.pexels.com/photos/4066290/pexels-photo-4066290.jpeg",
                description = "Camiseta slim fit em algodão premium, ideal para o dia a dia.",
                available = true,
                store = store,
                category = categoryRoupas,
                gender = genderHomem,
            ).apply {
                productItems = mutableListOf(
                    ProductItem(
                        product = this,
                        stockQuantity = 50,
                        price = BigDecimal("1500.00"),
                        imageUrl = "https://images.pexels.com/photos/4066290/pexels-photo-4066290.jpeg",
                        color = savedColors.find { it.name == "Preto" },
                        size = savedSizesRoupas.find { it.value == "M" }
                    ),
                    ProductItem(
                        product = this,
                        stockQuantity = 30,
                        price = BigDecimal("1500.00"),
                        imageUrl = "https://images.pexels.com/photos/4066290/pexels-photo-4066290.jpeg",
                        color = savedColors.find { it.name == "Branco" },
                        size = savedSizesRoupas.find { it.value == "G" }
                    )
                )
            },
            Product(
                name = "Sapatilhas Running Pro",
                imageUrl = "https://images.pexels.com/photos/2529148/pexels-photo-2529148.jpeg",
                description = "Sapatilhas de alta performance para corrida e treinos intensos.",
                available = true,
                store = store,
                category = categoryCalcados,
                gender = genderUnissex,
            ).apply {
                productItems = mutableListOf(
                    ProductItem(
                        product = this,
                        stockQuantity = 20,
                        price = BigDecimal("4500.00"),
                        imageUrl = "https://images.pexels.com/photos/2529148/pexels-photo-2529148.jpeg",
                        color = savedColors.find { it.name == "Azul" },
                        size = savedSizesCalcados.find { it.value == "42" }
                    ),
                    ProductItem(
                        product = this,
                        stockQuantity = 15,
                        price = BigDecimal("4500.00"),
                        imageUrl = "https://images.pexels.com/photos/2529148/pexels-photo-2529148.jpeg",
                        color = savedColors.find { it.name == "Preto" },
                        size = savedSizesCalcados.find { it.value == "40" }
                    )
                )
            },
            Product(
                name = "Vestido Floral de Verão",
                imageUrl = "https://images.pexels.com/photos/1055691/pexels-photo-1055691.jpeg",
                description = "Vestido leve e fresco com estampa floral, perfeito para dias quentes.",
                available = true,
                store = store,
                category = categoryRoupas,
                gender = genderMulher,
            ).apply {
                productItems = mutableListOf(
                    ProductItem(
                        product = this,
                        stockQuantity = 25,
                        price = BigDecimal("3200.00"),
                        imageUrl = "https://images.pexels.com/photos/1055691/pexels-photo-1055691.jpeg",
                        color = savedColors.find { it.name == "Rosa" },
                        size = savedSizesRoupas.find { it.value == "P" }
                    ),
                    ProductItem(
                        product = this,
                        stockQuantity = 20,
                        price = BigDecimal("3200.00"),
                        imageUrl = "https://images.pexels.com/photos/1055691/pexels-photo-1055691.jpeg",
                        color = savedColors.find { it.name == "Rosa" },
                        size = savedSizesRoupas.find { it.value == "M" }
                    )
                )
            },
            Product(
                name = "Relógio Digital Sport",
                imageUrl = "https://images.pexels.com/photos/277390/pexels-photo-277390.jpeg",
                description = "Relógio digital resistente à água com cronômetro e alarme.",
                available = true,
                store = store,
                category = categoryAcessorios,
                gender = genderUnissex,
            ).apply {
                productItems = mutableListOf(
                    ProductItem(
                        product = this,
                        stockQuantity = 40,
                        price = BigDecimal("2500.00"),
                        imageUrl = "https://images.pexels.com/photos/277390/pexels-photo-277390.jpeg",
                        color = savedColors.find { it.name == "Preto" },
                        size = null
                    ),
                    ProductItem(
                        product = this,
                        stockQuantity = 35,
                        price = BigDecimal("2500.00"),
                        imageUrl = "https://images.pexels.com/photos/277390/pexels-photo-277390.jpeg",
                        color = savedColors.find { it.name == "Cinza" },
                        size = null
                    )
                )
            },
            Product(
                name = "Auscultadores Wireless Noise Cancelling",
                imageUrl = "https://images.pexels.com/photos/3394651/pexels-photo-3394651.jpeg",
                description = "Auscultadores sem fios com cancelamento ativo de ruído e 30h de autonomia.",
                available = true,
                store = store,
                category = categoryEletronicos,
                gender = genderUnissex,
            ).apply {
                productItems = mutableListOf(
                    ProductItem(
                        product = this,
                        stockQuantity = 10,
                        price = BigDecimal("12000.00"),
                        imageUrl = "https://images.pexels.com/photos/3394651/pexels-photo-3394651.jpeg",
                        color = savedColors.find { it.name == "Preto" },
                        size = null
                    ),
                    ProductItem(
                        product = this,
                        stockQuantity = 12,
                        price = BigDecimal("12000.00"),
                        imageUrl = "https://images.pexels.com/photos/3394651/pexels-photo-3394651.jpeg",
                        color = savedColors.find { it.name == "Branco" },
                        size = null
                    )
                )
            },
            Product(
                name = "Calças Jeans Regular Fit",
                imageUrl = "https://images.pexels.com/photos/1082528/pexels-photo-1082528.jpeg",
                description = "Calças jeans clássicas em azul denim, corte regular.",
                available = true,
                store = store,
                category = categoryRoupas,
                gender = genderHomem,
            ).apply {
                productItems = mutableListOf(
                    ProductItem(
                        product = this,
                        stockQuantity = 25,
                        price = BigDecimal("2800.00"),
                        imageUrl = "https://images.pexels.com/photos/1082528/pexels-photo-1082528.jpeg",
                        color = savedColors.find { it.name == "Azul" },
                        size = savedSizesRoupas.find { it.value == "GG" }
                    )
                )
            }
        )

        productRepository.saveAllAndFlush(products)
    }
}