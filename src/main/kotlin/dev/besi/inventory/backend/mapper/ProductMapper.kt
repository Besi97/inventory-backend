package dev.besi.inventory.backend.mapper

import dev.besi.inventory.backend.repository.model.Product
import dev.besi.inventory.graphql.model.ProductMutation
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ProductMapper {
    fun map(product: Product): dev.besi.inventory.graphql.model.Product
    fun map(products: List<Product>): List<dev.besi.inventory.graphql.model.Product>

    fun mapToDbModel(product: ProductMutation): Product
}