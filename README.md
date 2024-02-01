# KotStruct Example

Example service for [kotstruct](https://github.com/vooft/kotstruct) library.

> #### IMPORTANT
> Mappers must be defined in a separate module, because KSP need them to be available
> in the classpath. 
> The recommended structure is to have a separate module for DTO/DB/Model objects in one module, 
> mapper in another, and all usages in others.
> 
> This project structured in a following way:
> - /models - DB models, DTOs and jOOQ classes
> - /mappers - KotStruct mapper definitions
> - /app - actual application

## Endpoints
This service has 2 endpoints:
* POST `/products` - creates a new product from a payload
* GET `/products/{id}` - retrieves a product from a database

Endpoints are defined in a swagger file [products.yaml](./models/src/main/resources/products.yaml). 

Swagger is used to auto-generate interfaces and DTOs using [OpenAPI Generator](https://openapi-generator.tech/).

## Database
Database has one table called `products` that is defined by a liquibase migration [database.yaml](./models/src/main/resources/database.yaml).

This migration is used by `jOOQ` codegen plugin to generate type-safe database accessors.

## Internal representation
Internally, products are represented by `ProductModel`, which is created manually and designed to be used for any business-logic.


# Mapper
Mappers are defined in the file [ProductMapper.kt](./mappers/src/main/kotlin/io/github/kotstruct/example/ProductMapper.kt).

Descriptor is provided next to it, and is used to generate the actual mapper.
