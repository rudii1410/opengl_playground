package core.renderengine

import core.math.Matrix4
import core.math.Vector3
import core.terrains.Terrain
import core.util.MathUtil
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import simplewindow.shader.terrain.TerrainShader

class TerrainRenderer {
    private val shader: TerrainShader

    constructor(shader: TerrainShader, projectionMatrix: Matrix4) {
        this.shader = shader
        shader.start()
        shader.loadProjectionMatrix(projectionMatrix)
        shader.connectTextureUnits()
        shader.stop()
    }

    fun render(terrains: List<Terrain>) {
        for (terrain in terrains) {
            prepareTexturedModel(terrain)
            prepareInstance(terrain)
            GL11.glDrawElements(
                GL11.GL_TRIANGLES,
                terrain.model.vertexCount,
                GL11.GL_UNSIGNED_INT,
                0
            )
            unbindTexturedModel()
        }
    }

    private fun prepareTexturedModel(terrain: Terrain) {
        val model = terrain.model
        GL30.glBindVertexArray(model.vaoId)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL20.glEnableVertexAttribArray(2)
        bindTextures(terrain)
        shader.loadShineVariables(1f, 0f)
    }

    private fun bindTextures(terrain: Terrain) {
        val texturePack = terrain.texturePack
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.bgTexture.textureId)
        GL13.glActiveTexture(GL13.GL_TEXTURE1)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.rTexture.textureId)
        GL13.glActiveTexture(GL13.GL_TEXTURE2)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.gTexture.textureId)
        GL13.glActiveTexture(GL13.GL_TEXTURE3)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.bTexture.textureId)
        GL13.glActiveTexture(GL13.GL_TEXTURE4)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.blendMap.textureId)
    }

    private fun unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL20.glDisableVertexAttribArray(2)
        GL30.glBindVertexArray(0)
    }

    private fun prepareInstance(terrain: Terrain) {
        shader.loadTransformationMatrix(
            MathUtil.createTransformationMatrix(terrain.getPosVec3(), Vector3(0f), 1f)
        )
    }
}