import java.util.Date
import java.text.SimpleDateFormat

plugins {
    id("fabric-loom")
//    id 'maven-publish'
}


//val modver = property("mod_version")
//val minecraftVer = sc.current.version
////val name = "${modver}+mc+${minecraftVer}+build.${Date().format('yyMMddHHmm')}"
////base.archivesName = property("mod.id") as String + archives_base_name

val modver = project.findProperty("mod_version")?.toString()
val minecraftVer = stonecutter.current.version
val archivesBaseName = project.findProperty("archives_base_name")?.toString() ?: "mod-id"
val formattedDate = SimpleDateFormat("yyMMddHHmm").format(Date())
val customName = "${modver}+mc+${minecraftVer}+build.${formattedDate}"
base {
    archivesName.set("${archivesBaseName}+${customName}")
}


repositories {
    mavenCentral()
    // 阿里云镜像
    maven {
        url = uri("https://maven.aliyun.com/repository/public/")
    }
    // CurseMaven
    maven {
        url = uri("https://www.cursemaven.com")
    }
    // Modrinth
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
    // JitPack
    maven {
        url = uri("https://jitpack.io")
    }
}

loom {
    accessWidenerPath.set(file("aca.accesswidener"))

    runConfigs.all {
//        ideConfigGenerated = true
        vmArgs("-Dmixin.debug.export=true")
        vmArgs("-XX:+AllowEnhancedClassRedefinition")
        runDir("../../run")
    }
}

configurations {
//    // 获取 modRuntimeOnly 配置并添加排除规则
//    named("modRuntimeOnly") {
//        exclude(group = "net.fabricmc", module = "fabric-loader")
//    }
}

dependencies {
    "minecraft"("com.mojang:minecraft:${minecraftVer}")
//    "mappings"(loom.officialMojangMappings())
    "implementation"("net.fabricmc:fabric-loader:${property("loader_version")}")
    "implementation"("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    "implementation"("curse.maven:carpet-349239:${property("carpet_core_version")}")
}

tasks.processResources {
    from("aca.accesswidener")

    inputs.property("version", project.property("modver"))
    inputs.property("mc", project.property("minecraftVer"))

    filesMatching("fabric.mod.json") {
        val valueMap = mapOf(
            "version" to (project.property("modver") ),
            "mc" to (sc.current.version)
        )
        expand(valueMap)
    }
}

//tasks.withType(JavaCompile).configureEach {
//	it.options.release = 21
//}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    inputs.property("archivesName", base.archivesName)
    from("LICENSE") {
        rename { fileName ->
            // 在 Kotlin 中，it 指代当前文件名
            // 必须调用 .get() 获取 Property 的值
            "${fileName}_${base.archivesName.get()}"
        }
    }
}


