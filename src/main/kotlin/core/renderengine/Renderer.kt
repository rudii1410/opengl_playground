package core.renderengine

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import simplewindow.model.TexturedModel

class Renderer {
    fun prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
        GL11.glClearColor(1f, 0f, 0f, 1f)
    }

    fun render(texturedModel: TexturedModel) {
        val model = texturedModel.rawModel
        GL30.glBindVertexArray(model.vaoId)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.texture.textureId)
        GL11.glDrawElements(
            GL11.GL_TRIANGLES,
            model.vertexCount,
            GL11.GL_UNSIGNED_INT,
            0
        )
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL30.glBindVertexArray(0)
    }
}