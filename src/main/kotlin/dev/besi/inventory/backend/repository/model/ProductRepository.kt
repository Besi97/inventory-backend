package dev.besi.inventory.backend.repository.model

import org.springframework.data.repository.CrudRepository

interface ProductRepository: CrudRepository<Product, Long>