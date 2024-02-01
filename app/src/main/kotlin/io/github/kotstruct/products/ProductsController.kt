package io.github.kotstruct.products

import io.github.kotstruct.KotStructGeneratedProductMapper
import io.github.kotstruct.KotStructGeneratedProductRequestToModelMapper
import io.github.kotstruct.apis.ProductsServerApi
import io.github.kotstruct.models.ProductDto
import io.github.kotstruct.models.ProductRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ProductsController(private val repository: ProductsRepository) : ProductsServerApi {

    private val dtoMapper = KotStructGeneratedProductMapper()
    private val requestMapper = KotStructGeneratedProductRequestToModelMapper()

    override fun get(id: UUID): ResponseEntity<ProductDto> {
        val result = repository.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(dtoMapper.mapModelToDto(result))
    }

    override fun new(productRequestDto: ProductRequestDto): ResponseEntity<ProductDto> {
        val toInsert = requestMapper.mapRequestToModel(productRequestDto)
        val inserted = repository.insert(toInsert)
        return ResponseEntity.ok(dtoMapper.mapModelToDto(inserted))
    }
}
