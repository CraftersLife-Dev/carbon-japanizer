import xyz.jpenilla.resourcefactory.bukkit.Permission
import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml.Load

val projectVersion: String by project
version = projectVersion

plugins {
    id("java")
    id("checkstyle")
    alias(libs.plugins.shadow)
    alias(libs.plugins.resource.factory)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.gremlin)
    alias(libs.plugins.indra.licenser.spotless)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

dependencies {
    // Paper
    compileOnly(libs.paper.api)

    // Integrations
    compileOnly(libs.mini.placeholders)

    // Libraries
    compileOnly(libs.configurate.yaml) // Paperに組み込んである
    runtimeDownload(libs.adventure.serializer.configurate)
    implementation(libs.doburoku.standard)

    // Annotation processor
    annotationProcessor(libs.doburoku.annotation.processor)
}

val mainPackage = "$group.papertemplate" // TODO: パッケージ名を変更
paperPluginYaml {
    name = "PaperTemplate" // TODO: プラグイン名を変更
    author = "Namiu (うにたろう)" // TODO: 自分の名前に変更
    website = "https://github.com/CraftersLife-Dev"
    apiVersion = "1.21"

    main = "$mainPackage.minecraft.paper.JavaPluginImpl"
    bootstrapper = "$mainPackage.minecraft.paper.PluginBootstrapImpl"
    loader = "$mainPackage.minecraft.paper.PluginLoaderImpl"

    permissions {
        register("${paperPluginYaml.name.get().lowercase()}.command.admin") {
            description = "${paperPluginYaml.name}の管理者系コマンド"
            default = Permission.Default.OP
        }
    }

    dependencies {
        server("MiniPlaceholders", Load.BEFORE, false)
        server("LuckPerms", Load.BEFORE, false)
    }
}

indraSpotlessLicenser {
    licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
    property("name", paperPluginYaml.name)
    property("author", paperPluginYaml.author)
    property("contributors", paperPluginYaml.contributors)
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
        archiveBaseName = paperPluginYaml.name
        archiveClassifier = null as String?
        gremlin {
            listOf("xyz.jpenilla.gremlin")
                .forEach {
                    relocate(it, "$mainPackage.libs.$it")
                }
        }
    }

    runServer {
        // runディレクトリの中にlog4j2.xmlを突っ込むとログの設定を変更可能
        // Paper: https://github.com/PaperMC/Paper/blob/main/paper-server/src/main/resources/log4j2.xml
        systemProperty("log4j.configurationFile", "log4j2.xml")
        minecraftVersion("1.21.8")
        downloadPlugins {
            modrinth("luckperms", "v5.5.0-bukkit")
            url("https://ci.codemc.io/job/MiniPlaceholders/job/MiniPlaceholders/14/artifact/jar/MiniPlaceholders-Paper-2.3.1-SNAPSHOT.jar")
            modrinth("miniplaceholders-placeholderapi-expansion", "1.2.0")
            hangar("PlaceholderAPI", "2.11.6")
        }
    }

    writeDependencies {
        repos.add("https://repo.papermc.io/repository/maven-public/")
        repos.add("https://repo.maven.apache.org/maven2/")
    }

    checkstyle {
        toolVersion = libs.versions.check.style.get()
        configDirectory = rootDir.resolve(".checkstyle")
    }
}
