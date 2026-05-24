#!/usr/bin/env kotlin

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter").version("0.9.2")
}

stonecutter {
    create(rootProject) {
        versions("1.21.5", "1.21.8", "1.21.10", "1.21.11", "26.1.2", "26.2").buildscript("build.gradle.kts")
        vcsVersion = "26.2"
    }
}

rootProject.name = "antideath-carpet-addition"
