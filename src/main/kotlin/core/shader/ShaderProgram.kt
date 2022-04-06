package core.shader

import core.math.Matrix4
import core.math.Vector3
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.Vector
import kotlin.system.exitProcess


abstract class ShaderProgram {
    private var programId: Int
    private var vertexShaderId: Int
    private var fragmentShaderId: Int

    constructor(vertexFile: String, fragmentFile: String) {
        vertexShaderId = loadShader(vertexFile, GL20.GL_VERTEX_SHADER)
        fragmentShaderId = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER)
        programId = GL20.glCreateProgram()
        GL20.glAttachShader(programId, vertexShaderId)
        GL20.glAttachShader(programId, fragmentShaderId)
        bindAttributes()
        GL20.glLinkProgram(programId)
        GL20.glValidateProgram(programId);
        getAllUniformLocation()
    }

    protected abstract fun getAllUniformLocation()
    protected abstract fun bindAttributes();

    protected fun getUniformLocation(uniformName: String): Int {
        return GL20.glGetUniformLocation(programId, uniformName)
    }

    fun start() {
        GL20.glUseProgram(programId)
    }

    fun stop() {
        GL20.glUseProgram(0)
    }

    fun cleanUp() {
        stop()
        GL20.glDetachShader(programId, vertexShaderId)
        GL20.glDetachShader(programId, fragmentShaderId)
        GL20.glDeleteShader(vertexShaderId)
        GL20.glDeleteShader(fragmentShaderId)
        GL20.glDeleteProgram(programId)
    }

    protected fun bindAttribute(attribute: Int, variableName: String) {
        GL20.glBindAttribLocation(programId, attribute, variableName)
    }

    protected fun loadFloat(location: Int, value: Float) {
        GL20.glUniform1f(location, value)
    }

    private fun loadVector(location: Int, vector: Vector3) {
        GL20.glUniform3f(location, vector.x(), vector.y(), vector.z())
    }

    protected fun loadBoolean(location: Int, value: Boolean) {
        GL20.glUniform1f(location, if (value) 1f else 0f)
    }

    protected fun loadMatrix(location: Int, matrix: Matrix4) {
        GL20.glUniformMatrix4fv(location, false, matrix.toBuffer())
    }

    companion object {
        private val matrixBuffer = BufferUtils.createFloatBuffer(16)
        private fun loadShader(file: String, type: Int): Int {
            val shaderSrc = StringBuilder()
            try {
                val reader = BufferedReader(FileReader(file))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    shaderSrc.append(line).append("//\n")
                }
                reader.close()
            } catch (e: IOException) {
                e.printStackTrace()
                exitProcess(-1)
            }

            val shaderID = GL20.glCreateShader(type)
            GL20.glShaderSource(shaderID, shaderSrc)
            GL20.glCompileShader(shaderID)
            if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
                println(GL20.glGetShaderInfoLog(shaderID, 500))
                System.err.println("Could not compile shader!")
                exitProcess(-1)
            }
            return shaderID
        }
    }
}