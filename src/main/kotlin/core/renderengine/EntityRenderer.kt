package core.renderengine

import core.entities.Entity
import core.math.Matrix4
import core.util.MathUtil
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import simplewindow.model.TexturedModel
import simplewindow.shader.static.StaticShader

class EntityRenderer {
    private val shader: StaticShader

    constructor(shader: StaticShader, projectionMatrix: Matrix4) {
        this.shader = shader
        shader.start()
        shader.loadProjectionMatrix(projectionMatrix)
        shader.stop()
    }

    fun render(entityList: Map<TexturedModel, List<Entity>>) {
        entityList.forEach {
            prepareTexturedModel(it.key)
            for (entity in it.value) {
                prepareInstance(entity)
                GL11.glDrawElements(
                    GL11.GL_TRIANGLES,
                    it.key.rawModel.vertexCount,
                    GL11.GL_UNSIGNED_INT,
                    0
                )
            }
            unbindTexturedModel()
        }
    }

    private fun prepareTexturedModel(texturedModel: TexturedModel) {
        val model = texturedModel.rawModel
        GL30.glBindVertexArray(model.vaoId)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL20.glEnableVertexAttribArray(2)
        shader.loadShineVariables(texturedModel.texture.shineDamper, texturedModel.texture.reflectivity)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.texture.textureId)
    }

    private fun unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL20.glDisableVertexAttribArray(2)
        GL30.glBindVertexArray(0)
    }

    private fun prepareInstance(entity: Entity) {
        shader.loadTransformationMatrix(
            MathUtil.createTransformationMatrix(entity.position, entity.rotation, entity.scale)
        )
    }
}