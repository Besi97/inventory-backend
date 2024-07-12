package dev.besi.inventory.backend.repository.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false)
    var price: BigDecimal? = null
}
