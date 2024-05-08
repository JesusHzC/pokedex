object ProjectConfig {
    const val appId = "com.github.jesushzc.pokedex"
    const val compileSdk = 34
    const val minSdk = 24
    const val targetSdk = 34

    const val versionCode = 1
    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 0

    fun getVersionName(): String {
        return "$versionMajor.$versionMinor.$versionPatch"
    }

    const val baseUrl = "https://pokeapi.co/api/v2/"

}
