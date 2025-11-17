// Nastavit temp adresář pro SQLite před načtením projektů
val tempDir = java.io.File(System.getProperty("user.home"), "AppData/Local/Temp").apply {
    if (!exists()) mkdirs()
}
val tempPath = tempDir.absolutePath.replace("\\", "/")
System.setProperty("java.io.tmpdir", tempPath)
System.setProperty("org.sqlite.tmpdir", tempPath)

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MovieExplorer"
include(":app")

