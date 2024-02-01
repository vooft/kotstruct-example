package io.github.kotstruct.products

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.kotstruct.example.ProductModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant
import java.time.temporal.ChronoUnit.MILLIS
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class ProductsControllerTest {

    @Autowired
    private lateinit var repository: ProductsRepository

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun `should get existing product`() {
        val model = ProductModel(
            id = UUID.randomUUID(),
            name = "test123",
            price = 100,
            updatedAt = Instant.now().truncatedTo(MILLIS),
            createdAt = Instant.now().truncatedTo(MILLIS)
        )

        repository.insert(model)

        mvc.perform(
            get("/products/${model.id}")
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("""
                {
                  "id": "${model.id}",
                  "name": "${model.name}",
                  "price": ${model.price},
                  "updated_at": "${model.updatedAt}",
                  "created_at": "${model.createdAt}"
                }
            """.trimIndent()))
    }

    @Test
    fun `should insert a new product`() {
        val name = "test1"
        val price = 101

        val json = mvc.perform(post("/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "name": "$name",
                  "price": $price
                }
            """.trimIndent())
        )
            .andExpect(status().isOk)
            .andReturn()
            .response
            .contentAsString

        val jsonNode = mapper.readTree(json)

        val id = UUID.fromString(jsonNode.get("id").asText())
        val model = repository.findById(id)

        assertEquals(name, model?.name)
        assertEquals(price, model?.price)
    }
}
