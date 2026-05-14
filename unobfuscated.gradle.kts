import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("net.fabricmc.fabric-loom")
}

val minecraftVer = stonecutter.current.version
val modver = "${property("mod_version")}"
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
    "implementation"("net.fabricmc:fabric-loader:${property("loader_version")}")
    "implementation"("com.github.gnembon:fabric-carpet:${property("carpet_core_version")}")
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
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
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
    replacements.string(current.parsed > "1.21.11") {
        replace("net.minecraft.world.entity.npc.Villager", "net.minecraft.world.entity.npc.villager.Villager")
    }
}




