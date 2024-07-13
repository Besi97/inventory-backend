package dev.besi.inventory.backend.configuration

import org.mariadb.jdbc.MariaDbPoolDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import javax.sql.DataSource

@Configuration
@PropertySource("file:/etc/inventory/db-secret/.db.properties")
class MariaDbConfiguration {
    @Bean
    fun dataSource(
        @Value("\${db.username}") username: String,
        @Value("\${db.password}") password: String,
        @Value("\${db.url}") url: String
    ): DataSource =
        MariaDbPoolDataSource().also {
            it.user = username
            it.setPassword(password)
            it.url = url
        }
}