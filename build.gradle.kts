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

val minecraftVer = stonecutter.current.version
//val modver = "${property("mod_version")}"
val modver = "1.3.0"
val mod = "${modver}+${minecraftVer}+build.${SimpleDateFormat("yyMMddHHmm").format(Date())}"
val archivesBaseName = project.findProperty("archives_base_name")

base {
    archivesName.set("${archivesBaseName}+${mod}")
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
    "mappings"(loom.officialMojangMappings())
    "modImplementation"("net.fabricmc:fabric-loader:${property("loader_version")}")
    "modImplementation"("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    "modImplementation"("curse.maven:carpet-349239:${property("carpet_core_version")}")
}

tasks.processResources {
    from("aca.accesswidener")

    inputs.property("modver", modver)
    inputs.property("mc", minecraftVer)

    filesMatching("fabric.mod.json") {
        val valueMap = mapOf(
            "version" to modver,
            "mc" to minecraftVer
        )
        expand(valueMap)
    }
}

tasks.withType<Test> {
    enabled = false
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    inputs.property("archivesName", base.archivesName)
    from("LICENSE") {
        rename { fileName ->
            "${fileName}_${base.archivesName.get()}"
        }
    }
}

stonecutter {
    replacements.string(current.parsed < "26.1") {
        replace("net.minecraft.world.entity.npc.villager.Villager", "net.minecraft.world.entity.npc.Villager")
    }
}

