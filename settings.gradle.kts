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
    id("dev.kikugie.stonecutter").version("0.9.6")
}

stonecutter {
    create(rootProject) {
        versions("1.21.1","1.21.4","1.21.5", "1.21.8", "1.21.10", "1.21.11", "26.1", "26.2", "26.3").buildscript("build.gradle.kts")
        vcsVersion = "26.2"
    }
}

rootProject.name = "antideath-carpet-addition"
