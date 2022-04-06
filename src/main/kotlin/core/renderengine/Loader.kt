package core.renderengine

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.nio.FloatBuffer


class Loader {

    private val vaoList = ArrayList<Int>()
    private val vboList = ArrayList<Int>()

    fun loadToVao(pos: FloatArray): RawModel {
        val vaoId = createVao()
        storeDataInAttributeList(0, pos)
        unbindVao()
        return RawModel(vaoId, pos.size/3)
    }

    fun cleanUp() {
        for (vao in vaoList) GL30.glDeleteVertexArrays(vao)
        for (vbo in vboList) GL15.glDeleteBuffers(vbo)
    }

    private fun createVao(): Int {
        val vaoId = GL30.glGenVertexArrays()
        vaoList.add(vaoId)
        GL30.glBindVertexArray(vaoId)
        return vaoId
    }

    private fun storeDataInAttributeList(attributeNumber: Int, data: FloatArray) {
        val vboId = GL15.glGenBuffers()
        vboList.add(vboId)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId)
        GL15.glBufferData(
            GL15.GL_ARRAY_BUFFER,
            storeDataInFloatBuffer(data),
            GL15.GL_STATIC_DRAW
        )
        GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0)
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
    }

    private fun unbindVao() {
        GL30.glBindVertexArray(0)
    }

    private fun storeDataInFloatBuffer(data: FloatArray): FloatBuffer {
        val buffer = BufferUtils.createFloatBuffer(data.size)
        buffer.put(data)
        buffer.flip()
        return buffer;
    }
}