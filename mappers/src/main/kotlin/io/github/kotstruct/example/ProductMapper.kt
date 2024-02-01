package io.github.kotstruct.example

import io.github.kotstruct.KotStructDescribedBy
import io.github.kotstruct.KotStructDescriptor
import io.github.kotstruct.KotStructDescriptor.Companion.kotStruct
import io.github.kotstruct.KotStructMapper
import io.github.kotstruct.models.ProductDto
import io.github.kotstruct.models.ProductRequestDto
import org.jooq.generated.tables.records.ProductsRecord
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit.MILLIS
import java.util.UUID

@KotStructDescribedBy(ProductMapperDescriptor::class)
interface ProductMapper : KotStructMapper {
    fun mapModelToDto(model: ProductModel): ProductDto
    fun mapModelToJooq(model: ProductModel): ProductsRecord
    fun mapJooqToModel(record: ProductsRecord): ProductModel
}

// TODO: fix encapsulation for field mappings
@KotStructDescribedBy(ProductRequestToModelMapperDescriptor::class)
interface ProductRequestToModelMapper : KotStructMapper {
    fun mapRequestToModel(dto: ProductRequestDto): ProductModel
}

object ProductMapperDescriptor : KotStructDescriptor by kotStruct({
    mapperFor<OffsetDateTime, Instant> { it.toInstant() }
    mapperFor<Instant, OffsetDateTime> { OffsetDateTime.ofInstant(it, ZoneOffset.UTC) }

    // since there is only one accessible constructor (secondary), it will be picked up by Kotlin
    factoryFor<ProductsRecord> { ::ProductsRecord }
})

object ProductRequestToModelMapperDescriptor : KotStructDescriptor by kotStruct({
    mappingFor<ProductRequestDto, ProductModel> {
        mapFactory { UUID.randomUUID() } into { ProductModel::id }
        mapFactory { Instant.now().truncatedTo(MILLIS) } into { ProductModel::createdAt }
        mapFactory { Instant.now().truncatedTo(MILLIS) } into { ProductModel::updatedAt }
    }
})
