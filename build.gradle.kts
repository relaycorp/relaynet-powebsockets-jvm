/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "tech.relaycorp"

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.71"

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

    id("org.jetbrains.dokka") version "0.10.1"

    `maven-publish`

    id("com.diffplug.gradle.spotless") version "3.27.1"

    jacoco
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit5 integration.
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

java {
    withJavadocJar()
    withSourcesJar()
}

jacoco {
    toolVersion = "0.8.5"
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
        html.destination = file("$buildDir/reports/coverage")
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "CLASS"
                value = "MISSEDCOUNT"
                maximum = "0".toBigDecimal()
            }
            limit {
                counter = "METHOD"
                value = "MISSEDCOUNT"
                maximum = "0".toBigDecimal()
            }

            limit {
                counter = "BRANCH"
                value = "MISSEDCOUNT"
                maximum = "0".toBigDecimal()
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    finalizedBy("jacocoTestReport")
    doLast {
        println("View code coverage at:")
        println("file://$buildDir/reports/coverage/index.html")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.dokka {
    outputFormat = "html"
    outputDirectory = "$buildDir/docs/api"
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["java"])

            pom {
                val scmUrl = "https://github.com/relaycorp/relaynet-powebsockets-jvm"

                name.set(rootProject.name)
                description.set("PoWebSockets JVM library")
                url.set(scmUrl)
                developers {
                    developer {
                        id.set("relaycorp")
                        name.set("Relaycorp, Inc.")
                        email.set("no-reply@relaycorp.tech")
                    }
                }
                licenses {
                    license {
                        name.set("MIT")
                    }
                }
                scm {
                    connection.set("scm:git:$scmUrl.git")
                    developerConnection.set("scm:git:$scmUrl.git")
                    url.set(scmUrl)
                }
            }
        }
    }
    repositories {
        maven {
            // publish=1 automatically publishes the version
            url = uri(
                    "https://api.bintray.com/maven/relaycorp/maven/" +
                            "tech.relaycorp.powebsockets/;publish=1"
            )
            credentials {
                username = System.getenv("BINTRAY_USERNAME")
                password = System.getenv("BINTRAY_KEY")
            }
        }
    }
}

spotless {
    val ktlintUserData = mapOf(
            "max_line_length" to "100",
            "disabled_rules" to "import-ordering"
    )

    kotlin {
        ktlint("0.36.0").userData(ktlintUserData)
    }
    kotlinGradle {
        ktlint().userData(ktlintUserData)
    }
}
