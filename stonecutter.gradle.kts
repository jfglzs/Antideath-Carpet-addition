plugins {
    id("dev.kikugie.stonecutter")
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom-remap") version "1.16-SNAPSHOT" apply false
}
stonecutter active "26.2"

tasks.register("buildAndGather") {
    group = "build"
    dependsOn(project.subprojects.map { it.tasks.named("build") })
    doFirst {
        println("Gathering builds")
        val buildLibs: (Project) -> java.nio.file.Path = { p ->
            p.layout.buildDirectory
                .dir("libs")
                .get()
                .asFile
                .toPath()
        }
        project.delete(project.fileTree(buildLibs(rootProject)) { include("*") })
        project.subprojects.forEach { subproject ->
            project.copy {
                from(buildLibs(subproject)) {
                    include("*.jar")
                    exclude("*-dev.jar", "*-sources.jar", "*-shadow.jar", "*-javadoc.jar")
                }
                into(buildLibs(rootProject))
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
            }
        }
    }
}