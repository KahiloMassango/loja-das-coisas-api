package api.store.Utils.infra

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class MainSeeder(
    val categoriesSeeder: CategoriesSeeder,
    val genderSeeder: GenderSeeder
) : ApplicationRunner {


    override fun run(args: ApplicationArguments) {
        categoriesSeeder.seedCategories()
        genderSeeder.seedSection()
    }
}
