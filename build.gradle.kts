import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.plugin.spring)
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}

subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")

    group = findProperty("group") ?: "io.github.kotstruct"
    version = findProperty("version") ?: "1.0-SNAPSHOT"

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    dependencies {
        implementation(platform(springBootBom(rootProject.project.libs.plugins.spring.boot.get().version)))
        implementation(platform("org.jetbrains.kotlin:kotlin-bom:${rootProject.project.libs.versions.kotlin.get()}"))
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
            showStandardStreams = true
        }
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict -Xmx1g")
            jvmTarget = "21"
        }
    }
}

fun springBootBom(version: VersionConstraint) = "org.springframework.boot:spring-boot-dependencies:$version"

