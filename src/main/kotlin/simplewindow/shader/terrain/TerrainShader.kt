package simplewindow.shader.terrain

import core.entities.Camera
import core.entities.Light
import core.math.Matrix4
import core.math.Vector3
import core.shader.ShaderProgram
import core.util.MathUtil

class TerrainShader : ShaderProgram(VERTEX_FILE, FRAGMENT_FILE) {
    private var locationTransformationMatrix = 0
    private var locationProjectionMatrix = 0
    private var locationViewMatrix = 0
    private var locationLightPos = 0

    private var locationLightColor = 0
    private var locationShineDamper = 0
    private var locationReflectivity = 0
    private var locationSkyColor = 0

    private var locationBgTexture = 0
    private var locationRTexture = 0
    private var locationGTexture = 0
    private var locationBTexture = 0
    private var locationBlendMap = 0

    override fun bindAttributes() {
        bindAttribute(0, "position")
        bindAttribute(1, "textures")
        bindAttribute(2, "normal")
    }

    override fun getAllUniformLocation() {
        locationTransformationMatrix = getUniformLocation("transformationMatrix")
        locationProjectionMatrix = getUniformLocation("projectionMatrix")
        locationViewMatrix = getUniformLocation("viewMatrix")
        locationLightPos = getUniformLocation("lightPosition")

        locationLightColor = getUniformLocation("lightColor")
        locationShineDamper = getUniformLocation("shineDamper")
        locationReflectivity = getUniformLocation("reflectivity")
        locationSkyColor = getUniformLocation("skyColor")

        locationBgTexture = getUniformLocation("bgTexture")
        locationRTexture = getUniformLocation("rTexture")
        locationGTexture = getUniformLocation("gTexture")
        locationBTexture = getUniformLocation("bTexture")
        locationBlendMap = getUniformLocation("blendMap")
    }

    fun loadTransformationMatrix(matrix: Matrix4) {
        loadMatrix(locationTransformationMatrix, matrix)
    }

    fun loadProjectionMatrix(matrix: Matrix4) {
        loadMatrix(locationProjectionMatrix, matrix)
    }

    fun loadViewMatrix(camera: Camera) {
        loadMatrix(locationViewMatrix, MathUtil.createViewMatrix(camera))
    }

    fun loadLight(light: Light) {
        loadVector(locationLightPos, light.position)
        loadVector(locationLightColor, light.color)
    }

    fun loadShineVariables(damper: Float, reflectivity: Float) {
        loadFloat(locationShineDamper, damper)
        loadFloat(locationReflectivity, reflectivity)
    }

    fun loadSkyColor(r: Float, g: Float, b: Float) {
        loadVector(locationSkyColor, Vector3(r, g, b))
    }

    fun connectTextureUnits() {
        loadInt(locationBgTexture, 0)
        loadInt(locationRTexture, 1)
        loadInt(locationGTexture, 2)
        loadInt(locationBTexture, 3)
        loadInt(locationBlendMap, 4)
    }

    companion object {
        private const val SHADER_DIR = "src/main/kotlin/simplewindow/shader/terrain"
        private const val VERTEX_FILE = "$SHADER_DIR/terrainVertexShader.txt"
        private const val FRAGMENT_FILE = "$SHADER_DIR/terrainFragmentShader.txt"
    }
}