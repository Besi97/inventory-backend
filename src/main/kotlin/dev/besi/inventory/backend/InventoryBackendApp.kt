package dev.besi.inventory.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InventoryBackendApp

fun main(args: Array<String>) {
    runApplication<InventoryBackendApp>(*args)
}
