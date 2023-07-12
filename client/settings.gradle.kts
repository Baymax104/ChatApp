pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.aliyun.com/repository/public/")
        maven("https://maven.aliyun.com/repository/spring/")
        maven("https://maven.aliyun.com/repository/central/")
        maven("https://maven.aliyun.com/repository/google/")
        maven("https://maven.aliyun.com/repository/gradle-plugin/")
        maven("https://maven.aliyun.com/repository/spring-plugin/")
        maven("https://maven.aliyun.com/nexus/content/groups/public/" )
        maven("https://maven.aliyun.com/nexus/content/repositories/jcenter" )
        maven("https://jitpack.io" )
    }
}
rootProject.name = "ChatApp"
include(":app")
include(":BaseMVVM")
