package dev.besi.inventory.backend.resolver

import dev.besi.inventory.graphql.api.ProductsQueryResolver
import dev.besi.inventory.graphql.model.Product
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class DemoResolver: ProductsQueryResolver {
    @QueryMapping
    override fun products(): List<Product> =
        listOf(
            Product("123", "test product", 123.98),
            Product("124", "another one", 420.69),
            Product("125", "wooooow", 666.0)
        )
}