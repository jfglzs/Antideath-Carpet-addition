import dev.kikugie.stonecutter.data.ParsedVersion
import net.fabricmc.loom.api.LoomGradleExtensionAPI

fun getProperty(propertyName: String) =
    providers.gradleProperty(propertyName).orNull
        ?: project.findProperty(propertyName)?.toString()
        ?: ""

val modId = getProperty("mod_id")
val modName = getProperty("mod_name")

val mavenGroup = getProperty("maven_group")
val minecraftVersion = getProperty("minecraft_version")
val modVersion = getProperty("mod_version")
val minecraftRequirementVersion = getProperty("minecraft_requirement_version")
val loaderRequirementVersion = getProperty("loader_requirement_version")
val archivesBaseName = getProperty("archives_base_name")

val parchmentVersion = getProperty("parchment_version")

val unobfuscated = ParsedVersion(minecraftVersion) >= ParsedVersion("26.1")
apply(plugin = if (unobfuscated) "net.fabricmc.fabric-loom" else "net.fabricmc.fabric-loom-remap")
val loomExtension = extensions.getByType(LoomGradleExtensionAPI::class)

var modVersionSuffix = ""
val artifactVersion = modVersion
var artifactVersionSuffix = ""
// detect github action environment variables
// https://docs.github.com/en/actions/learn-github-actions/environment-variables#default-environment-variables
if (System.getenv("BUILD_RELEASE") != "true") {
    val buildNumber = System.getenv("BUILD_ID")
    modVersionSuffix += if (buildNumber != null) "+build.$buildNumber" else "-SNAPSHOT"
    artifactVersionSuffix = "-SNAPSHOT" // A non-release artifact is always a SNAPSHOT artifact
}
val fullModVersion = modVersion + modVersionSuffix
var fullProjectVersion = ""
var fullArtifactVersion = ""

// Example version values:
//   project.mod_version     1.0.3                      (the base mod version)
//   modVersionSuffix        +build.88                  (use github action build number if possible)
//   artifactVersionSuffix   -SNAPSHOT
//   fullModVersion          1.0.3+build.88             (the actual mod version to use in the mod)
//   fullProjectVersion      v1.0.3-mc1.15.2+build.88   (in build output jar name)
//   fullArtifactVersion     1.0.3-mc1.15.2-SNAPSHOT    (maven artifact version)

group = mavenGroup
val baseExtension = extensions.getByType(BasePluginExtension::class)
if (System.getenv("JITPACK") == "true") {
    // move mc version into archivesBaseName, so jitpack will be able to organize archives from multiple subprojects correctly
    baseExtension.archivesName.set("$archivesBaseName-mc$minecraftVersion")
    fullProjectVersion = "v$modVersion$modVersionSuffix"
    fullArtifactVersion = artifactVersion + artifactVersionSuffix
} else {
    baseExtension.archivesName.set(archivesBaseName)
    fullProjectVersion = "v$modVersion-mc$minecraftVersion$modVersionSuffix"
    fullArtifactVersion = "$artifactVersion-mc$minecraftVersion$artifactVersionSuffix"
}
version = fullProjectVersion

repositories {
    mavenCentral()
    maven {
        url = uri("https://masa.dy.fi/maven")
        content { includeGroup("carpet") }
    }
    maven {
        url = uri("https://maven.parchmentmc.org")
        content { includeGroup("org.parchmentmc.data") }
    }
    maven {
        url = uri("https://maven.fallenbreath.me/releases")
        content { includeGroup("me.fallenbreath") }
    }
}

val commonVmArgs = listOf(
    "--sun-misc-unsafe-memory-access=allow",
    "-Dmixin.debug.export=true"
)
val devVmArg = "-XX:+AllowEnhancedClassRedefinition"
loomExtension.runConfigs.configureEach {
    // to make sure it generates all "Minecraft Client (:subproject_name)" applications
    isIdeConfigGenerated = true
    runDir = if (unobfuscated) "../../run" else "../../run-obsuscated"
    vmArgs(commonVmArgs + devVmArg)
}
loomExtension.runs {
    val auditVmArg = "-DmixinAuditor.audit=true"
    register("serverMixinAudit") {
        server()
        vmArgs.add(auditVmArg)
        vmArgs.remove(devVmArg)
        isIdeConfigGenerated = false
    }
    register("clientMixinAudit") {
        client()
        vmArgs.add(auditVmArg)
        vmArgs.remove(devVmArg)
        isIdeConfigGenerated = false
    }
}

dependencies {
    fun autoImplementation(dep: Any): Dependency? =
        (add(if (unobfuscated) "implementation" else "modImplementation", dep))

    fun autoRuntimeOnly(dep: Any): Dependency? =
        (add(if (unobfuscated) "runtimeOnly" else "modRuntimeOnly", dep))

    "minecraft"("com.mojang:minecraft:${minecraftVersion}")
    if (!unobfuscated) {
        "mappings"(
            loomExtension.layered {
                officialMojangMappings()
                logger.lifecycle("parchmentVersion:$parchmentVersion")
                if (parchmentVersion.isNotEmpty()) {
                    parchment("org.parchmentmc.data:parchment-$minecraftVersion:$parchmentVersion@zip")
                }
            }
        )
    }
    autoRuntimeOnly("me.fallenbreath:mixin-auditor:0.2.0-${if (unobfuscated) "u" else "o"}")
    autoImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    autoImplementation("carpet:fabric-carpet:${property("carpet_core_version")}")
}

tasks.processResources {

    inputs.property("modver", modVersion)
    inputs.property("mc", minecraftRequirementVersion)
    inputs.property("loader", loaderRequirementVersion)
    inputs.property("id", modId)
    inputs.property("name", modName)

    filesMatching("fabric.mod.json") {
        val valueMap = mapOf(
            "version" to modVersion,
            "mc" to minecraftRequirementVersion,
            "loader" to loaderRequirementVersion,
            "id" to modId,
            "name" to modName
        )
        expand(valueMap)
    }
    filesMatching("antideath-carpet-addition.mixins.json") {
        val valueMap = mapOf(
            "compatibility_level" to if (unobfuscated) "JAVA_25" else "JAVA_21"
        )
        expand(valueMap)
    }
}

tasks.withType<Test> {
    enabled = false
}

java {
    val javaVersion = if (unobfuscated) JavaVersion.VERSION_25 else JavaVersion.VERSION_21
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    withSourcesJar()
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
    replacements.string(current.parsed < "1.21.11") {
        replace("net.minecraft.world.entity.npc.villager.Villager", "net.minecraft.world.entity.npc.Villager")
    }
}
