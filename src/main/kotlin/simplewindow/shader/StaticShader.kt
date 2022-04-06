package simplewindow.shader

import core.shader.ShaderProgram

class StaticShader : ShaderProgram(VERTEX_FILE, FRAGMENT_FILE) {
    override fun bindAttributes() {
        super.bindAttribute(0, "position")
        super.bindAttribute(1, "textures")
    }

    companion object {
        private const val SHADER_DIR = "src/main/kotlin/simplewindow/shader"
        private const val VERTEX_FILE = "$SHADER_DIR/vertexShader.txt"
        private const val FRAGMENT_FILE = "$SHADER_DIR/fragmentShader.txt"
    }
}