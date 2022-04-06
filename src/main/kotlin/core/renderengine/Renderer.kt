package core.renderengine

import core.entities.Entity
import core.math.Matrix4
import core.util.MathUtil
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import simplewindow.shader.StaticShader
import kotlin.math.tan

class Renderer{
    private val projectionMatrix: Matrix4 = createProjectionMatrix()

    constructor(shader: StaticShader) {
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glCullFace(GL11.GL_BACK)
        shader.start()
        shader.loadProjectionMatrix(projectionMatrix)
        shader.stop()
    }

    fun prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
        GL11.glClearColor(1f, 1f, 0.5f, 1f)
    }

    fun render(entity: Entity, shader: StaticShader) {
        val texturedModel = entity.model
        val model = texturedModel.rawModel
        GL30.glBindVertexArray(model.vaoId)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL20.glEnableVertexAttribArray(2)
        shader.loadTransformationMatrix(
            MathUtil.createTransformationMatrix(entity.position, entity.rotation, entity.scale)
        )
        shader.loadShineVariables(texturedModel.texture.shineDamper, texturedModel.texture.reflectivity)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.texture.textureId)
        GL11.glDrawElements(
            GL11.GL_TRIANGLES,
            model.vertexCount,
            GL11.GL_UNSIGNED_INT,
            0
        )
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL20.glDisableVertexAttribArray(2)
        GL30.glBindVertexArray(0)
    }

    companion object {
        private const val FOV = 70f
        private const val NEAR_PLANE = 0.1f
        private const val FAR_PLANE = 1000f

        private fun createProjectionMatrix(): Matrix4 {
            val aspectRatio = 1280f / 720f
            val yScale = (1f / tan(Math.toRadians((FOV / 2f).toDouble()))) * aspectRatio
            val xScale = yScale / aspectRatio
            val frustumLength = FAR_PLANE - NEAR_PLANE

            return Matrix4().also {
                it.put(0, 0, xScale.toFloat())
                it.put(1, 1, yScale.toFloat())
                it.put(2, 2, -((FAR_PLANE + NEAR_PLANE) / frustumLength))
                it.put(2, 3, -1f)
                it.put(3, 2, -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength))
                it.put(3, 3, 0f)
            }
        }
    }
}