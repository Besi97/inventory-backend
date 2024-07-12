package dev.besi.inventory.backend.resolver

import dev.besi.inventory.backend.mapper.ProductMapper
import dev.besi.inventory.backend.repository.model.ProductRepository
import dev.besi.inventory.graphql.api.ProductsQueryResolver
import dev.besi.inventory.graphql.model.Product
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class DemoResolver(
    val productRepository: ProductRepository,
    val productMapper: ProductMapper
): ProductsQueryResolver {
    @QueryMapping
    override fun products(): List<Product> =
        productMapper.map(productRepository.findAll().toList())
}