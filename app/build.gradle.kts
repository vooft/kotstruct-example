plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.h2database:h2")
    implementation("org.liquibase:liquibase-core")

    implementation(project(":mappers"))

    ksp(libs.kotstruct.generator)
    ksp(project(":mappers"))
    ksp(project(":models"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

