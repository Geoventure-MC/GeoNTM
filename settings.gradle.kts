pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9.8"
    // Picks the right Fabric Loom variant per node. Loom split at the obfuscation boundary:
    // `net.fabricmc.fabric-loom` builds the unobfuscated era (26.1+) and its officialMojangMappings()
    // throws outright, while `net.fabricmc.fabric-loom-remap` carries the remapping pipeline for
    // everything below. Both ship under the same version line, so this selects the plugin id rather
    // than juggling two Loom versions on one buildscript classpath. Its default selector is exactly
    // ours — obfuscated below 26 — so no explicit `loomx.obfuscated` is needed.
    id("dev.kikugie.loom-back-compat") version "0.4.2"
}

stonecutter {
    create(rootProject) {
        // One node per (minecraft version, loader) pair. The node name carries the loader suffix, so
        // the logical version must be given separately — otherwise Stonecutter parses "-neoforge" as a
        // semver pre-release tag and orders 26.1-neoforge BELOW 26.1.
        fun match(version: String, vararg loaders: String) = loaders.forEach {
            version("$version-$it", version).buildscript = "build.$it.gradle.kts"
        }

        match("1.20.1", "forge")

        vcsVersion = "1.20.1-forge"
    }
}

rootProject.name = "geontm"

// GeoCore supplies the cross-version API facades. Composite build during development so changes to
// it are picked up without a publish cycle; CI resolves the published artifact instead.
//
// Gradle cannot infer the substitutions on its own: each GeoCore node publishes under a derived
// artifactId (geocore-<loader>-<mc>) that does not match its project name (<mc>-<loader>), so the
// mapping has to be spelled out.
// Must list only nodes GeoCore actually declares — substituting a missing project fails the build.
// Keep in step with GeoCore/settings.gradle.kts as nodes are added.
includeBuild("../GeoCore") {
    dependencySubstitution {
        listOf("1.20.1-forge").forEach { node ->
            val (mc, loader) = node.split('-', limit = 2)
            substitute(module("fr.geoventure:geocore-$loader-$mc")).using(project(":$node"))
        }
    }
}
