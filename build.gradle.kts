import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

val projectVersion: String by project
version = projectVersion

plugins {
    id("java")
    id("checkstyle")
    alias(libs.plugins.shadow)
    alias(libs.plugins.resource.factory.velocity)
    alias(libs.plugins.resource.factory.paper)
    alias(libs.plugins.run.velocity)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.gremlin)
    alias(libs.plugins.indra.licenser.spotless)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

dependencies {
    // Velocity
    compileOnly(libs.velocity.api)
    compileOnly(libs.paper.api)

    // Integrations
    compileOnly(libs.mini.placeholders)
    compileOnly(libs.carbon)

    // Libraries
    implementation(libs.cloud.velocity)
    implementation(libs.cloud.paper)
    implementation(libs.configurate.hocon)
    implementation(libs.adventure.serializer.configurate)
    implementation(libs.doburoku.standard)

    // Annotation processor
    annotationProcessor(libs.doburoku.annotation.processor)

    // Database
    runtimeDownload(libs.h2)
    runtimeDownload(libs.mysql)
    runtimeDownload(libs.hikariCP)
    runtimeDownload(libs.jdbiCore)
    runtimeDownload(libs.jdbiSqlObject)
    runtimeDownload(libs.flyway)
    runtimeDownload(libs.flywayMysql) {
        isTransitive = false
    }

    // Misc
    runtimeDownload(libs.caffeine)
}

val mainPackage = "$group.carbonjapanizer"
paperPluginYaml {
    name = "CarbonJapanizer"
    main = "$mainPackage.platform.paper.PaperCarbonJapanizer"
    bootstrapper = "$mainPackage.platform.paper.PaperCarbonJapanizerBootstrapper"
    loader = "$mainPackage.platform.paper.PaperCarbonJapanizerLoader"
    apiVersion = "1.21.5"
    authors = listOf("Namiu (うにたろう)")
    website = "https://github.com/CraftersLife-Dev"

    dependencies {
        server("LuckPerms", PaperPluginYaml.Load.BEFORE, false)
        server("MiniPlaceholders", PaperPluginYaml.Load.BEFORE, false)
        server("CarbonChat", PaperPluginYaml.Load.BEFORE, true)
    }
}
velocityPluginJson {
    id = "carbonjapanizer"
    name = "CarbonJapanizer"
    version = rootProject.version.toString()
    description = project.description
    authors = listOf("Namiu (うにたろう)")
    main = "$mainPackage.platform.velocity.VelocityCarbonJapanizer"
    dependency("signedvelocity")
    dependency("luckperms", true)
    dependency("carbonchat")
}

indraSpotlessLicenser {
    licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
    property("name", "CarbonJapanizer")
    property("author", "Namiu (うにたろう)")
    property("contributors", "")
}

configurations {
    compileOnly {
        extendsFrom(configurations.runtimeDownload.get())
    }
}

tasks {
    compileJava {
        options.compilerArgs.add("-parameters")
    }

    shadowJar {
        archiveBaseName = velocityPluginJson.name
        archiveClassifier = null as String?
        gremlin {
            listOf("xyz.jpenilla.gremlin")
                .forEach {
                    relocate(it, "$mainPackage.libraries.$it")
                }
        }
    }

    runServer {
        minecraftVersion("1.21.4")
        runDirectory(File("run-paper"))
        systemProperty("log4j.configurationFile", "log4j2.xml")
        downloadPlugins {
            modrinth("luckperms", "v5.5.0-bukkit")
            modrinth("miniplaceholders", "7caNTwMh")
//            modrinth("carbon", "H4KLsB5g")
        }
    }

    runVelocity {
        velocityVersion("3.4.0-SNAPSHOT")
        runDirectory(File("run-velocity"))
        systemProperty("log4j.configurationFile", "log4j2.xml")
        downloadPlugins {
            modrinth("signedvelocity", "1.3.0")
            modrinth("luckperms", "v5.5.0-velocity")
            modrinth("miniplaceholders", "fW9GvvXS")
//            modrinth("carbon", "JdcV6BZb")
        }
    }

    writeDependencies {
        repos.add("https://repo.papermc.io/repository/maven-public/")
        repos.add("https://repo.maven.apache.org/maven2/")
        repos.add("https://central.sonatype.com/repository/maven-snapshots/")
    }

    checkstyle {
        toolVersion = libs.versions.check.style.get()
        configDirectory = rootDir.resolve(".checkstyle")
    }
}
