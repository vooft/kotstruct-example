import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.KotlinGenerator

plugins {
    `java-library`
    alias(libs.plugins.openapi)
    alias(libs.plugins.jooq)
}

dependencies {
    // openapi
    compileOnly("org.springframework.boot:spring-boot-starter-web")

    // jooq
    api(libs.jooq.core)
    jooqCodegen(libs.jooq.liquibase)
}

// OpenAPI
openApiGenerate {
    generatorName.set("kotlin-spring")
    skipOverwrite.set(true)

    inputSpec.set("$projectDir/src/main/resources/products.yaml")
    outputDir.set("$projectDir/build/generated")

    packageName.set("io.github.kotstruct")

    // openapi can generate Instant, but we'll use default OffsetDateTime
//    typeMappings.put("DateTime", "java.time.Instant")
//    importMappings.put("DateTime", "java.time.Instant")

    modelNameSuffix.set("Dto")

    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "serializationLibrary" to "jackson",
            "useSpringBoot3" to "true",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "enumPropertyNaming" to "original",
            "documentationProvider" to "none",
            "exceptionHandler" to "false",
            "useBeanValidation" to "false",
            "skipDefaultInterface" to "true",
            "apiSuffix" to "ServerApi",
        ),
    )
}

tasks.withType<KotlinCompile> {
    dependsOn("openApiGenerate")
}

kotlin.sourceSets["main"].kotlin.srcDir("$projectDir/build/generated/src/main/kotlin")

// jOOQ
jooq {
    configuration {
        generator {
            name = KotlinGenerator::class.qualifiedName
            generate {
                isKotlinNotNullRecordAttributes = true
            }
            database {
                name = "org.jooq.meta.extensions.liquibase.LiquibaseDatabase"

                forcedTypes {
                    forcedType {
                        name = "INSTANT"
                        includeTypes = "(?i:TIMESTAMP\\ WITH\\ TIME\\ ZONE)"
                    }
                }

                properties {
                    property {
                        key = "rootPath"
                        value = "$projectDir/src/main/resources"
                    }

                    property {
                        key = "scripts"
                        value = "database.yaml"
                    }
                }
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    dependsOn(tasks.getByName("jooqCodegen"))
}
