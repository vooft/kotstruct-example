package io.github.kotstruct.products

import io.github.kotstruct.KotStructGeneratedProductMapper
import io.github.kotstruct.example.ProductModel
import org.jooq.DSLContext
import org.jooq.generated.tables.references.PRODUCTS
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class ProductsRepository(private val dsl: DSLContext) {

    private val mapper = KotStructGeneratedProductMapper()

    fun insert(model: ProductModel): ProductModel {
        val record = mapper.mapModelToJooq(model)
        val insertedRecord = dsl.insertInto(PRODUCTS)
            .set(record)
            .returning()
            .fetchSingle()
        return mapper.mapJooqToModel(insertedRecord)
    }

    fun findById(id: UUID): ProductModel? {
        val record = dsl.selectFrom(PRODUCTS).where(PRODUCTS.ID.eq(id)).fetchOne() ?: return null
        return mapper.mapJooqToModel(record)
    }
}
