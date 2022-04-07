package simplewindow.shader.static

import core.entities.Camera
import core.entities.Light
import core.math.Matrix4
import core.shader.ShaderProgram
import core.util.MathUtil

class StaticShader : ShaderProgram(VERTEX_FILE, FRAGMENT_FILE) {
    private var locationTransformationMatrix = 0
    private var locationProjectionMatrix = 0
    private var locationViewMatrix = 0
    private var locationLightPos= 0
    private var locationLightColor = 0
    private var locationShineDamper = 0
    private var locationReflectivity = 0

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

    companion object {
        private const val SHADER_DIR = "src/main/kotlin/simplewindow/shader/static"
        private const val VERTEX_FILE = "$SHADER_DIR/vertexShader.txt"
        private const val FRAGMENT_FILE = "$SHADER_DIR/fragmentShader.txt"
    }
}