package dev.besi.inventory.backend.resolver

import dev.besi.inventory.backend.mapper.ProductMapper
import dev.besi.inventory.backend.repository.model.ProductRepository
import dev.besi.inventory.graphql.api.CreateProductMutationResolver
import dev.besi.inventory.graphql.api.ProductsQueryResolver
import dev.besi.inventory.graphql.api.UpdateProductMutationResolver
import dev.besi.inventory.graphql.model.Product
import dev.besi.inventory.graphql.model.ProductMutation
import jakarta.persistence.EntityNotFoundException
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.math.BigDecimal

@Controller
class DemoResolver(
    val productRepository: ProductRepository,
    val productMapper: ProductMapper
): ProductsQueryResolver, CreateProductMutationResolver, UpdateProductMutationResolver {
    @QueryMapping
    override fun products(): List<Product> =
        productMapper.map(productRepository.findAll().toList())

    override fun createProduct(newProduct: ProductMutation): Product =
        productRepository.save(productMapper.mapToDbModel(newProduct))
            .let { productMapper.map(it) }

    override fun updateProduct(productId: String, product: ProductMutation): Product =
        productRepository.findById(productId.toLong())
            .map {
                it.name = product.name
                it.price = BigDecimal(product.price)
                return@map productRepository.save(it)
            }
            .map { productMapper.map(it) }
            .orElseThrow { EntityNotFoundException("Product not found with ID: $productId") }
}