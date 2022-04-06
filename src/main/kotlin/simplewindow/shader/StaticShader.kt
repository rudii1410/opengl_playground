package simplewindow.shader

import core.Camera
import core.math.Matrix4
import core.shader.ShaderProgram
import core.util.MathUtil

class StaticShader : ShaderProgram(VERTEX_FILE, FRAGMENT_FILE) {
    private var locationTransformationMatrix = 0
    private var locationProjectionMatrix = 0
    private var locationViewMatrix = 0

    override fun bindAttributes() {
        bindAttribute(0, "position")
        bindAttribute(1, "textures")
    }

    override fun getAllUniformLocation() {
        locationTransformationMatrix = getUniformLocation("transformationMatrix")
        locationProjectionMatrix = getUniformLocation("projectionMatrix")
        locationViewMatrix = getUniformLocation("viewMatrix")
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

    companion object {
        private const val SHADER_DIR = "src/main/kotlin/simplewindow/shader"
        private const val VERTEX_FILE = "$SHADER_DIR/vertexShader.txt"
        private const val FRAGMENT_FILE = "$SHADER_DIR/fragmentShader.txt"
    }
}