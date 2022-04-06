package core.renderengine

import core.graphics.Texture
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.nio.FloatBuffer
import java.nio.IntBuffer


class Loader {

    private val vaoList = ArrayList<Int>()
    private val vboList = ArrayList<Int>()
    private val textureList = ArrayList<Int>()

    fun loadToVao(pos: FloatArray, textures: FloatArray, indices: IntArray): RawModel {
        val vaoId = createVao()
        bindIndicesBuffer(indices)
        storeDataInAttributeList(0, 3, pos)
        storeDataInAttributeList(1, 2, textures)
        unbindVao()
        return RawModel(vaoId, indices.size)
    }

    fun loadTexture(fileLoc: String): Int {
        val id = Texture.loadTexture(fileLoc).id
        textureList.add(id)
        return id
    }

    fun cleanUp() {
        for (vao in vaoList) GL30.glDeleteVertexArrays(vao)
        for (vbo in vboList) GL15.glDeleteBuffers(vbo)
        for (texture in textureList) GL11.glDeleteTextures(texture)
    }

    private fun createVao(): Int {
        val vaoId = GL30.glGenVertexArrays()
        vaoList.add(vaoId)
        GL30.glBindVertexArray(vaoId)
        return vaoId
    }

    private fun storeDataInAttributeList(attributeNumber: Int, coordinateSize: Int, data: FloatArray) {
        val vboId = GL15.glGenBuffers()
        vboList.add(vboId)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
        GL15.glBufferData(
            GL15.GL_ARRAY_BUFFER,
            storeDataInFloatBuffer(data),
            GL15.GL_STATIC_DRAW
        )
        GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }

    private fun unbindVao() {
        GL30.glBindVertexArray(0)
    }

    private fun bindIndicesBuffer(indices: IntArray) {
        val vboId = GL15.glGenBuffers()
        vboList.add(vboId)
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId)
        GL15.glBufferData(
            GL15.GL_ELEMENT_ARRAY_BUFFER,
            storeDataInIntBuffer(indices),
            GL15.GL_STATIC_DRAW
        )
    }

    private fun storeDataInIntBuffer(data: IntArray): IntBuffer {
        return BufferUtils.createIntBuffer(data.size).also {
            it.put(data)
            it.flip()
        }
    }

    private fun storeDataInFloatBuffer(data: FloatArray): FloatBuffer {
        return BufferUtils.createFloatBuffer(data.size).also {
            it.put(data)
            it.flip()
        }
    }
}